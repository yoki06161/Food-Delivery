package com.example.fooddelivery.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {

    @Id @GeneratedValue
    private Long id;

    @OneToOne
    private Order order;

    @ManyToOne
    private User rider;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private String currentLocation; // 예: 경도, 위도, 혹은 주소
}
