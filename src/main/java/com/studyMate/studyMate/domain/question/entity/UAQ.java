package com.studyMate.studyMate.domain.question.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "uaq")
@DiscriminatorValue("UAQ")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class UAQ extends Question {
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
