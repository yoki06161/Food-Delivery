package com.example.fooddelivery.domain;

public enum DeliveryStatus {
    WAITING,     // 배달 대기(라이더 미할당)
    PICKED_UP,   // 가게에서 음식을 픽업
    DELIVERING,  // 배달 중
    COMPLETED    // 배달 완료
}
