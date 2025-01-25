package com.example.fooddelivery.repository;

import com.example.fooddelivery.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
