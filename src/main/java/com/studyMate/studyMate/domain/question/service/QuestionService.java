package com.studyMate.studyMate.domain.question.service;

import com.querydsl.core.QueryResults;
import com.studyMate.studyMate.domain.history.dto.QuestionHistoryDto;
import com.studyMate.studyMate.domain.history.repository.QuestionHistoryRepository;
import com.studyMate.studyMate.domain.history.service.QuestionHistoryService;
import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import com.studyMate.studyMate.domain.question.dto.*;
import com.studyMate.studyMate.domain.question.entity.MAQ;
import com.studyMate.studyMate.domain.history.entity.QuestionHistory;
import com.studyMate.studyMate.domain.question.entity.SAQ;
import com.studyMate.studyMate.domain.question.repository.QuestionMaqRepository;
import com.studyMate.studyMate.domain.question.repository.QuestionRepository;
import com.studyMate.studyMate.domain.question.repository.QuestionSaqRepository;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.domain.user.repository.UserRepository;
import com.studyMate.studyMate.global.error.CustomException;
import com.studyMate.studyMate.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMaqRepository questionMaqRepository;
    private final QuestionSaqRepository questionSaqRepository;
    private final QuestionHistoryService questionHistoryService;
    private final UserRepository userRepository;

    private final int MAX_LEVEL_TEST_DIFFICULTY = 20;
    private final int LEVEL_TEST_QUESTION_COUNT = 20;
    private final int LEVEL_TEST_SCORE_WIEGHT = 100;

    private final QuestionHistoryRepository questionHistoryRepository;

    public MaqQuestionPageDto findMaqQuestionsLatest(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDt"));
        Page<MAQ> query = questionMaqRepository.findAll(pageRequest);
        return new MaqQuestionPageDto(query);
    }

    /**
     * Question 랜덤 출제기능 (By. Question Category)
     * 이미 유저가 맞춘 문제에 대해서는 출제하지 않으며,
     * 유저의 Score 를 기반으로 적절한 Difficulty에 맞는 문제를 탐색하고,
     * 유저가 맞추지 못한 문제중 랜덤으로 문제를 1개 출제한다.
     * @param questionCategory
     * @param userId
     */
    public MaqQuestionDto findMaqQuestionsCommon(QuestionCategory questionCategory, String userId) {
        // 1. 유저의 적정 Difficulty를 뽑아라.
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.INVALID_USERID));
        Integer userScore = user.getScore();
        List<Integer> userProperDifficulty = getUserProperDifficulty(userScore);
        PageRequest pageReq = PageRequest.of(0, 1);

        // 2. 조건에 맞추어 조회,
        Page<MAQ> query = questionRepository.findRandMaqQuestionsByDifficultyAndCategoryAndPaging(
                userProperDifficulty.get(0),
                userProperDifficulty.get(1),
                questionCategory,
                userId,
                pageReq
        );

        // 3. 리턴하라.
        return new MaqQuestionDto(query.getContent().get(0));
    }

    /**
     * Question 랜덤 출제기능 (By. Question Category)
     * 이미 유저가 맞춘 문제에 대해서는 출제하지 않으며,
     * 유저의 Score 를 기반으로 적절한 Difficulty에 맞는 문제를 탐색하고,
     * 유저가 맞추지 못한 문제중 랜덤으로 문제를 1개 출제한다.
     * @param questionCategory
     * @param userId
     */
    public SaqQuestionDto findSaqQuestionsCommon(QuestionCategory questionCategory, String userId) {
        // 1. 유저의 적정 Difficulty를 뽑아라.
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.INVALID_USERID));
        Integer userScore = user.getScore();
        List<Integer> userProperDifficulty = getUserProperDifficulty(userScore);
        PageRequest pageReq = PageRequest.of(0, 1);

        // 2. 조건에 맞추어 조회,
        Page<SAQ> query = questionRepository.findRandSaqQuestionsByDifficultyAndCategoryAndPaging(
                userProperDifficulty.get(0),
                userProperDifficulty.get(1),
                questionCategory,
                userId,
                pageReq
        );

        // 3. 리턴하라.
        return new SaqQuestionDto(query.getContent().get(0));
    }

    public GetQuestionDetailResponseDto findQuestionDetailById(String questionId, String userId) {
        // TODO : 일반유저 (1 ~ 5) : 자신이 푼 문제에 대해서만 문제의 상세정보를 조회할 수 있음.
        // TODO : 어드민 유저 (7 ~ 9) : 무엇이든 조회할 수 있음.
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.INVALID_USERID));
        GetQuestionDetailResponseDto question = questionRepository.findQuestionDetailById(questionId);

        // 어드민 -> 무조건 허용.
        if (user.getRole() >= 7) {
            return question;
        }

        // History 테이블 조회 (문제를 푼 내역이 있는지 체크)
        List<QuestionHistoryDto> histories = questionHistoryService.findHistoriesByQuestionIdAndUserId(questionId, userId);

        // 문제 푼 내역이 없다면, -> 접근 비허용
        if (histories.isEmpty()) {
            throw new CustomException(ErrorCode.NO_QUESTION_RECORDS);
        }

        // 문제 푼 내역이 있다면, -> 접근 허용
        return question;
    }

    public List<MaqQuestionDto> getLevelTestQuestions() {
        List<MAQ> questions = this.questionRepository.findMaqQuestionsLessThanDifficultyAndCount(MAX_LEVEL_TEST_DIFFICULTY, LEVEL_TEST_QUESTION_COUNT);
        return questions.stream()
                .map(MaqQuestionDto::new)
                .toList();
    }

    @Transactional
    public CheckMaqQuestionResponseDto checkCommonMaqQuestion(
            String questionId,
            String userAnswer,
            String userId
    ){
        // 유효한 유저 & 문제 체크
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.INVALID_USERID));
        MAQ dbQuestion = questionMaqRepository.findById(questionId).orElseThrow(() -> new CustomException(ErrorCode.INVALID_QUESTION));

        // 문제 정답을 맞추고..
        boolean isCorrectAnswer = dbQuestion.getAnswer().equals(userAnswer);

        // 점수 환산 후,
        int score = 0;

        if(!isCorrectAnswer) {
            score -= dbQuestion.getDifficulty();
        } else {
            score += dbQuestion.getDifficulty();
        }

        int userScore = user.accumulateUserScore(score);

        // History Table 기록
        questionHistoryRepository.save(QuestionHistory
                .builder()
                .user(user)
                .question(dbQuestion)
                .userAnswer(String.valueOf(userAnswer))
                .score(dbQuestion.getDifficulty())
                .isCorrect(isCorrectAnswer)
                .qType(dbQuestion.getCategory())
                .build()
        );


        // 리턴
        return CheckMaqQuestionResponseDto
                .builder()
                .answer(dbQuestion.getAnswer())
                .answerExplanation(dbQuestion.getAnswerExplanation())
                .userAnswer(userAnswer)
                .isCorrect(isCorrectAnswer)
                .reflectedScore(score)
                .userScore(userScore)
                .build();
    }

    @Transactional
    public CheckSaqQuestionResponseDto checkCommonSaqQuestion(
            String questionId,
            String userAnswer,
            String userId
    ){
        // 유효한 유저 & 문제 체크
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.INVALID_USERID));
        SAQ dbQuestion = questionSaqRepository.findById(questionId).orElseThrow(() -> new CustomException(ErrorCode.INVALID_QUESTION));

        // 정답체크
        int score = checkSaqScore(userAnswer, dbQuestion);

        // 점수반영
        int userScore = user.accumulateUserScore(score);

        // History Table 기록 (2개 키워드값 이상 맞출 경우 정답으로 간주함, 1개인 경우 오답)
        questionHistoryRepository.save(QuestionHistory
                .builder()
                .user(user)
                .question(dbQuestion)
                .userAnswer(userAnswer)
                .score(score)
                .isCorrect(score >= dbQuestion.getDifficulty() / 2)
                .qType(dbQuestion.getCategory())
                .build()
        );

        return CheckSaqQuestionResponseDto
                .builder()
                .keyword1(dbQuestion.getKeyword1())
                .keyword2(dbQuestion.getKeyword2())
                .keyword3(dbQuestion.getKeyword3())
                .modelAnswer(dbQuestion.getAnswer())
                .answerExplanation(dbQuestion.getAnswerExplanation())
                .userAnswer(userAnswer)
                .reflectedScore(score)
                .userScore(userScore)
                .build();
    }

    @Transactional
    public CheckMaqQuestionsResponseDto checkLevelTestQuestions(
            List<String> questions,
            List<String> userChoices,
            String userId
    ) {
        if (questions.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_ANSWERSHEET);
        }

        // 1. Question과 user Choice의 길이 비교 (동일 체크)
        if (questions.size() != userChoices.size()) {
            throw new CustomException(ErrorCode.INVALID_ANSWERSHEET);
        }

        // 2. 유효한 유저 체크
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.INVALID_USERID));

        // 3. Questions 의 문제들의 정답 내역을 조회
        List<MAQ> dbQuestions = questionMaqRepository.findMAQSByQuestionIdIn(questions);
        System.out.println(dbQuestions);

        List<QuestionHistory> questionHistories = new ArrayList<>();
        List<String> correctQuestion = new ArrayList<>();
        List<String> wrongQuestion = new ArrayList<>();

        int totalScore = 0;

        // 4. User Choice의 문제 정답 내역과 비교
        for (int i = 0; i < questions.size(); i++) {
            MAQ question = dbQuestions.get(i);
            String questionId = dbQuestions.get(i).getQuestionId();
            String dbAnswer = dbQuestions.get(i).getAnswer();
            String userAnswer = userChoices.get(i);

            // 정답
            if (Objects.equals(dbAnswer, userAnswer)) {
                correctQuestion.add(questionId);
                questionHistories.add(QuestionHistory.builder()
                        .user(user)
                        .question(question)
                        .userAnswer(String.valueOf(userAnswer))
                        .score(question.getDifficulty())
                        .isCorrect(true)
                        .qType(question.getCategory())
                        .build()
                );
                totalScore = totalScore + question.getDifficulty();
            } else {
                // 오답
                wrongQuestion.add(questionId);
                questionHistories.add(QuestionHistory.builder()
                        .user(user)
                        .question(question)
                        .userAnswer(String.valueOf(userAnswer))
                        .score(question.getDifficulty())
                        .isCorrect(false)
                        .qType(question.getCategory())
                        .build()
                );

                totalScore = totalScore - question.getDifficulty();
            }
        }

        double percentileScore = ((double) correctQuestion.size() / questions.size()) * 100;

        // 5. History 반영
        questionHistoryService.saveQuestionHistories(questionHistories);

        // 6. 유저 점수 반영
        int userCurrentScore = user.accumulateUserScore(totalScore, LEVEL_TEST_SCORE_WIEGHT);

        // 7. 결과 반환
        return CheckMaqQuestionsResponseDto.builder()
                .percentileScore(Double.parseDouble(String.format("%.2f", percentileScore)))
                .yourInitScore(userCurrentScore)
                .requestedQuestionCount(questions.size())
                .correctQuestions(correctQuestion)
                .wrongQuestions(wrongQuestion)
                .build();
    }

    @Transactional
    public void generateFakeQuestions() {
        // MAQ
        List<MAQ> maqQuestionList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            MAQ maqDBQuestion = MAQ.builder()
                    .questionTitle("Test Question Title_" + QuestionCategory.DB_MAQ.name() + "-" + i)
                    .content("Test Question Content_" + QuestionCategory.DB_MAQ.name() + "-" + i)
                    .answerExplanation("Test Question Content Explaination" + QuestionCategory.DB_MAQ.name() + "-" + i)
                    .category(QuestionCategory.DB_MAQ)
                    .difficulty(i % 100 + 1)
                    .choice1("Choice 1 for question " + i) // 자식 클래스 필드
                    .choice2("Choice 2 for question " + i) // 자식 클래스 필드
                    .choice3("Choice 3 for question " + i) // 자식 클래스 필드
                    .choice4("Choice 4 for question " + i) // 자식 클래스 필드
                    .answer(String.valueOf(i % 4 + 1)) // 자식 클래스 필드
                    .build();

            MAQ maqOsQuestion = MAQ.builder()
                    .questionTitle("Test Question Title_" + QuestionCategory.OS_MAQ.name() + "-" + i)
                    .content("Test Question Content_" + QuestionCategory.OS_MAQ.name() + "-" + i)
                    .answerExplanation("Test Question Content Explaination" + QuestionCategory.OS_MAQ.name() + "-" + i)
                    .category(QuestionCategory.OS_MAQ)
                    .difficulty(i % 100 + 1)
                    .choice1("Choice 1 for question " + i) // 자식 클래스 필드
                    .choice2("Choice 2 for question " + i) // 자식 클래스 필드
                    .choice3("Choice 3 for question " + i) // 자식 클래스 필드
                    .choice4("Choice 4 for question " + i) // 자식 클래스 필드
                    .answer(String.valueOf(i % 4 + 1)) // 자식 클래스 필드
                    .build();

            MAQ maqNetworkQuestion = MAQ.builder()
                    .questionTitle("Test Question Title_" + QuestionCategory.NETWORK_MAQ.name() + "-" + i)
                    .content("Test Question Content_" + QuestionCategory.NETWORK_MAQ.name() + "-" + i)
                    .answerExplanation("Test Question Content Explaination" + QuestionCategory.NETWORK_MAQ.name() + "-" + i)
                    .category(QuestionCategory.NETWORK_MAQ)
                    .difficulty(i % 100 + 1)
                    .choice1("Choice 1 for question " + i) // 자식 클래스 필드
                    .choice2("Choice 2 for question " + i) // 자식 클래스 필드
                    .choice3("Choice 3 for question " + i) // 자식 클래스 필드
                    .choice4("Choice 4 for question " + i) // 자식 클래스 필드
                    .answer(String.valueOf(i % 4 + 1)) // 자식 클래스 필드
                    .build();

            MAQ maqDesignQuestion = MAQ.builder()
                    .questionTitle("Test Question Title_" + QuestionCategory.DESIGN_MAQ.name() + "-" + i)
                    .content("Test Question Content_" + QuestionCategory.DESIGN_MAQ.name() + "-" + i)
                    .answerExplanation("Test Question Content Explaination" + QuestionCategory.DESIGN_MAQ.name() + "-" + i)
                    .category(QuestionCategory.DESIGN_MAQ)
                    .difficulty(i % 100 + 1)
                    .choice1("Choice 1 for question " + i) // 자식 클래스 필드
                    .choice2("Choice 2 for question " + i) // 자식 클래스 필드
                    .choice3("Choice 3 for question " + i) // 자식 클래스 필드
                    .choice4("Choice 4 for question " + i) // 자식 클래스 필드
                    .answer(String.valueOf(i % 4 + 1)) // 자식 클래스 필드
                    .build();

            MAQ maqAlgorithumQuestion = MAQ.builder()
                    .questionTitle("Test Question Title_" + QuestionCategory.ALGORITHUM_MAQ.name() + "-" + i)
                    .content("Test Question Content_" + QuestionCategory.ALGORITHUM_MAQ.name() + "-" + i)
                    .answerExplanation("Test Question Content Explaination" + QuestionCategory.ALGORITHUM_MAQ.name() + "-" + i)
                    .category(QuestionCategory.ALGORITHUM_MAQ)
                    .difficulty(i % 100 + 1)
                    .choice1("Choice 1 for question " + i) // 자식 클래스 필드
                    .choice2("Choice 2 for question " + i) // 자식 클래스 필드
                    .choice3("Choice 3 for question " + i) // 자식 클래스 필드
                    .choice4("Choice 4 for question " + i) // 자식 클래스 필드
                    .answer(String.valueOf(i % 4 + 1)) // 자식 클래스 필드
                    .build();

            maqQuestionList.add(maqDBQuestion);
            maqQuestionList.add(maqOsQuestion);
            maqQuestionList.add(maqNetworkQuestion);
            maqQuestionList.add(maqDesignQuestion);
            maqQuestionList.add(maqAlgorithumQuestion);
        }

        log.info("Fake Question Generate count : {}", maqQuestionList.size());

        questionRepository.saveAll(maqQuestionList);
    }

    /**
     * 유저 점수에 맞추어 적합한 Difficulty를 뽑아라.
     * @param userScore
     * @return
     */
    private List<Integer> getUserProperDifficulty(int userScore) {

        if(userScore == 0) {
            throw new CustomException(ErrorCode.NO_LEVEL_TEST_RECORDS);
        }

        List<Integer> difficultyResult;

        //      클래스      점수범위       난이도 산출
        //        1	    1000 ~ 2000	    1 ~ 10
        //        2	    2001 ~ 4000	    11 ~ 20
        //        3	    4001 ~ 8000	    21 ~ 30
        //        4	    8001 ~ 16000	31 ~ 40
        //        5	    16001 ~ 32000	41 ~ 50
        //        6	    32001 ~ 64000	51 ~ 60
        //        7	    64001 ~ 128000	61 ~ 70
        //        8	    128001 ~ 256000	71 ~ 80
        //        9	    256001 ~ 512000	81 ~ 100
        if(userScore <= 2000) {
            difficultyResult = Arrays.asList(1, 10);
        } else if (userScore <= 4000) {
            difficultyResult = Arrays.asList(11, 20);
        } else if (userScore <= 8000) {
            difficultyResult = Arrays.asList(21, 30);
        } else if (userScore <= 16000) {
            difficultyResult = Arrays.asList(31, 40);
        } else if (userScore <= 32000) {
            difficultyResult = Arrays.asList(41, 50);
        } else if (userScore <= 64000) {
            difficultyResult = Arrays.asList(51, 60);
        } else if (userScore <= 128000) {
            difficultyResult = Arrays.asList(61, 70);
        } else if (userScore <= 256000) {
            difficultyResult = Arrays.asList(71, 80);
        } else if (userScore <= 512000) {
            difficultyResult = Arrays.asList(81, 100);
        } else {
            difficultyResult = Arrays.asList(1, 9999999);
        }

        return difficultyResult;
    }


    private int checkSaqScore(String userAnswer, SAQ dbQuestion) {
        String keyword1 = dbQuestion.getKeyword1().toLowerCase();
        String keyword2 = dbQuestion.getKeyword2().toLowerCase();
        String keyword3 = dbQuestion.getKeyword3().toLowerCase();

        String userAnswerLower = userAnswer.toLowerCase();

        int containsCnt = 0;

        if(userAnswer.contains(keyword1)) {
            containsCnt += 1;
        }

        if(userAnswer.contains(keyword2)) {
            containsCnt += 1;
        }

        if(userAnswer.contains(keyword3)) {
            containsCnt += 1;
        }


        int score = switch (containsCnt) {
            case 0 -> -dbQuestion.getDifficulty();
            case 1 -> dbQuestion.getDifficulty() / 3;
            case 2 -> dbQuestion.getDifficulty() / 2;
            case 3 -> dbQuestion.getDifficulty();
            default -> 0;
        };

        return score;
    }

}
