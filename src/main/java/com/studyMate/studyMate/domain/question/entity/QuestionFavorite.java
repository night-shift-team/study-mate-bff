package com.studyMate.studyMate.domain.question.entity;

import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.global.data.BaseEntityDate;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "questions_favorite")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@SuperBuilder
public class QuestionFavorite extends BaseEntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_favorite_id")
    private Long questionFavoriteId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}
