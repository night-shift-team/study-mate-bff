package com.studyMate.studyMate.domain.question.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CheckMaqQuestionResponseDto (
    // 백분위 환산 점수 결과
    Double percentileScore,

    // 배치 점수
    Integer yourInitScore,

    // 제출한 문제 수
    Integer requestedQuestionCount,

    // 정답 문제
    List<String> correctQuestions,

    // 오답 문제
    List<String> wrongQuestions
) { }