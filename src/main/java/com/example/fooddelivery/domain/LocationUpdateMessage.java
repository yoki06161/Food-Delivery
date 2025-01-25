package com.example.fooddelivery.domain;

import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class LocationUpdateMessage {
    private Long riderId;       // 라이더 식별자
    private double latitude;    // 위도
    private double longitude;   // 경도
    private String status;      // 상태(예: 'DELIVERING')
}
