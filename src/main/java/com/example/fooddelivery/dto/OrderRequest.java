package com.example.fooddelivery.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {
    @NotEmpty(message = "메뉴 ID 목록은 필수 입력 항목입니다.")
    private List<Long> menuIds;

    @NotEmpty(message = "수량 목록은 필수 입력 항목입니다.")
    private List<Integer> quantities;

    @NotNull(message = "배송 주소는 필수 입력 항목입니다.")
    private String address;

    @NotNull(message = "결제 방법은 필수 입력 항목입니다.")
    private String paymentMethod;
}
