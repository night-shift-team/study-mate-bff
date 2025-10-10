package com.studyMate.studyMate.global.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LatencyUtil {

    /**
     * 메서드 실행 시간 측정 및 로깅
     * @param methodName 메서드명
     * @param task 실행할 작업
     * @param <T> 리턴 타입
     * @return 작업 실행 결과
     */
    public static <T> T measureLatency(String methodName, LatencyTask<T> task) {
        long startTime = System.currentTimeMillis();
        
        try {
            T result = task.execute();
            long endTime = System.currentTimeMillis();
            long latency = endTime - startTime;
            
            log.info("[LATENCY] {} - {}ms", methodName, latency);
            
            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long latency = endTime - startTime;
            
            log.error("[LATENCY] {} - {}ms (FAILED: {})", methodName, latency, e.getMessage());
            throw e;
        }
    }

    /**
     * 메서드 실행 시간 측정 및 로깅 (상세 정보 포함)
     * @param methodName 메서드명
     * @param description 설명
     * @param task 실행할 작업
     * @param <T> 리턴 타입
     * @return 작업 실행 결과
     */
    public static <T> T measureLatencyWithDetail(String methodName, String description, LatencyTask<T> task) {
        long startTime = System.currentTimeMillis();
        
        try {
            T result = task.execute();
            long endTime = System.currentTimeMillis();
            long latency = endTime - startTime;
            
            if (latency > 1000) {
                log.warn("[LATENCY] {} ({}) - {}ms ⚠️ SLOW", methodName, description, latency);
            } else if (latency > 500) {
                log.info("[LATENCY] {} ({}) - {}ms", methodName, description, latency);
            } else {
                log.debug("[LATENCY] {} ({}) - {}ms", methodName, description, latency);
            }
            
            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long latency = endTime - startTime;
            
            log.error("[LATENCY] {} ({}) - {}ms (FAILED: {})", methodName, description, latency, e.getMessage());
            throw e;
        }
    }

    /**
     * 비교 측정 (구/신 API 비교용)
     * @param label 레이블 (예: "DB 기반", "Redis 기반")
     * @param task 실행할 작업
     * @param <T> 리턴 타입
     * @return 작업 실행 결과
     */
    public static <T> T measureLatencyWithLabel(String label, LatencyTask<T> task) {
        long startTime = System.currentTimeMillis();
        
        try {
            T result = task.execute();
            long endTime = System.currentTimeMillis();
            long latency = endTime - startTime;
            
            log.info("[LATENCY_COMPARE] [{}] - {}ms", label, latency);
            
            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long latency = endTime - startTime;
            
            log.error("[LATENCY_COMPARE] [{}] - {}ms (FAILED: {})", label, latency, e.getMessage());
            throw e;
        }
    }

    @FunctionalInterface
    public interface LatencyTask<T> {
        T execute();
    }
}
