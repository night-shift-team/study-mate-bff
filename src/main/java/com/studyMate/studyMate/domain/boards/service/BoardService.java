package com.studyMate.studyMate.domain.boards.service;

import com.studyMate.studyMate.domain.boards.data.BoardCategory;
import com.studyMate.studyMate.domain.boards.data.BoardStatus;
import com.studyMate.studyMate.domain.boards.entity.Boards;
import com.studyMate.studyMate.domain.boards.repository.BoardRepository;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.domain.user.repository.UserRepository;
import com.studyMate.studyMate.global.error.CustomException;
import com.studyMate.studyMate.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    ////////////////////////////////////////////////// BOARD //////////////////////////////////////////////////
    public Long createFreeBoard (
            String title,
            String content,
            String writerid
    ) {
        return this.saveBoard(title, content, BoardCategory.FREE, writerid);
    }

    public Long createQNABoard (
            String title,
            String content,
            String writerId
    ) {
        return this.saveBoard(title, content, BoardCategory.QNA, writerId);
    }

    private Long saveBoard(
            String title,
            String content,
            BoardCategory category,
            String writerId
    ) {
        User writer = this.userRepository.findById(writerId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USERID));

        Boards newBoard = boardRepository.save(
                Boards.builder()
                        .title(title)
                        .content(content)
                        .category(category)
                        .user(writer)
                        .build()
        );

        return newBoard.getId();
    }
}
