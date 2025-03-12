package com.studyMate.studyMate.domain.question.service;

import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import com.studyMate.studyMate.domain.question.dto.CheckMaqQuestionResponseDto;
import com.studyMate.studyMate.domain.question.dto.GetQuestionDetailResponseDto;
import com.studyMate.studyMate.domain.question.dto.MaqQuestionDto;
import com.studyMate.studyMate.domain.question.entity.MAQ;
import com.studyMate.studyMate.domain.history.entity.QuestionHistory;
import com.studyMate.studyMate.domain.history.repository.QuestionHistoryRepository;
import com.studyMate.studyMate.domain.question.repository.QuestionMaqRepository;
import com.studyMate.studyMate.domain.question.repository.QuestionRepository;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.domain.user.repository.UserRepository;
import com.studyMate.studyMate.global.error.CustomException;
import com.studyMate.studyMate.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMaqRepository questionMaqRepository;
    private final QuestionHistoryRepository questionHistoryRepository;
    private final UserRepository userRepository;
    private final int MAX_LEVEL_TEST_DIFFICULTY = 20;
    private final int LEVEL_TEST_QUESTION_COUNT = 20;


    public List<MAQ> findMaqAll(){
        return questionRepository.findMaqQuestions();
    }

    public GetQuestionDetailResponseDto findQuestionDetailById(String questionId, String userId) {
        // TODO : 일반유저 (1 ~ 5) : 자신이 푼 문제에 대해서만 문제의 상세정보를 조회할 수 있음.
        // TODO : 어드민 유저 (7 ~ 9) : 무엇이든 조회할 수 있음.
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.INVALID_USERID));
        GetQuestionDetailResponseDto question = questionRepository.findQuestionDetailById(questionId);

        // 어드민 -> 무조건 허용.
        if(user.getRole() >= 7) {
            return question;
        }

        // History 테이블 조회 (문제를 푼 내역이 있는지 체크)
        List<QuestionHistory> histories = questionHistoryRepository.findQuestionHistoriesByUser_UserIdAndQuestion_QuestionId(user.getUserId(), question.questionId());

        // 문제 푼 내역이 없다면, -> 접근 비허용
        if(histories.isEmpty()) {
            throw new CustomException(ErrorCode.NO_QUESTION_RECORDS);
        }

        // 문제 푼 내역이 있다면, -> 접근 허용
        return question;
    }

    public List<MaqQuestionDto> getLevelTestQuestions() {
        List<MAQ> questions = this.questionRepository.findMaqQuestionsLessThanDifficultyAndCount(MAX_LEVEL_TEST_DIFFICULTY, LEVEL_TEST_QUESTION_COUNT);
//        System.out.println("Check Question First question Id : " + questions.get(0).getQuestionId());
        return questions.stream()
                .map(MaqQuestionDto::new)
                .toList();
    }

    @Transactional
    public CheckMaqQuestionResponseDto checkLevelTestQuestions(
            List<String> questions,
            List<String> userChoices,
            String userId
    ) {
        if(questions.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_ANSWERSHEET);
        }

        // 1. Question과 user Choice의 길이 비교 (동일 체크)
        if(questions.size() != userChoices.size()){
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
        for(int i=0; i < questions.size(); i++){
            MAQ question = dbQuestions.get(i);
            String questionId = dbQuestions.get(i).getQuestionId();
            String dbAnswer = dbQuestions.get(i).getAnswer();
            String userAnswer = userChoices.get(i);

            // 정답
            if(Objects.equals(dbAnswer, userAnswer)){
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
        questionHistoryRepository.saveAll(questionHistories);
        // 6. 유저 점수 반영
        user.accumulateUserScore(totalScore);

        // 7. 결과 반환
        return CheckMaqQuestionResponseDto.builder()
                .percentileScore(Double.parseDouble(String.format("%.2f", percentileScore)))
                .yourInitSore(user.getScore())
                .requestedQuestionCount(questions.size())
                .correctQuestions(correctQuestion)
                .wrongQuestions(wrongQuestion)
                .build();
    }

    @Transactional
    public void generateFakeQuestions() {
        // MAQ
        List<MAQ> maqQuestionList = new ArrayList<>();
        for(int i=1; i <= 100; i++) {
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

}
