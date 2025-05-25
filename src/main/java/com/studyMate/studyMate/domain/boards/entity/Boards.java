package com.studyMate.studyMate.domain.boards.entity;

import com.studyMate.studyMate.domain.boards.data.BoardCategory;
import com.studyMate.studyMate.domain.boards.data.BoardStatus;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.global.data.BaseEntityDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "boards")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Boards extends BaseEntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private BoardCategory category;

    @Column
    private BoardStatus status;

    @Column
    private Integer view;
}
