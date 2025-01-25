package com.example.fooddelivery.repository;

import com.example.fooddelivery.domain.Delivery;
import com.example.fooddelivery.domain.DeliveryStatus;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
	Optional<Delivery> findTopByRiderIdAndStatusInOrderByIdDesc(Long riderId, List<DeliveryStatus> statuses);
}
