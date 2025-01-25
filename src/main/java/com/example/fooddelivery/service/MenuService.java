package com.example.fooddelivery.service;

import com.example.fooddelivery.domain.Menu;
import com.example.fooddelivery.repository.MenuRepository;
import com.example.fooddelivery.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public Menu addMenu(Menu menu) {
        // Store 존재 여부 확인
        storeRepository.findById(menu.getStore().getId()).orElseThrow(() -> {
            return new RuntimeException("Store not found");
        });

        Menu savedMenu = menuRepository.save(menu);
        return savedMenu;
    }

    public List<Menu> getMenusByStoreId(Long storeId) {
        return menuRepository.findByStoreId(storeId);
    }

    @Transactional
    public Menu updateMenu(Menu menu) {
        Menu existingMenu = menuRepository.findById(menu.getId()).orElseThrow(() -> {
            return new RuntimeException("Menu not found");
        });

        existingMenu.setName(menu.getName());
        existingMenu.setPrice(menu.getPrice());
        existingMenu.setSoldOut(menu.isSoldOut());
        // 필요한 다른 필드들도 업데이트

        Menu updatedMenu = menuRepository.save(existingMenu);
        return updatedMenu;
    }

    @Transactional
    public void deleteMenu(Long storeId, Long menuId) {
        Menu menu = menuRepository.findByIdAndStoreId(menuId, storeId).orElseThrow(() -> {
            return new RuntimeException("Menu not found");
        });
        menuRepository.delete(menu);
    }
}
