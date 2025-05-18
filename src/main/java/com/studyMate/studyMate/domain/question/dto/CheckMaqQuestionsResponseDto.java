package com.studyMate.studyMate.domain.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckMaqQuestionsResponseDto {
    // 백분위 환산 점수 결과
    private Double percentileScore;

    // 배치 점수
    private Integer yourInitScore;

    // 제출한 문제 수
    private Integer requestedQuestionCount;

    // 정답 문제
    private List<String> correctQuestions;

    // 오답 문제
    private List<String> wrongQuestions;
}
