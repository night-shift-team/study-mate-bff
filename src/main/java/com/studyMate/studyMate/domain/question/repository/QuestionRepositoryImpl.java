package com.studyMate.studyMate.domain.question.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studyMate.studyMate.domain.question.entity.MAQ;
import com.studyMate.studyMate.domain.question.entity.QQuestion;
import com.studyMate.studyMate.domain.question.entity.Question;
import com.studyMate.studyMate.domain.question.entity.UAQ;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.studyMate.studyMate.domain.question.entity.QQuestion.*;
import static com.studyMate.studyMate.domain.question.entity.QMAQ.*;
import static com.studyMate.studyMate.domain.question.entity.QUAQ.*;


@Repository
public class QuestionRepositoryImpl implements QuestionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public QuestionRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<UAQ> findUaqQuestions() {
        return queryFactory
                .selectFrom(uAQ)
                .from(uAQ)
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
