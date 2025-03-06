package com.studyMate.studyMate.domain.question.controller;

import com.studyMate.studyMate.domain.question.entity.MAQ;
import com.studyMate.studyMate.domain.question.entity.Question;
import com.studyMate.studyMate.domain.question.entity.UAQ;
import com.studyMate.studyMate.domain.question.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
@Tag(name = "Question API")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/generator/admin")
    @Operation(summary = "Question 가라데이터 생성기 (테스트 전용)", description = "Question 가라데이터 생성 API")
    public boolean generateFakeQuestions() {
        questionService.generateFakeQuestions();
        return true;
    }

    @GetMapping("/uaq")
    @Operation(summary = "Questions", description = "Questions")
    public List<UAQ> getQuestions() {
        return questionService.findUaqAll();
    }

    @GetMapping("/maq")
    @Operation(summary = "MAQ Questions", description = "MAQ Questions")
    public List<MAQ> getMaqQuestions() {
        return questionService.findMaqAll();
    }
}
