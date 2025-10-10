package com.studyMate.studyMate.global.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * API 레이턴시 측정 어노테이션
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MeasureLatency {
    /**
     * 레이블
     */
    String label() default "";
    
    /**
     * 설명
     */
    String description() default "";
}
