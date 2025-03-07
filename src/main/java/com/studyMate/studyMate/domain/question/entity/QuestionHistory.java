package com.studyMate.studyMate.domain.question.entity;

import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import com.studyMate.studyMate.domain.user.data.LoginType;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.global.data.BaseEntityDate;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "question_history")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class QuestionHistory extends BaseEntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "user_answer", nullable = false)
    private String userAnswer;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;

    @Enumerated(EnumType.STRING)
    @Column(name = "qType", nullable = false)
    private QuestionCategory qType;
}