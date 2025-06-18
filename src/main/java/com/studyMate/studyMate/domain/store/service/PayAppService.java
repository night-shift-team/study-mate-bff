package com.studyMate.studyMate.domain.store.service;

import com.studyMate.studyMate.domain.store.data.PaymentStatus;
import com.studyMate.studyMate.domain.store.dto.PayAppRequestDto;
import com.studyMate.studyMate.domain.store.entity.StoreItems;
import com.studyMate.studyMate.domain.store.entity.StoreOrders;
import com.studyMate.studyMate.domain.store.repository.StoreItemsRepository;
import com.studyMate.studyMate.domain.store.repository.StoreOrdersRepository;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.domain.user.repository.UserRepository;
import com.studyMate.studyMate.global.error.CustomException;
import com.studyMate.studyMate.global.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PayAppService {
    private final RestTemplate restTemplate = new RestTemplate();

    private final StoreItemsRepository storeItemsRepository;
    private final StoreOrdersRepository storeOrdersRepository;
    private final UserRepository userRepository;

    @Value("${e.payapp.user_phone}")
    private String PAYAPP_PHONE;

    private final String PAYAPP_URL = "https://api.payapp.kr/oapi/apiLoad.html";
    private final String PAYAPP_CALLBACK_URL = "https://api-dev.study-mate.academy/api/v1/store/payment/callback";


    public String requestPayAppPay(PayAppRequestDto dto, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USERID));

        StoreItems item = storeItemsRepository.findByItemId(dto.getItemId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_ITEM_ID));

        MultiValueMap<String, String> payAppRequest = createPayAppPayParam(item.getItemId(), user.getUserId());

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(payAppRequest, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(PAYAPP_URL, request, String.class);
            log.info("[store - Check response] " + response);

            String body = response.getBody();
            if (body == null || body.isBlank()) {
                log.error("[store] PayApp Response Body is null");
            }

            String payUrl = extractPayUrlFromBody(body);
            if (payUrl == null) {
                log.error("[store] PayApp Response Body is null");
            }

            log.info("[store] 결제 URL 생성 :" + body);

            return payUrl;
        } catch (RuntimeException e) {
            log.error("[store] payment failed : " + e.getMessage());
            throw new CustomException(ErrorCode.PAYMENT_FAIELD);
        }
    }

    public String handlePayAppCallback(HttpServletRequest request) {
        String rawData = request.getParameterMap().entrySet().stream()
                .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
                .collect(Collectors.joining("&"));

        String buyer = request.getParameter("buyerid"); // 구매자 ID
        String payAppOrderId = request.getParameter("mul_no"); // 결제 요청번호 (결제 취소시 사용)
        PaymentStatus payState = getPaymentStatusName(request.getParameter("pay_state")); // 결제 상태

        String paidPrice = request.getParameter("price"); // 결제금액
        String payMethod = getPaymentMethodName(request.getParameter("pay_type")); // 결제 수단

        String reqDate = request.getParameter("reqdate"); // 결제 요청 시작일
        String payDate = request.getParameter("pay_date"); // 결제일

        User user = userRepository.findById(buyer)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USERID));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        /**
         * 공백 오류 수정
         */
        System.out.println("Check 공백 :" + (int) reqDate.charAt(10));
        String cleanedReqDate = reqDate.replaceAll("\\s+", " ").trim();
        String cleanedPayDate = payDate.replaceAll("\\s+", " ").trim();

        StoreOrders order = storeOrdersRepository.save(StoreOrders.builder()
                .user(user)
                .payAppOrderId(payAppOrderId)
                .status(payState)
                .paidPrice(Integer.parseInt(paidPrice))
                .paymentMethod(payMethod)
                .payReqDate(LocalDateTime.parse(cleanedReqDate, formatter))
                .payDate(LocalDateTime.parse(cleanedPayDate, formatter))
                .payAppRaw(rawData)
                .build()
        );

        log.info("[store] {} 유저가, {} 금액을 결제했습니다.", user.getLoginId(), paidPrice);
        return order.getOrderId();
    }

    private MultiValueMap<String, String> createPayAppPayParam(
            String itemId,
            String buyerId
    ) {
        StoreItems item = storeItemsRepository.findById(itemId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_ITEM_ID));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cmd", "payrequest");
        params.add("userid", "blockmonkey");
        params.add("goodname", item.getName());
        params.add("price", String.valueOf(item.getPriceKrw()));
        params.add("recvphone", PAYAPP_PHONE);
        params.add("smsuse", "n");
        params.add("feedbackurl", PAYAPP_CALLBACK_URL);
        params.add("buyerid", buyerId);

        return params;
    };

    private String extractPayUrlFromBody(String body) {
        return Arrays.stream(body.split("&"))
                .map(s -> s.split("=", 2))
                .filter(kv -> kv.length == 2)
                .collect(Collectors.toMap(
                        kv -> URLDecoder.decode(kv[0], StandardCharsets.UTF_8),
                        kv -> URLDecoder.decode(kv[1], StandardCharsets.UTF_8)
                ))
                .get("reqResult[0][payurl]");
    }

    private String getPaymentMethodName(String methodCode) {
        return switch (methodCode) {
            case "1" -> "신용카드";
            case "2" -> "휴대전화";
            case "4" -> "대면결제";
            case "6" -> "계좌이체";
            case "7" -> "가상계좌";
            case "15" -> "카카오페이";
            case "16" -> "네이버페이";
            case "17" -> "등록결제";
            case "21" -> "스마일페이";
            case "22" -> "위챗페이";
            case "23" -> "애플페이";
            case "24" -> "내통장결제";
            default -> "미확인";
        };
    }

    private PaymentStatus getPaymentStatusName(String status) {
        return switch (status) {
            case "1" -> PaymentStatus.REQUEST;
            case "4" -> PaymentStatus.PAID;
            case "8" -> PaymentStatus.REQ_CANCELLED;
            case "32" -> PaymentStatus.REQ_CANCELLED;
            case "9" -> PaymentStatus.PAY_ALL_CANCELLED;
            case "64" -> PaymentStatus.PAY_ALL_CANCELLED;
            case "70" -> PaymentStatus.PAY_PARTIAL_CANCELLED;
            case "71" -> PaymentStatus.PAY_PARTIAL_CANCELLED;
            case "10" -> PaymentStatus.PENDING;
            default -> PaymentStatus.FAILED;
        };
    }
}
