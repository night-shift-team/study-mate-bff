package com.studyMate.studyMate.domain.question.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "saq")
@DiscriminatorValue("SAQ")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class SAQ extends Question {
    @Id
    @Column(name = "question_id")
    private String questionId;

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

    @Override
    public String toString() {
        return "SAQ{" +
                "questionId=" + questionId +
                ", questionTitle='" + getQuestionTitle() + '\'' +
                ", questionContent'" + getContent() + '\'' +
                ", answer'" + getAnswer() + '\'' +
                ", answerExplanation'" + getAnswerExplanation() + '\'' +
                ", difficulty=" + getDifficulty() +
                ", category=" + getCategory() +
                ", keyword1='" + keyword1 + '\'' +
                ", keyword2='" + keyword2 + '\'' +
                ", keyword3='" + keyword3 + '\'' +
                '}';
    }
}
