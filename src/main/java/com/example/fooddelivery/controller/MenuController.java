package com.example.fooddelivery.controller;

import com.example.fooddelivery.dto.MenuDTO;
import com.example.fooddelivery.domain.Menu;
import com.example.fooddelivery.domain.Store;
import com.example.fooddelivery.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/menu")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    // 메뉴 추가
    @PostMapping
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')") // 역할 기반 접근 제어
    public ResponseEntity<Long> addMenu(@Valid @RequestBody MenuDTO menuDTO) {
        try {
            Menu menu = toEntity(menuDTO);
            Menu createdMenu = menuService.addMenu(menu);
            return ResponseEntity.ok(createdMenu.getId());
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 특정 가게의 메뉴 목록 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<List<MenuDTO>> getMenus(@PathVariable("storeId") Long storeId) {
        try {
            List<Menu> menus = menuService.getMenusByStoreId(storeId);
            List<MenuDTO> menuDTOs = menus.stream().map(this::toDTO).collect(Collectors.toList());
            return ResponseEntity.ok(menuDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 메뉴 수정
    @PutMapping
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')") // 역할 기반 접근 제어
    public ResponseEntity<MenuDTO> updateMenu(@Valid @RequestBody MenuDTO menuDTO) {
        try {
            Menu menu = toEntity(menuDTO);
            Menu updatedMenu = menuService.updateMenu(menu);
            return ResponseEntity.ok(toDTO(updatedMenu));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 메뉴 삭제
    @DeleteMapping("/{storeId}/{menuId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')") // 역할 기반 접근 제어
    public ResponseEntity<Void> deleteMenu(@PathVariable("storeId") Long storeId, @PathVariable("menuId") Long menuId) {
        try {
            menuService.deleteMenu(storeId, menuId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // Menu 엔티티를 DTO로 변환
    private MenuDTO toDTO(Menu menu) {
        MenuDTO dto = new MenuDTO();
        dto.setId(menu.getId());
        dto.setStoreId(menu.getStore().getId());
        dto.setName(menu.getName());
        dto.setPrice(menu.getPrice());
        dto.setSoldOut(menu.isSoldOut());
        return dto;
    }

    // DTO를 Menu 엔티티로 변환
    private Menu toEntity(MenuDTO dto) {
        Menu menu = new Menu();
        menu.setId(dto.getId());
        menu.setStore(Store.builder().id(dto.getStoreId()).build());
        menu.setName(dto.getName());
        menu.setPrice(dto.getPrice());
        menu.setSoldOut(dto.isSoldOut());
        return menu;
    }
}
