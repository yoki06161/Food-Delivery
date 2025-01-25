package com.example.fooddelivery.controller;

import com.example.fooddelivery.domain.Delivery;
import com.example.fooddelivery.domain.DeliveryStatus;
import com.example.fooddelivery.domain.DeliveryUpdateMessage;
import com.example.fooddelivery.domain.User;
import com.example.fooddelivery.domain.UserRole;
import com.example.fooddelivery.security.JwtProvider;
import com.example.fooddelivery.service.DeliveryService;
import com.example.fooddelivery.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    // 배달 생성 (라이더만 가능)
    @PostMapping("/{orderId}")
    @PreAuthorize("hasRole('RIDER')") // 역할 기반 접근 제어
    public ResponseEntity<?> createDelivery(@RequestHeader("Authorization") String bearerToken,
                                            @PathVariable("orderId") Long orderId) {
        String token = bearerToken.substring(7);
        String email = jwtProvider.getAuthentication(token).getName();

        User rider = userService.findByEmail(email);
        if (rider == null || rider.getRole() != UserRole.RIDER) {
            return ResponseEntity.status(403).body("라이더 정보가 없습니다.");
        }

        Delivery delivery = deliveryService.createDelivery(orderId, rider);
        return ResponseEntity.ok(delivery.getId());
    }

    // 배달 상태 업데이트 (라이더 또는 관리자 가능)
    @PostMapping("/{deliveryId}/status")
    @PreAuthorize("hasRole('RIDER') or hasRole('ADMIN')") // 역할 기반 접근 제어
    public ResponseEntity<?> updateDeliveryStatus(@PathVariable("deliveryId") Long deliveryId,
                                                  @RequestParam("status") DeliveryStatus status,
                                                  @RequestParam(value = "location", required = false) String location) {
        Delivery delivery = deliveryService.updateDeliveryStatus(deliveryId, status, location);

        DeliveryUpdateMessage updateMessage = DeliveryUpdateMessage.builder()
                .deliveryId(delivery.getId())
                .orderId(delivery.getOrder().getId())
                .riderId(delivery.getRider().getId())
                .status(delivery.getStatus())
                .currentLocation(delivery.getCurrentLocation())
                .timestamp(LocalDateTime.now())
                .build();

        messagingTemplate.convertAndSend("/topic/deliveryUpdates", updateMessage);

        return ResponseEntity.ok(delivery);
    }
}
