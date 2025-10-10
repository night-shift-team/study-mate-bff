package com.studyMate.studyMate.global.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * API 레이턴시 측정 어노테이션
 * 컨트롤러 메서드에 이 어노테이션을 붙이면 자동으로 레이턴시가 측정됩니다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MeasureLatency {
    /**
     * 레이블 (예: "Redis 기반", "DB 기반")
     */
    String label() default "";
    
    /**
     * 설명
     */
    String description() default "";
}
