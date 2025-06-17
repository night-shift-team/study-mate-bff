package com.studyMate.studyMate.domain.store.data;

public enum PaymentStatus {
    PENDING, // 결제 중 상태
    PAID, // 결제 완료 상태
    CANCELLED, // 유저가 취소한 상태
    FAILED // 실패상태
}
