package com.studyMate.studyMate.domain.contest.entity;

import com.studyMate.studyMate.domain.question.entity.Question;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "season_contest_questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeasonContestQuestion {

    @EmbeddedId
    private SeasonContestQuestionId id;

    @ManyToOne
    @MapsId("seasonId")
    @JoinColumn(name = "season_id")
    private Season season;

    @ManyToOne
    @MapsId("questionId")
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "seq", nullable = false)
    private Integer seq;
}

