package com.example.fooddelivery.service;

import com.example.fooddelivery.domain.Store;
import com.example.fooddelivery.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    @Transactional
    public Store createStore(Store store) {
        return storeRepository.save(store);
    }

    @Transactional(readOnly = true)
    public Store getStoreById(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("가게를 찾을 수 없습니다. ID = " + storeId));
    }
}
