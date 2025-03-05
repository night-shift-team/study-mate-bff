package com.studyMate.studyMate.domain.question.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "saq")
@DiscriminatorValue("MAQ")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class SAQ extends Question {
    @Id
    @Column(name = "question_id")
    private Long questionId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "keyword_1", nullable = false)
    private String keyword1;

    @Column(name = "keyword_2", nullable = false)
    private String keyword2;

    @Column(name = "keyword_3", nullable = false)
    private String keyword3;

    @Column(name = "answer", nullable = false)
    private Integer answer;
}
