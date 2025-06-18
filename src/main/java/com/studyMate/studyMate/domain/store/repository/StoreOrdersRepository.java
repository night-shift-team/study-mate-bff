package com.studyMate.studyMate.domain.store.repository;

import com.studyMate.studyMate.domain.store.entity.StoreOrders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreOrdersRepository extends JpaRepository<StoreOrders, String> {
}
