package com.studyMate.studyMate.domain.store.controller;

import com.studyMate.studyMate.domain.store.dto.PayAppRequestDto;
import com.studyMate.studyMate.domain.store.service.PayAppService;
import com.studyMate.studyMate.global.config.RoleAuth;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "store")
@RequestMapping("/store")
@AllArgsConstructor
public class PaymentController {

    private final PayAppService payAppService;

    @PostMapping("/payment/request")
    @Operation(summary = "PayApp 결제 URL 호출 API (로그인 필수)", description = "페이앱 결제 URL 호출 메소드")
    @RoleAuth
    public String createPayment(
            HttpServletRequest req,
            @RequestBody PayAppRequestDto dto
    ) {
        String userId = (String) req.getAttribute("userId");
        return payAppService.requestPayAppPay(dto, userId);
    }

    @PostMapping("/payment/callback")
    @Hidden
    @Operation(summary = "비호출 함수 (PayApp 콜백용)", description = "비호출 함수 (PayApp 콜백용)")
    public String handlePayAppCallback(HttpServletRequest req) {
        return payAppService.handlePayAppCallback(req);
    }
}
