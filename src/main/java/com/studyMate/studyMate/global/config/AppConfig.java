package com.studyMate.studyMate.global.config;

import com.studyMate.studyMate.global.util.JwtTokenUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.TimeZone;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final Environment environment;
    private final JwtTokenUtil jwtTokenUtil;

    @PostConstruct
    public void started() {
        // 타임존 설정 (UTC)
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        String[] activeProfiles = environment.getActiveProfiles();
        log.info("서버시간확인 : {}", LocalDateTime.now());
    }

    /**
     * (TODO: 문제 생성 API 생성 후, RoleAuth 적용하고 정상 작동 테스트 필요)
     */
//    @Bean
//    public AuditorAware<Long> auditorProvider() {
//        return new AuditorAware<Long>() {
//            @Override
//            public Optional<String> getCurrentAuditor() {
//                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//                if (attributes != null) {
//                    HttpServletRequest request = attributes.getRequest();
//                    String authHeader = request.getHeader("Authorization");
//
//                    if(authHeader != null && authHeader.startsWith("Bearer ")) {
//                        String acToken = authHeader.substring(7);
//                        String userId = jwtTokenUtil.getUserId(acToken);
//
//                        return Optional.ofNullable(userId);
//                    }
//                }
//                return Optional.empty();
//            }
//        };
//    }
}
