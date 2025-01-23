package com.studyMate.studyMate.domain.question.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "uaq")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UAQ {
    @Id
    @Column(name = "question_id")
    private Long questionId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "answer", nullable = false)
    private Integer answer;
}
