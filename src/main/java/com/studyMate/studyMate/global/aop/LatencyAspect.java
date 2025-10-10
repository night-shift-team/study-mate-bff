package com.studyMate.studyMate.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class LatencyAspect {

    @Around("@annotation(com.studyMate.studyMate.global.aop.MeasureLatency)")
    public Object measureLatency(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        MeasureLatency annotation = method.getAnnotation(MeasureLatency.class);
        
        String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();
        String label = annotation.label().isEmpty() ? methodName : annotation.label();
        String description = annotation.description();
        
        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            long latency = endTime - startTime;
            
            logLatency(label, description, latency, true);
            
            return result;
        } catch (Throwable e) {
            long endTime = System.currentTimeMillis();
            long latency = endTime - startTime;
            
            logLatency(label, description, latency, false);
            throw e;
        }
    }
    
    private void logLatency(String label, String description, long latency, boolean success) {
        String descPart = description.isEmpty() ? "" : " (" + description + ")";
        String statusPart = success ? "" : " (FAILED)";
        
        if (latency > 1000) {
            log.warn("[API_LATENCY] [{}]{} - {}ms {} (⚠SLOW)", label, descPart, latency, statusPart);
        } else if (latency > 500) {
            log.info("[API_LATENCY] [{}]{} - {}ms {} (Standard)", label, descPart, latency, statusPart);
        } else {
            log.info("[API_LATENCY] [{}]{} - {}ms {} (Fast)", label, descPart, latency, statusPart);
        }
    }
}
