package com.studyMate.studyMate.domain.question.controller;

import com.studyMate.studyMate.domain.question.dto.CheckMaqQuestionRequestDto;
import com.studyMate.studyMate.domain.question.dto.CheckMaqQuestionResponseDto;
import com.studyMate.studyMate.domain.question.dto.MaqQuestionDto;
import com.studyMate.studyMate.domain.question.entity.MAQ;
import com.studyMate.studyMate.domain.question.service.QuestionService;
import com.studyMate.studyMate.global.config.RoleAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/maq")
    @Operation(summary = "MAQ Questions", description = "MAQ Questions")
    public List<MAQ> getMaqQuestions() {
        return questionService.findMaqAll();
    }

    @GetMapping("/level-test")
    @Operation(summary = "레벨 테스트 문제 출제", description = "레벨 테스트 문제 출제")
    @RoleAuth
    public List<MaqQuestionDto> getLevelTestQuestions() {
        return questionService.getLevelTestQuestions();
    }

    @PostMapping("/check/level-test")
    @Operation(summary = "레벨 테스트 문제 결과 제출", description = "레벨 테스트에서 풀이한 문제에 대하여 정답을 체크하고, History에 기록함")
    @RoleAuth
    public CheckMaqQuestionResponseDto checkLevelTestQuestions(
            HttpServletRequest request,
            @RequestBody CheckMaqQuestionRequestDto body
    ) {
        long userId = (Long) request.getAttribute("userId");
        return questionService.checkLevelTestQuestions(body.questionIds(), body.userAnswers(), userId);
    }
}
