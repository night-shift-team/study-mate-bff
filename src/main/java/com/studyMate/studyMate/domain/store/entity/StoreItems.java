package com.studyMate.studyMate.domain.store.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.studyMate.studyMate.global.data.BaseEntityDate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "store_items")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreItems extends BaseEntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "item_id")
    private String itemId;

    @Column
    private String name;

    @Column
    private String description;

    @Column(name = "item_image")
    private String itemImage;

    @Column(name = "price_krw")
    private Integer priceKrw;

    @Column(name = "view_expired_dt")
    @Schema(description = "상품 노출 만료일", example = "2024-09-28T16:23:00.00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime viewExpiredDt;
}
