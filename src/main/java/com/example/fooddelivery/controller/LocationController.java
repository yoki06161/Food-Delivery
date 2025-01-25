package com.example.fooddelivery.controller;

import com.example.fooddelivery.domain.Delivery;
import com.example.fooddelivery.domain.DeliveryStatus;
import com.example.fooddelivery.domain.DeliveryUpdateMessage;
import com.example.fooddelivery.domain.LocationUpdateMessage;
import com.example.fooddelivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class LocationController {

    private final SimpMessagingTemplate messagingTemplate;
    private final DeliveryService deliveryService;

    @MessageMapping("/location")
    public void updateLocation(LocationUpdateMessage message) {
        // 현재 라이더가 담당하는 배달 찾기
        Delivery delivery = deliveryService.findActiveDeliveryByRiderId(message.getRiderId());
        if (delivery != null) {
            // 배달의 현재 위치 업데이트
            String currentLocation = message.getLatitude() + "," + message.getLongitude();
            deliveryService.updateDeliveryStatus(delivery.getId(), DeliveryStatus.DELIVERING, currentLocation);

            // DeliveryUpdateMessage 생성
            DeliveryUpdateMessage updateMessage = DeliveryUpdateMessage.builder()
                    .deliveryId(delivery.getId())
                    .orderId(delivery.getOrder().getId())
                    .riderId(delivery.getRider().getId())
                    .status(delivery.getStatus())
                    .currentLocation(delivery.getCurrentLocation())
                    .timestamp(LocalDateTime.now())
                    .build();

            // WebSocket을 통해 메시지 브로드캐스트
            messagingTemplate.convertAndSend("/topic/deliveryUpdates", updateMessage);
        }
    }
}
