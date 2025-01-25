package com.example.fooddelivery.controller;

import com.example.fooddelivery.dto.StoreDTO;
import com.example.fooddelivery.domain.Store;
import com.example.fooddelivery.domain.User;
import com.example.fooddelivery.service.StoreService;
import com.example.fooddelivery.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/store")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
    private final UserService userService;

    // 가게 생성 (OWNER 또는 ADMIN만 가능)
    @PostMapping
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public ResponseEntity<Long> createStore(@Valid @RequestBody StoreDTO storeDTO) {
        User owner = userService.findByEmail(storeDTO.getOwnerEmail());
        if (owner == null || owner.getRole() != com.example.fooddelivery.domain.UserRole.OWNER) {
            return ResponseEntity.status(403).body(null);
        }

        Store store = toEntity(storeDTO, owner);
        Store createdStore = storeService.createStore(store);
        return ResponseEntity.ok(createdStore.getId());
    }

    // 가게 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDTO> getStore(@PathVariable Long storeId) {
        Store store = storeService.getStoreById(storeId);
        StoreDTO dto = toDTO(store);
        return ResponseEntity.ok(dto);
    }

    // Store 엔티티를 DTO로 변환
    private StoreDTO toDTO(Store store) {
        StoreDTO dto = new StoreDTO();
        dto.setId(store.getId());
        dto.setStoreName(store.getStoreName());
        dto.setAddress(store.getAddress());
        dto.setPhone(store.getPhone());
        dto.setOwnerEmail(store.getOwner().getEmail());
        return dto;
    }

    // DTO를 Store 엔티티로 변환
    private Store toEntity(StoreDTO dto, User owner) {
        return Store.builder()
                .storeName(dto.getStoreName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .owner(owner)
                .build();
    }
}
