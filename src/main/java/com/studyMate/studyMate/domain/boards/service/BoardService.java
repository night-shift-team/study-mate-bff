package com.studyMate.studyMate.domain.boards.service;

import com.studyMate.studyMate.domain.boards.data.BoardCategory;
import com.studyMate.studyMate.domain.boards.dto.BoardDto;
import com.studyMate.studyMate.domain.boards.dto.PagingResponseDto;
import com.studyMate.studyMate.domain.boards.entity.Boards;
import com.studyMate.studyMate.domain.boards.repository.BoardRepository;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.domain.user.repository.UserRepository;
import com.studyMate.studyMate.global.error.CustomException;
import com.studyMate.studyMate.global.error.ErrorCode;
import com.studyMate.studyMate.global.redis.RedisKeyFactory;
import com.studyMate.studyMate.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final RedisService redisService;
    private final int BOARD_VIEW_TTL_HOURS = 24; // 게시물 조회 수 중복누적 방지 유효시간

    @Transactional
    public BoardDto getQnaBoardById(Long boardId, String userId){
        Boards board = boardRepository.findByIdAndCategory(boardId, BoardCategory.QNA)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_BOARD_ID));

        String key = RedisKeyFactory.viewedBoard(boardId, userId);
        // 조회 수 24시간 내, 중복 증감 방지
        boolean isAlreadyView = redisService.hasKey(key);
        if(!isAlreadyView){
            redisService.setValue(key, "1", Duration.ofHours(BOARD_VIEW_TTL_HOURS));
            board.updateView();
        }

        return new BoardDto(board);
    }

    public PagingResponseDto<BoardDto> getQNABoardsPaging(Integer page, Integer limit) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdDt"));

        Page<Boards> query = boardRepository.findAllByCategory(BoardCategory.QNA, pageRequest);
        Page<BoardDto> boardDtoPage = query.map(BoardDto::new);

        return new PagingResponseDto<>(boardDtoPage);
    }

    ////////////////////////////////////////////////// BOARD //////////////////////////////////////////////////
    @Transactional
    public Long createFreeBoard (
            String title,
            String content,
            String writerid
    ) {
        return this.saveBoard(title, content, BoardCategory.FREE, writerid);
    }

    @Transactional
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

    @Transactional
    public boolean deleteBoard(Long boardId, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USERID));

        Boards board = boardRepository.findByIdAndCategory(boardId, BoardCategory.QNA)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_BOARD_ID));


        if(
                user.getRole() < 7 && // User Role이 어드민이 아님.
                board.getUser().getUserId() != user.getUserId() // User가 Writer가 아님.
        ){
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        // TODO (HD) : Slack 공식 채널 추가 후, 알림 추가.
        board.deleteBoard();

        return true;
    }
}
