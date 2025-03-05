package com.studyMate.studyMate.domain.question.entity;

import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import com.studyMate.studyMate.global.data.BaseEntityDate;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "questions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "q_type")
@Builder
public class Question extends BaseEntityDate {
    @Id
    @Tsid
    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "question", nullable = false, columnDefinition = "LONGTEXT")
    private String question;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private QuestionCategory category;

    @Column(name = "comment", nullable = false, columnDefinition = "LONGTEXT")
    private String comment;

    @Column(name = "difficulty", nullable = false)
    private Integer difficulty;
}