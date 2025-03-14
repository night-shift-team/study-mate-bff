package com.studyMate.studyMate.domain.question.controller;

import com.querydsl.core.QueryResults;
import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import com.studyMate.studyMate.domain.question.dto.CheckMaqQuestionRequestDto;
import com.studyMate.studyMate.domain.question.dto.CheckMaqQuestionResponseDto;
import com.studyMate.studyMate.domain.question.dto.GetQuestionDetailResponseDto;
import com.studyMate.studyMate.domain.question.dto.MaqQuestionDto;
import com.studyMate.studyMate.domain.question.entity.MAQ;
import com.studyMate.studyMate.domain.question.service.QuestionService;
import com.studyMate.studyMate.global.config.RoleAuth;
import com.studyMate.studyMate.global.error.CustomException;
import com.studyMate.studyMate.global.error.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
@Tag(name = "Question API")
public class QuestionController {

    private final QuestionService questionService;


    @GetMapping("/{category}/maq")
    @Operation(summary = "Questions 조회 (카테고리)", description = "유저가 풀지않은 문제 중, 카테고리에 맞추어, 유저 점수에 맞추어 Difficulty를 산정하여 문제를 1개 출제함.")
    @RoleAuth
    public MaqQuestionDto getMaqQuestionsByCategory(
            HttpServletRequest req,
            @PathVariable("category")
            @Parameter(
                    description = "MAQ 카테고리만 허용",
                    schema = @Schema(allowableValues = {"ALGORITHUM_MAQ", "OS_MAQ", "NETWORK_MAQ", "DB_MAQ", "DESIGN_MAQ"})
            ) QuestionCategory category
    ) {
        if(category.getType() != QuestionCategory.Type.MAQ){
            throw new CustomException(ErrorCode.INVALID_QUESTION_CATEOGRY);
        }

        String userId = (String) req.getAttribute("userId");
        return questionService.findMaqQuestionsByCategory(category, userId);
    }

    @GetMapping("/{category}/saq")
    @Operation(summary = "Questions 조회 (카테고리)", description = "유저가 풀지않은 문제 중, 카테고리에 맞추어, 유저 점수에 맞추어 Difficulty를 산정하여 문제를 1개 출제함.")
    @RoleAuth
    public MaqQuestionDto getSaqQuestionsByCategory(
            HttpServletRequest req,
            @PathVariable("category")
            @Parameter(
                    description = "SAQ 카테고리만 허용",
                    schema = @Schema(allowableValues = {"ALGORITHUM_SAQ", "OS_SAQ", "NETWORK_SAQ", "DB_SAQ", "DESIGN_SAQ"})
            ) QuestionCategory category
    ) {
        if(category.getType() != QuestionCategory.Type.SAQ){
            throw new CustomException(ErrorCode.INVALID_QUESTION_CATEOGRY);
        }

        String userId = (String) req.getAttribute("userId");
        return questionService.findMaqQuestionsByCategory(category, userId);
    }

    @PostMapping("/generator/admin")
    @Operation(summary = "Question 가라데이터 생성기 (테스트 전용)", description = "Question 가라데이터 생성 API")
    public boolean generateFakeQuestions() {
        questionService.generateFakeQuestions();
        return true;
    }

    @GetMapping("/{questionId}")
    @Operation(summary = "Question 상세 정보 조회", description = "Question 상세정보 조회 API (MAQ | SAQ 공통사용) (*일반유저는 자신이 풀었던 내역에 대해서만 조회할 수 있음 | 어드민은 무조건 조회)")
    @RoleAuth
    public GetQuestionDetailResponseDto getQuestionDetail(HttpServletRequest req, @PathVariable("questionId") String questionId) {
        String userId = (String) req.getAttribute("userId");
        return questionService.findQuestionDetailById(questionId, userId);
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
            HttpServletRequest req,
            @RequestBody CheckMaqQuestionRequestDto body
    ) {
        String userId = (String) req.getAttribute("userId");
        return questionService.checkLevelTestQuestions(body.questionIds(), body.userAnswers(), userId);
    }
}
