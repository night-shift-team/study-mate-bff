package com.studyMate.studyMate.domain.boards.entity;

import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.global.data.BaseEntityDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Comments extends BaseEntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Boards board;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;
}
