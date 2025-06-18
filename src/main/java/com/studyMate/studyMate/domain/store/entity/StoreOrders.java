package com.studyMate.studyMate.domain.store.entity;

import com.studyMate.studyMate.domain.store.data.PaymentStatus;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.global.data.BaseEntityDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "store_orders")
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class StoreOrders extends BaseEntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private String orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "payapp_order_id")
    private String payAppOrderId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "paid_price")
    private Integer paidPrice;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "cancel_memo", nullable = true)
    private String cancelMemo;

    @Column(name = "cancel_date", nullable = true)
    private LocalDateTime cancelDate;

    @Column(name = "pay_req_date")
    private LocalDateTime payReqDate;

    @Column(name = "pay_date")
    private LocalDateTime payDate;

    @Column(name = "payapp_raw", nullable = false, columnDefinition = "LONGTEXT")
    private String payAppRaw;
}
