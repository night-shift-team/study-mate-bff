package com.studyMate.studyMate.domain.boards.data;

/**
 * 게시판 상태 (건의사항에 사용)
 */
public enum BoardStatus {
    RECEIVED, // 접수완료
    IN_REVIEW, // 검토중
    REJECTED, // 반려
    ACCEPTED // 승인
}
