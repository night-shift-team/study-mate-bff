package com.studyMate.studyMate.domain.store.dto.vo;

import com.studyMate.studyMate.domain.store.data.PaymentStatus;
import com.studyMate.studyMate.domain.store.entity.StoreOrders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private String orderId;
    private String itemName;
    private String paymentMethod;
    private Integer price;
    private LocalDateTime paymentDate;
    private PaymentStatus status;

    public OrderDto(StoreOrders storeOrders){
        this.orderId = storeOrders.getOrderId();
        this.itemName = storeOrders.getItemName();
        this.paymentMethod = storeOrders.getPaymentMethod();
        this.price = storeOrders.getPaidPrice();
        this.paymentDate = storeOrders.getPayDate();
        this.status = storeOrders.getStatus();
    }
}
