package com.studyMate.studyMate.domain.store.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.global.data.BaseEntityDate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_items")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserItems extends BaseEntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_item_id")
    private String userItemId;

    // 대상 유저
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 대상 아이템
    @ManyToOne
    @JoinColumn(name = "store_item_id", nullable = false)
    private StoreItems storeItem;
//
    // 아이템 만료일
    @Column(name = "expired_dt")
    @Schema(description = "상품 소유 만료일", example = "2024-09-28T16:23:00.00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expiredDt;
}
