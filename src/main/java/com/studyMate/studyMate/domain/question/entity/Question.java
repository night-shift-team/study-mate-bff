package com.studyMate.studyMate.domain.question.entity;

import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import com.studyMate.studyMate.global.data.BaseEntityDate;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "questions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "q_type")
@SuperBuilder
public class Question extends BaseEntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "question_id")
    private String questionId;

    @Column(name = "question_title", nullable = false, columnDefinition = "LONGTEXT")
    private String questionTitle;

    @Column(name = "question_content", nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "question_answer", nullable = false, columnDefinition = "LONGTEXT")
    private String answer;

    @Column(name = "answer_explanation", nullable = false, columnDefinition = "LONGTEXT")
    private String answerExplanation;

    @Column(name = "difficulty", nullable = false)
    private Integer difficulty;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private QuestionCategory category;

}