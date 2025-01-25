package com.example.fooddelivery.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreDTO {
    private Long id;

    @NotBlank(message = "가게 이름은 필수 입력 항목입니다.")
    private String storeName;

    @NotBlank(message = "주소는 필수 입력 항목입니다.")
    private String address;

    @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
    private String phone;

    @NotBlank(message = "소유자 이메일은 필수 입력 항목입니다.")
    private String ownerEmail;
}
