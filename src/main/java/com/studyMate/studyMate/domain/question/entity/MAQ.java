package com.studyMate.studyMate.domain.question.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "maq")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MAQ {
    @Id
    @Column(name = "question_id")
    private Long questionId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "choice_1", nullable = false)
    private String choice1;

    @Column(name = "choice_2", nullable = false)
    private String choice2;

    @Column(name = "choice_3", nullable = false)
    private String choice3;

    @Column(name = "choice_4", nullable = false)
    private String choice4;

    @Column(name = "answer", nullable = false)
    private Integer answer;
}
