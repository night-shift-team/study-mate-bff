package com.studyMate.studyMate.domain.boards.service;

import com.studyMate.studyMate.domain.boards.entity.Boards;
import com.studyMate.studyMate.domain.boards.entity.Comments;
import com.studyMate.studyMate.domain.boards.repository.BoardRepository;
import com.studyMate.studyMate.domain.boards.repository.CommentRepository;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.domain.user.repository.UserRepository;
import com.studyMate.studyMate.global.error.CustomException;
import com.studyMate.studyMate.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long saveComment(Long boardId, String comment, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USERID));

        Boards targetBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_BOARD_ID));

        Comments newComment = commentRepository.save(Comments.builder()
                .board(targetBoard)
                .content(comment)
                .user(user)
                .build()
        );

        return newComment.getId();
    }
}
