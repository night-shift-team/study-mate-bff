package com.studyMate.studyMate.domain.question.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studyMate.studyMate.domain.question.dto.GetQuestionDetailResponseDto;
import com.studyMate.studyMate.domain.question.entity.MAQ;
import com.studyMate.studyMate.domain.question.entity.SAQ;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.studyMate.studyMate.domain.question.entity.QMAQ.mAQ;
import static com.studyMate.studyMate.domain.question.entity.QQuestion.*;
import static com.studyMate.studyMate.domain.question.entity.QSAQ.*;

@Repository
public class QuestionRepositoryImpl implements QuestionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public QuestionRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<MAQ> findMaqQuestionsLessThanDifficultyAndCount(int difficulty, int count) {
        return queryFactory
                .selectFrom(mAQ)
                .where(mAQ.difficulty.loe(difficulty))
                .orderBy(Expressions.numberTemplate(Double.class, "function('RAND')").asc())
                .limit(count)
                .fetch();
    }

    @Override
    public List<MAQ> findMaqQuestions() {
        return queryFactory
                .selectFrom(mAQ)
                .from(mAQ)
                .fetch();
    }

    @Override
    public GetQuestionDetailResponseDto findQuestionDetailById(Long questionId) {
        // Question Super Type MAQ SubType | SAQ SubType 으로 구성되어있다.
        // Question 의 qType에 따라서 answer, options 부분을 가변적으로 바꾸어 가져올 것.
        // getQUestionDetailResponseDto 값에 맞추어서 반환.
        return queryFactory
                .select(Projections.constructor(GetQuestionDetailResponseDto.class,
                        question.questionId,
                        question.description,
                        question.comment,
                        question.difficulty,
                        /**
                         * options =
                         * maq = choice 1 ~ 4
                         * saq = keyword 1 ~ 4
                         */
                        new CaseBuilder()
                                .when(question.instanceOf(MAQ.class)).then(
                                        Expressions.stringTemplate("JSON_ARRAY({0}, {1}, {2}, {3})",
                                                mAQ.choice1, mAQ.choice2, mAQ.choice3, mAQ.choice4
                                        )
                                )
                                .when(question.instanceOf(SAQ.class)).then(
                                        Expressions.stringTemplate("JSON_ARRAY({0}, {1}, {2})",
                                                sAQ.keyword1, sAQ.keyword2, sAQ.keyword3
                                        )
                                )
                                .otherwise("null")
                                .as("options"),

                        question.category,

                        // answer = 상황에 따라 다름. maq | saq
                        new CaseBuilder()
                                .when(question.instanceOf(MAQ.class)).then(mAQ.answer.stringValue())
                                .when(question.instanceOf(SAQ.class)).then(sAQ.answer)
                                .otherwise("null")
                                .as("answer")
                ))
                .from(question)
                .leftJoin(mAQ).on(question.questionId.eq(mAQ.questionId)).fetchJoin()
                .leftJoin(sAQ).on(question.questionId.eq(sAQ.questionId)).fetchJoin()
                .where(question.questionId.eq(questionId))
                .fetchOne();
    }
}
