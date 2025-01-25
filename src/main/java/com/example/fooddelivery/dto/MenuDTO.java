package com.example.fooddelivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuDTO {
    private Long id;

    @NotNull(message = "Store ID는 필수 입력 항목입니다.")
    private Long storeId;

    @NotBlank(message = "메뉴 이름은 필수 입력 항목입니다.")
    private String name;

    @NotNull(message = "가격은 필수 입력 항목입니다.")
    private BigDecimal price;

    private boolean soldOut;
}
