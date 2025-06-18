package com.studyMate.studyMate.domain.store.data;

public enum PaymentStatus {
    REQUEST, // 요청 상태
    PAID, // 결제 완료 상태

    PENDING, // 결제 대기

    REQ_CANCELLED, // 요청 취소
    PAY_ALL_CANCELLED, // 결제 취소
    PAY_PARTIAL_CANCELLED, // 부분 결제 취소

    FAILED // 실패상태
}
