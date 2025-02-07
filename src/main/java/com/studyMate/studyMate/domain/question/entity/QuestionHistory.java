package com.studyMate.studyMate.domain.question.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.studyMate.studyMate.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "question_history")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionHistory {
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

    @Column(name = "selected_answer", nullable = false)
    private Integer selectedAnswer;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;

    @Column(name = "score_result", nullable = false)
    private Integer scoreResult;

    @CreatedDate
    @Column(name = "created_dt")
    @Schema(description = "생성일", example = "2024-09-28T16:23:00.00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDt;
}