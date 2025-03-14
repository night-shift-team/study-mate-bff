package com.studyMate.studyMate.domain.question.dto;

import java.util.List;

public record CheckMaqQuestionRequestDto(
        String questionId,
        String userAnswer
) { }
