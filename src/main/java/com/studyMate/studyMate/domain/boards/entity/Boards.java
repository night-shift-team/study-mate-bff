package com.studyMate.studyMate.domain.boards.entity;

import com.studyMate.studyMate.domain.boards.data.BoardCategory;
import com.studyMate.studyMate.domain.boards.data.BoardStatus;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.global.data.BaseEntityDate;
import com.studyMate.studyMate.global.error.CustomException;
import com.studyMate.studyMate.global.error.ErrorCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

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

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable = false)
    private BoardCategory category;

    @Enumerated(EnumType.STRING)
    private BoardStatus status = BoardStatus.RECEIVED;

    @Column(nullable = false)
    private Integer view;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comments> comments = new ArrayList<>();

    @PrePersist
    @PreUpdate
    private void defaultValue() {
        if(view == null) view = 0;
        if(status == null) status = BoardStatus.RECEIVED;
    }

    public Integer updateView() {
        this.view += 1;
        return this.view;
    }
}
