package com.studyMate.studyMate.domain.question.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "maq")
@DiscriminatorValue("MAQ")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class MAQ extends Question {
    @Id
    @Column(name = "question_id")
    private String questionId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "question_id")
    @JsonIgnore
    private Question question;

    @Column(name = "choice_1", nullable = false)
    private String choice1;

    @Column(name = "choice_2", nullable = false)
    private String choice2;

    @Column(name = "choice_3", nullable = false)
    private String choice3;

    @Column(name = "choice_4", nullable = false)
    private String choice4;

    @Override
    public String toString() {
        return "MAQ{" +
                "questionId=" + questionId +
                ", questionTitle='" + getQuestionTitle() + '\'' +
                ", questionContent'" + getContent() + '\'' +
                ", answer'" + getAnswer() + '\'' +
                ", answerExplanation'" + getAnswerExplanation() + '\'' +
                ", difficulty=" + getDifficulty() +
                ", category=" + getCategory() +
                ", choice1='" + choice1 + '\'' +
                ", choice2='" + choice2 + '\'' +
                ", choice3='" + choice3 + '\'' +
                ", choice4='" + choice4 + '\'' +
                '}';
    }
}
