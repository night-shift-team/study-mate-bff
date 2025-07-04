package com.studyMate.studyMate.domain.store.controller;

import com.studyMate.studyMate.domain.store.dto.PageResponseDto;
import com.studyMate.studyMate.domain.store.dto.PayAppCallbackResponseDto;
import com.studyMate.studyMate.domain.store.dto.PayAppRequestDto;
import com.studyMate.studyMate.domain.store.dto.vo.OrderDto;
import com.studyMate.studyMate.domain.store.service.PaymentEmitterService;
import com.studyMate.studyMate.domain.store.service.PaymentService;
import com.studyMate.studyMate.global.config.RoleAuth;
import com.studyMate.studyMate.global.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Tag(name = "store")
@RequestMapping("/store")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentEmitterService paymentEmitterService;
    private final JwtTokenUtil jwtTokenUtil;
    private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @PostMapping("/payment/request")
    @Operation(summary = "PayApp 결제 URL 호출 API (로그인 필수)", description = "페이앱 결제 URL 호출 메소드")
    @RoleAuth
    public String createPayment(
            HttpServletRequest req,
            @RequestBody PayAppRequestDto dto
    ) {
        String userId = (String) req.getAttribute("userId");
        return paymentService.requestPayAppPay(dto, userId);
    }

    @PostMapping("/payment/callback")
    @Hidden
    @Operation(summary = "비호출 함수 (PayApp 콜백용)", description = "비호출 함수 (PayApp 콜백용)")
    public String handlePayAppCallback(HttpServletRequest req) {
        PayAppCallbackResponseDto payAppCallback = paymentService.handlePayAppCallback(req);

        SseEmitter emitter = emitters.get(payAppCallback.getUserId());

        if (emitter != null) {
            try {
                paymentEmitterService.createPaymentEvent(payAppCallback.getUserId(), payAppCallback);
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }

        return payAppCallback.getOrderId();
    }

    @GetMapping(value = "/payment/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(@RequestParam("token") String token) {
        if(!jwtTokenUtil.validateToken(token)) {
            log.warn("토큰 검증 실패 : {}", token);
        } else {
            log.warn("토큰 검증 성공 : {}", token);
        }

        return paymentEmitterService.createEmitter(jwtTokenUtil.getUserId(token));
    }

    @GetMapping("/payment/orders/my")
    @Operation(summary = "내 상점 결제 내역 조회 API", description = "나의 결제 내역을 조회하는 기능")
    @RoleAuth
    public PageResponseDto<OrderDto> getMyOrders(
            HttpServletRequest req,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        String userId = (String) req.getAttribute("userId");
        return paymentService.getMyOrders(userId, page, limit);
    }
}
