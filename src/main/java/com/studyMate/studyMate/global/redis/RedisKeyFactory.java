package com.studyMate.studyMate.global.redis;

// 레디스 키 관리 생성 Factory
public class RedisKeyFactory {
    // 게시글 조회 Redis Key
    public static String viewedBoard(Long boardId, Long userId) {
        return String.format("viewed:board:%d:user:%s", boardId, userId);
    }

    public static String viewedBoard(Long boardId, String userId) {
        return String.format("viewed:board:%d:user:%s", boardId, userId);
    }

    // 유저 로컬 회원가입 이메일 인증 Redis Key
    public static String signUpLocalUser(String userId) {
        return String.format("signup:%s", userId);
    }

    // 로컬 회원가입 이메일 인증 Verified Redis Key
    public static String singupVerifiedLocalUser(String userId) {
        return String.format("singup-verified:%s", userId);
    }

    // 유저 Refresh Token Redis Key
    public static String refreshTokenUser(String userId) {
        return String.format("rf-token:%s", userId);
    }

    // 유저 비밀번호 찾기 인증 메일 Redis Key
    public static String findPwdUser(String userId) {
        return String.format("find-pwd:%s", userId);
    }

    // 비밀번호 초기화 후, 비밀번호 변경 필요한 유저인지 체크하는 Key
    public static String changePasswordRequiredUser(String userId) {
        return String.format("chg-pwd-required:%s", userId);
    }

    // 유저 랭킹 Sorted Set Redis Key
    public static String userRankingSortedSet() {
        return "ranking:user:score";
    }
}
