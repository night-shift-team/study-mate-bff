package com.studyMate.studyMate.global.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class LogUtil {

    private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);

    /**
     * 로그 메세지 info level 출력
     * @param method 사용 메소드명
     * @param userId 유저 id
     * @param message 로그 메세지
     */
    public static void infoLog(String method, String userId, String message) {
        setMDC(method, userId);
        logger.info(formatMessage(method, userId, message));
        MDC.clear();
    }

    /**
     * 로그 메세지 warn level 출력
     * @param method 사용 메소드명
     * @param userId 유저 id
     * @param message 로그 메세지
     */
    public static void warnLog(String method, String userId, String message) {
        setMDC(method, userId);
        logger.warn(formatMessage(method, userId, message));
        MDC.clear();
    }

    /**
     * 로그 메세지 Error level 출력
     * @param method 사용 메소드명
     * @param userId 유저 id
     * @param message 로그 메세지
     */
    public static void errorLog(String method, String userId, String message) {
        setMDC(method, userId);
        logger.error(formatMessage(method, userId, message));
        MDC.clear();
    }

    private static void setMDC(String methodName, String user) {
        MDC.put("methodName", methodName);
        MDC.put("user", user);
    }

    private static String formatMessage(String methodName, String user, String message) {
        return methodName + "(" + user + ") - " + message;
    }
}
