package com.example.fooddelivery.service;

import com.example.fooddelivery.domain.*;
import com.example.fooddelivery.repository.DeliveryRepository;
import com.example.fooddelivery.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;

    // 배달 생성
    @Transactional
    public Delivery createDelivery(Long orderId, User rider) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        Delivery delivery = Delivery.builder()
                .order(order)
                .rider(rider)
                .status(DeliveryStatus.WAITING)
                .build();
        return deliveryRepository.save(delivery);
    }

    // 배달 상태 업데이트
    @Transactional
    public Delivery updateDeliveryStatus(Long deliveryId, DeliveryStatus status, String currentLocation) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("배달 정보를 찾을 수 없습니다. ID = " + deliveryId));

        delivery.setStatus(status);
        if (currentLocation != null) {
            delivery.setCurrentLocation(currentLocation);
        }

        // 배달 완료 시 주문 상태도 업데이트
        if (status == DeliveryStatus.COMPLETED) {
            delivery.getOrder().setStatus(OrderStatus.COMPLETED);
        }

        return deliveryRepository.save(delivery);
    }

    // 활성 배달 찾기
    @Transactional(readOnly = true)
    public Delivery findActiveDeliveryByRiderId(Long riderId) {
        return deliveryRepository.findTopByRiderIdAndStatusInOrderByIdDesc(
                riderId,
                List.of(DeliveryStatus.WAITING, DeliveryStatus.PICKED_UP, DeliveryStatus.DELIVERING)
        ).orElse(null);
    }
}
