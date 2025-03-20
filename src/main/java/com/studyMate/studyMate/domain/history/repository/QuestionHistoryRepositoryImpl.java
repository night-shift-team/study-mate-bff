package com.studyMate.studyMate.domain.history.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studyMate.studyMate.domain.history.dto.SolveStatsDto;
import com.studyMate.studyMate.domain.history.dto.SolveStatsResponseDto;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.studyMate.studyMate.domain.history.entity.QQuestionHistory.questionHistory;

@Repository
public class QuestionHistoryRepositoryImpl implements QuestionHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public QuestionHistoryRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public SolveStatsResponseDto getQuestionSolveStatsInYear(String userId) {
        LocalDateTime now = LocalDateTime.now(); // End Date
        LocalDateTime yearBefore = now.minusYears(1); // Start Date

        List<SolveStatsDto> query = queryFactory.
                select(Projections.fields(
                        SolveStatsDto.class,
                        Expressions.dateTemplate(
                                String.class,
                                "DATE_FORMAT({0}, {1})",
                                questionHistory.createdDt,
                                "%Y-%m-%d"
                        ).as("solveDay"),
                        questionHistory.count().as("solveCount")
                        ))
                .from(questionHistory)
                .where(
                        questionHistory.user.userId.eq(userId).and(
                                questionHistory.createdDt.goe(yearBefore) // 1년 내 기록만
                        )
                )
                .groupBy(Expressions.dateTemplate(
                        LocalDate.class,
                        "DATE_FORMAT({0}, {1})",
                        questionHistory.createdDt,
                        "%Y-%m-%d"
                ))
                .orderBy(questionHistory.createdDt.asc())
                .fetch();

        return SolveStatsResponseDto
                .builder()
                .solveStats(query)
                .StartDate(yearBefore.toLocalDate())
                .EndDate(now.toLocalDate())
                .build();
    }
}
