package com.studyMate.studyMate.domain.contest.entity;

import com.studyMate.studyMate.domain.question.entity.Question;
import com.studyMate.studyMate.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "contest_history")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContestHistory {

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
    private LocalDateTime createdDt;
}
