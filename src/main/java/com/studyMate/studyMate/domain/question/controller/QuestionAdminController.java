package com.studyMate.studyMate.domain.question.controller;

import com.studyMate.studyMate.domain.question.dto.*;
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
@Tag(name = "Question Admin API (Required Role = 7+)")
public class QuestionAdminController {

    private final QuestionService questionService;

    @GetMapping(value = "/search-maq")
    @Operation(summary = "Admin Page MAQ 문제 검색 기능", description = "MAQ Question 문제 검색 (문제이름, 문제내용, 정답, 정답해설 내용에 키워드가 포함된 내용을 검색함 <최신순>)")
    @RoleAuth(requiredRole = 7)
    public MaqQuestionPageDto getMaqQuestionsLatest(
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        return questionService.searchMaqBykeyword(keyword, page, limit);
    }

    @GetMapping(value = "/search-saq")
    @Operation(summary = "Admin Page SAQ 문제 검색 기능", description = "SAQ Question 문제 검색 (문제이름, 문제내용, 정답, 정답해설 내용에 키워드가 포함된 내용을 검색함 <최신순>)")
//    @RoleAuth(requiredRole = 7)
    public SaqQuestionPageDto getSaqQuestionsLatest(
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        return questionService.searchSaqBykeyword(keyword, page, limit);
    }

    @GetMapping(value = "/{page}/{limit}/maq")
    @Operation(summary = "Admin Page MAQ Question 조회 (최신순)", description = "MAQ Question 전체 내역 조회")
    @RoleAuth(requiredRole = 7)
    public MaqQuestionPageDto getMaqQuestionsLatest(
        @PathVariable("page") Integer page,
        @PathVariable("limit") Integer limit
    ) {
        return questionService.findMaqQuestionsLatest(page, limit);
    }

    @GetMapping(value = "/{page}/{limit}/saq")
    @Operation(summary = "Admin Page SAQ Question 조회 (최신순)", description = "SAQ Question 전체 내역 조회")
    @RoleAuth(requiredRole = 7)
    public SaqQuestionPageDto getSaqQuestionsLatest(
            @PathVariable("page") Integer page,
            @PathVariable("limit") Integer limit
    ) {
        return questionService.findSaqQuestionsLatest(page, limit);
    }


    @PostMapping(value = "/maq")
    @Operation(summary = "Admin Page MAQ Question 생성", description = "MAQ Question 생성")
    @RoleAuth(requiredRole = 7)
    public String getMaqQuestionsLatest(
            HttpServletRequest req,
            @RequestBody @Valid CreateMaqQuestionRequestDto requestDto
    ) {
        return questionService.createMaqQuestion(requestDto);
    }

    @PostMapping(value = "/saq")
    @Operation(summary = "Admin Page SAQ Question 생성", description = "SAQ Question 생성")
    @RoleAuth(requiredRole = 7)
    public String getMaqQuestionsLatest(
            HttpServletRequest req,
            @RequestBody @Valid CreateSaqQuestionRequestDto requestDto
    ) {
        return questionService.createSaqQuestion(requestDto);
    }

    @PatchMapping(value = "/{question-id}/maq")
    @Operation(summary = "Admin Page MAQ Question 수정", description = "MAQ Question 수정")
    @RoleAuth(requiredRole = 7)
    public String updateMaqQuestion(
            HttpServletRequest req,
            @PathVariable("question-id") String questionId,
            @RequestBody @Valid CreateMaqQuestionRequestDto requestDto
    ) {
        return questionService.updateMaqQuestion(questionId, requestDto);
    }

    @PatchMapping(value = "/{question-id}/saq")
    @Operation(summary = "Admin Page SAQ Question 수정", description = "SAQ Question 수정")
    @RoleAuth(requiredRole = 7)
    public String updateSaqQuestion(
            HttpServletRequest req,
            @PathVariable("question-id") String questionId,
            @RequestBody @Valid CreateSaqQuestionRequestDto requestDto
    ) {
        return questionService.updateSaqQuestion(questionId, requestDto);
    }

    @PostMapping("/generator/maq")
    @Operation(summary = "MAQ Question 가라데이터 생성기 (테스트 전용)", description = "Question 가라데이터 생성 API")
    @RoleAuth(requiredRole = 7)
    public boolean generateFakeQuestions() {
        questionService.generateFakeQuestions();
        return true;
    }
}
