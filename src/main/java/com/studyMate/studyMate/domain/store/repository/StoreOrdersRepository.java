package com.studyMate.studyMate.domain.store.repository;

import com.studyMate.studyMate.domain.store.entity.StoreItems;
import com.studyMate.studyMate.domain.store.entity.StoreOrders;
import com.studyMate.studyMate.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreOrdersRepository extends JpaRepository<StoreOrders, String> {
    Page<StoreOrders> findAllByUser(Pageable pageable, User user);
}
