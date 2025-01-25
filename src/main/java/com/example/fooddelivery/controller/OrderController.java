package com.example.fooddelivery.controller;

import com.example.fooddelivery.dto.OrderRequest;
import com.example.fooddelivery.domain.Order;
import com.example.fooddelivery.domain.OrderStatus;
import com.example.fooddelivery.security.JwtProvider;
import com.example.fooddelivery.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final JwtProvider jwtProvider;

    // 주문 생성 (고객만 가능)
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> createOrder(
            @RequestHeader("Authorization") String bearerToken,
            @Valid @RequestBody OrderRequest orderRequest) {
        
        String token = bearerToken.substring(7);
        String email = jwtProvider.getAuthentication(token).getName();

        Order order = orderService.createOrder(email, orderRequest);
        return ResponseEntity.ok(order.getId());
    }

    // 주문 결제 (고객만 가능)
    @PostMapping("/{orderId}/pay")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> payOrder(@PathVariable("orderId") Long orderId,
                                      @RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.substring(7);
        String email = jwtProvider.getAuthentication(token).getName();

        Order order = orderService.payOrder(orderId, email);
        return ResponseEntity.ok(order);
    }

    // 주문 상태 업데이트
    @PostMapping("/{orderId}/status")
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable("orderId") Long orderId,
            @RequestParam("status") OrderStatus status) {
        Order order = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(order);
    }
}
