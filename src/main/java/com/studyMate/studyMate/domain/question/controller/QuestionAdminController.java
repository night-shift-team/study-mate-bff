package com.studyMate.studyMate.domain.question.controller;

import com.studyMate.studyMate.domain.question.dto.CreateMaqQuestionRequestDto;
import com.studyMate.studyMate.domain.question.dto.MaqQuestionDto;
import com.studyMate.studyMate.domain.question.dto.MaqQuestionPageDto;
import com.studyMate.studyMate.domain.question.dto.SaqQuestionPageDto;
import com.studyMate.studyMate.domain.question.entity.MAQ;
import com.studyMate.studyMate.domain.question.service.QuestionService;
import com.studyMate.studyMate.global.config.RoleAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question/admin")
@Tag(name = "Question Admin API")
public class QuestionAdminController {

    private final QuestionService questionService;

    @GetMapping(value = "/{page}/{limit}/maq")
    @Operation(summary = "Admin Page MAQ Question 조회 (최신순)", description = "MAQ Question 전체 내역 조회")
    public MaqQuestionPageDto getMaqQuestionsLatest(
        @PathVariable("page") Integer page,
        @PathVariable("limit") Integer limit
    ) {
        return questionService.findMaqQuestionsLatest(page, limit);
    }

    @GetMapping(value = "/{page}/{limit}/saq")
    @Operation(summary = "Admin Page SAQ Question 조회 (최신순)", description = "SAQ Question 전체 내역 조회")
    public SaqQuestionPageDto getSaqQuestionsLatest(
            @PathVariable("page") Integer page,
            @PathVariable("limit") Integer limit
    ) {
        return questionService.findSaqQuestionsLatest(page, limit);
    }


    @PostMapping(value = "/maq")
    @Operation(summary = "Admin Page MAQ Question 생성", description = "MAQ Question 생성")
    public String getMaqQuestionsLatest(
            HttpServletRequest req,
            @RequestBody @Valid CreateMaqQuestionRequestDto requestDto
    ) {
        return questionService.createMaqQuestion(requestDto);
    }

    @PostMapping("/generator/maq")
    @Operation(summary = "MAQ Question 가라데이터 생성기 (테스트 전용)", description = "Question 가라데이터 생성 API")
//    @RoleAuth(requiredRole = 7)
    public boolean generateFakeQuestions() {
        questionService.generateFakeQuestions();
        return true;
    }
}
