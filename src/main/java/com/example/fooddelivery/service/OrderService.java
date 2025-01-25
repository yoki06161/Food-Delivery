package com.example.fooddelivery.service;

import com.example.fooddelivery.dto.OrderRequest;
import com.example.fooddelivery.domain.*;
import com.example.fooddelivery.repository.MenuRepository;
import com.example.fooddelivery.repository.OrderRepository;
import com.example.fooddelivery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    // 주문 생성
    @Transactional
    public Order createOrder(String customerEmail, OrderRequest request) {
        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Order order = Order.builder()
                .customer(customer)
                .orderTime(LocalDateTime.now())
                .status(OrderStatus.ORDERED)
                .deliveryAddress(request.getAddress())
                .paymentMethod(request.getPaymentMethod())
                .orderLineItems(new ArrayList<>())
                .build();

        for (int i = 0; i < request.getMenuIds().size(); i++) {
            Long menuId = request.getMenuIds().get(i);
            int qty = request.getQuantities().get(i);

            Menu menu = menuRepository.findById(menuId)
                    .orElseThrow(() -> new RuntimeException("메뉴를 찾을 수 없습니다. ID = " + menuId));

            BigDecimal totalPrice = menu.getPrice().multiply(BigDecimal.valueOf(qty));

            OrderLineItem lineItem = OrderLineItem.builder()
                    .order(order)
                    .menu(menu)
                    .quantity(qty)
                    .totalPrice(totalPrice)
                    .build();
            order.getOrderLineItems().add(lineItem);
        }

        return orderRepository.save(order);
    }

    // 주문 결제
    @Transactional
    public Order payOrder(Long orderId, String customerEmail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다. ID = " + orderId));

        if (!order.getCustomer().getEmail().equals(customerEmail)) {
            throw new RuntimeException("주문 소유자가 아닙니다.");
        }

        // 실제 결제 로직이 필요 (여기서는 단순히 상태 변경)
        order.setStatus(OrderStatus.PAID);
        return orderRepository.save(order);
    }

    // 주문 상태 업데이트
    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다. ID = " + orderId));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
