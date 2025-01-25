package com.example.fooddelivery.domain;

public enum OrderStatus {
    ORDERED,    // 주문 접수
    PAID,       // 결제 완료
    COOKING,    // 조리 중
    DELIVERY,   // 배달 중
    COMPLETED,  // 배달 완료
    CANCELED
}
