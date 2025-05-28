package com.studyMate.studyMate.global.redis;

/**
 * 레디스 키 관리 생성 Factory
 */
public class RedisKeyFactory {
    /**
     * 게시글 조회 Redis Key
     * @param boardId 조회한 Board ID
     * @param userId 조회한 user ID
     * @return
     */
    public static String viewedBoard(Long boardId, Long userId) {
        return String.format("viewed:board:%d:user:%s", boardId, userId);
    }
}
