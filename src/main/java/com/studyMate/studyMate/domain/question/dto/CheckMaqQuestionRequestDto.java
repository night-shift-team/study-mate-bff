package com.studyMate.studyMate.domain.question.dto;

import java.util.List;

public record CheckMaqQuestionRequestDto(
    List<Long> questionIds,
    List<Integer> userAnswers
) {

}

