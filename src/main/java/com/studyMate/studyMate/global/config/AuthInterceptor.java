package com.studyMate.studyMate.global.config;

import com.studyMate.studyMate.global.error.CustomException;
import com.studyMate.studyMate.global.error.ErrorCode;
import com.studyMate.studyMate.global.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@AllArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String sessionId = request.getSession().getId();

        boolean verifyResult = false;

        if (handler instanceof HandlerMethod handlerMethod) {
            RoleAuth roleAuth = handlerMethod.getMethodAnnotation(RoleAuth.class);
            // Method에 @RoleAuth Annotation이 있는지 확인,
            if (roleAuth != null) {
                String authHeader = request.getHeader("Authorization");

                // Authorization Header 확인
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    log.info("EMPTY TOKEN || [{}}] {}", sessionId, requestURI);
                    throw new CustomException(ErrorCode.EMPTY_TOKEN);
                }

                // Token 검증
                String acToken = authHeader.substring(7);

                // JWT 검증을 거치고, -> 만약 실패시 에러
                boolean isValid = jwtTokenUtil.validateToken(acToken);
                long userId = jwtTokenUtil.getUserId(acToken);
                if(!isValid) {
                    throw new CustomException(ErrorCode.UNAUTHORIZED);
                }

                request.setAttribute("userId", userId);
                log.info("[AuthInterceptor] user id : {} || url : {}", userId, requestURI);

                verifyResult = isValid;
            } else {
                verifyResult = true;
            }
        }

        return verifyResult;
    }
}
