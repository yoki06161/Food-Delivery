package com.example.fooddelivery.repository;

import com.example.fooddelivery.domain.RiderLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiderLocationRepository extends JpaRepository<RiderLocation, Long> {
}