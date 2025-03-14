package com.studyMate.studyMate.domain.question.dto;

import java.util.List;

public record CheckMaqQuestionsRequestDto(
    List<String> questionIds,
    List<String> userAnswers
) {

}

