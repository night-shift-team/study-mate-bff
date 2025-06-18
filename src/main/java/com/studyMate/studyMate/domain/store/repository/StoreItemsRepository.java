package com.studyMate.studyMate.domain.store.repository;

import com.studyMate.studyMate.domain.store.entity.StoreItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreItemsRepository extends JpaRepository<StoreItems, String> {
    Optional<StoreItems> findByItemId(String itemId);
}
