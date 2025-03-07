package com.studyMate.studyMate.domain.question.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studyMate.studyMate.domain.question.entity.MAQ;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.studyMate.studyMate.domain.question.entity.QMAQ.*;

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
}
