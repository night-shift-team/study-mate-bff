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

    public static String viewedBoard(Long boardId, String userId) {
        return String.format("viewed:board:%d:user:%s", boardId, userId);
    }

    /**
     * 유저 로컬 회원가입 이메일 인증 Redis Key
     */
    public static String signUpLocalUser(String userId) {
        return String.format("signup:%s", userId);
    }

    /**
     * 로컬 회원가입 이메일 인증 Verified Redis Key
     */
    public static String singupVerifiedLocalUser(String userId) {
        return String.format("singup-verified:%s", userId);
    }

    /**
     * 유저 Refresh Token Redis Key
     */
    public static String refreshTokenUser(String userId) {
        return String.format("rf-token:%s", userId);
    }

    /**
     * 유저 비밀번호 찾기 인증 메일 Redis Key
     */
    public static String findPwdUser(String userId) {
        return String.format("find-pwd:%s", userId);
    }
}
