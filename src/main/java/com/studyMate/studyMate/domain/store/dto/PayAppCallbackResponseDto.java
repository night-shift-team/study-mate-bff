package com.studyMate.studyMate.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayAppCallbackResponseDto {

    private String orderId;
    private String userId;
    private Integer paidPrice;
}
