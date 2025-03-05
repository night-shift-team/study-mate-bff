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
    @Tsid
    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "description", nullable = false, columnDefinition = "LONGTEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private QuestionCategory category;

    @Column(name = "comment", nullable = false, columnDefinition = "LONGTEXT")
    private String comment;

    @Column(name = "difficulty", nullable = false)
    private Integer difficulty;
}