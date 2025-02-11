package com.studyMate.studyMate.domain.contest.entity;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeasonContestQuestionId implements Serializable {

    private Long seasonId;
    private Long questionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeasonContestQuestionId that = (SeasonContestQuestionId) o;
        return Objects.equals(seasonId, that.seasonId) &&
                Objects.equals(questionId, that.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seasonId, questionId);
    }
}
