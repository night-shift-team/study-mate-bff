package com.studyMate.studyMate.global.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.time.LocalDateTime;
import java.util.TimeZone;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final Environment environment;

    @PostConstruct
    public void started() {
        // 타임존 설정 (UTC)
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        String[] activeProfiles = environment.getActiveProfiles();
        log.info("서버시간확인 : {}", LocalDateTime.now());
    }
}
