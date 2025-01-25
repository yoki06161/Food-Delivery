package com.example.fooddelivery.repository;

import com.example.fooddelivery.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByStoreId(Long storeId);
    
    Optional<Menu> findByIdAndStoreId(Long id, Long storeId);
}
