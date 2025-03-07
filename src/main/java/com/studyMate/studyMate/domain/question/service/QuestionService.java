package com.studyMate.studyMate.domain.question.service;

import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import com.studyMate.studyMate.domain.question.dto.MaqQuestionDto;
import com.studyMate.studyMate.domain.question.entity.MAQ;
import com.studyMate.studyMate.domain.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final int MAX_LEVEL_TEST_DIFFICULTY = 20;


    public List<MAQ> findMaqAll(){
        return questionRepository.findMaqQuestions();
    }

    public List<MaqQuestionDto> getLevelTestQuestions() {
        List<MAQ> questions = this.questionRepository.findMaqQuestionsLessThanDifficultyAndCount(MAX_LEVEL_TEST_DIFFICULTY, 20);

        return questions.stream()
                .map(MaqQuestionDto::new)
                .toList();
    }

    @Transactional
    public boolean generateFakeQuestions() {
        // MAQ
        List<MAQ> maqQuestionList = new ArrayList<>();
        for(int i=1; i <= 100; i++) {
            MAQ maqDBQuestion = MAQ.builder()
                    .description("Test Question_" + QuestionCategory.DB_MAQ.name() + "-" + i)
                    .category(QuestionCategory.DB_MAQ)
                    .comment("Test Question Comment" + QuestionCategory.DB_MAQ.name() + "-" + i)
                    .difficulty(i % 100 + 1)
                    .choice1("Choice 1 for question " + i) // 자식 클래스 필드
                    .choice2("Choice 2 for question " + i) // 자식 클래스 필드
                    .choice3("Choice 3 for question " + i) // 자식 클래스 필드
                    .choice4("Choice 4 for question " + i) // 자식 클래스 필드
                    .answer(i % 4 + 1) // 자식 클래스 필드
                    .build();

            MAQ maqOsQuestion = MAQ.builder()
                    .description("Test Question_" + QuestionCategory.OS_MAQ.name() + "-" + i)
                    .category(QuestionCategory.OS_MAQ)
                    .comment("Test Question Comment" + QuestionCategory.OS_MAQ.name() + "-" + i)
                    .difficulty(i % 100 + 1)
                    .choice1("Choice 1 for question " + i) // 자식 클래스 필드
                    .choice2("Choice 2 for question " + i) // 자식 클래스 필드
                    .choice3("Choice 3 for question " + i) // 자식 클래스 필드
                    .choice4("Choice 4 for question " + i) // 자식 클래스 필드
                    .answer(i % 4 + 1) // 자식 클래스 필드
                    .build();

            MAQ maqNetworkQuestion = MAQ.builder()
                    .description("Test Question_" + QuestionCategory.NETWORK_MAQ.name() + "-" + i)
                    .category(QuestionCategory.NETWORK_MAQ)
                    .comment("Test Question Comment" + QuestionCategory.NETWORK_MAQ.name() + "-" + i)
                    .difficulty(i % 100 + 1)
                    .choice1("Choice 1 for question " + i) // 자식 클래스 필드
                    .choice2("Choice 2 for question " + i) // 자식 클래스 필드
                    .choice3("Choice 3 for question " + i) // 자식 클래스 필드
                    .choice4("Choice 4 for question " + i) // 자식 클래스 필드
                    .answer(i % 4 + 1) // 자식 클래스 필드
                    .build();

            MAQ maqDesignQuestion = MAQ.builder()
                    .description("Test Question_" + QuestionCategory.DESIGN_MAQ.name() + "-" + i)
                    .category(QuestionCategory.DESIGN_MAQ)
                    .comment("Test Question Comment" + QuestionCategory.DESIGN_MAQ.name() + "-" + i)
                    .difficulty(i % 100 + 1)
                    .choice1("Choice 1 for question " + i) // 자식 클래스 필드
                    .choice2("Choice 2 for question " + i) // 자식 클래스 필드
                    .choice3("Choice 3 for question " + i) // 자식 클래스 필드
                    .choice4("Choice 4 for question " + i) // 자식 클래스 필드
                    .answer(i % 4 + 1) // 자식 클래스 필드
                    .build();

            MAQ maqAlgorithumQuestion = MAQ.builder()
                    .description("Test Question_" + QuestionCategory.ALGORITHUM_MAQ.name() + "-" + i)
                    .category(QuestionCategory.ALGORITHUM_MAQ)
                    .comment("Test Question Comment" + QuestionCategory.ALGORITHUM_MAQ.name() + "-" + i)
                    .difficulty(i % 100 + 1)
                    .choice1("Choice 1 for question " + i) // 자식 클래스 필드
                    .choice2("Choice 2 for question " + i) // 자식 클래스 필드
                    .choice3("Choice 3 for question " + i) // 자식 클래스 필드
                    .choice4("Choice 4 for question " + i) // 자식 클래스 필드
                    .answer(i % 4 + 1) // 자식 클래스 필드
                    .build();

            maqQuestionList.add(maqDBQuestion);
            maqQuestionList.add(maqOsQuestion);
            maqQuestionList.add(maqNetworkQuestion);
            maqQuestionList.add(maqDesignQuestion);
            maqQuestionList.add(maqAlgorithumQuestion);
        }

        log.info("Fake Question Generate count : {}", maqQuestionList.size());

        questionRepository.saveAll(maqQuestionList);

        return true;
    }

}
