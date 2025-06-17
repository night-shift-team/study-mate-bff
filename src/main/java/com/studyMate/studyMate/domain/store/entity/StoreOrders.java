package com.studyMate.studyMate.domain.store.entity;

import com.studyMate.studyMate.domain.store.data.PaymentStatus;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.global.data.BaseEntityDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "store_orders")
@Getter
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

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "paid_price")
    private Integer paidPrice;

    @Column(name = "payapp_raw")
    private String payAppRaw;
}
