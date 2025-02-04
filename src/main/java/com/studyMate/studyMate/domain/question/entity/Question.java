package com.studyMate.studyMate.domain.question.entity;

import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "questions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    @Id
    @Tsid
    @Column(name = "question_id")
    private Long questionId;

    @Lob
    @Column(name = "question", nullable = false)
    private String question;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private QuestionCategory category;

    @Lob
    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "difficulty", nullable = false)
    private Integer difficulty;
}