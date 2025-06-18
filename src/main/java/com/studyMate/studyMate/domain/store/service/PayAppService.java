package com.studyMate.studyMate.domain.store.service;

import com.studyMate.studyMate.domain.store.dto.PayAppRequestDto;
import com.studyMate.studyMate.domain.store.entity.StoreItems;
import com.studyMate.studyMate.domain.store.repository.StoreItemsRepository;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.domain.user.repository.UserRepository;
import com.studyMate.studyMate.global.error.CustomException;
import com.studyMate.studyMate.global.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PayAppService {
    private static final Logger log = LoggerFactory.getLogger(PayAppService.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final StoreItemsRepository storeItemsRepository;
    private final UserRepository userRepository;
    private final String PAYAPP_URL = "https://api.payapp.kr/oapi/apiLoad.html";
    private final String PAYAPP_CALLBACK_URL = "https://api-dev.study-mate.academy/api/v1/store/payment/callback";

    public String requestPayAppPay(PayAppRequestDto dto, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_USERID));

        StoreItems item = storeItemsRepository.findByItemId(dto.getItemId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_ITEM_ID));

        MultiValueMap<String, String> payAppRequest = createPayAppPayParam(item.getItemId());

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
        // 전체 파라미터 출력
        request.getParameterMap().forEach((key, values) -> {
            log.info("[PayApp Callback Param] {} = {}", key, String.join(",", values));
        });

        String result = request.getParameter("result");
        String payState = request.getParameter("paystate");
        String orderNum = request.getParameter("order_num"); // 주문번호
        String price = request.getParameter("price"); // 결제금액
        String mulNo = request.getParameter("mul_no"); // PayApp 결제번호

        log.info("[PayApp Callback] result={}, payState={}, orderNum={}, price={}, mulNo={}",
                result, payState, orderNum, price, mulNo);

        log.info("결제 성공 처리 완료 for itemId={}", orderNum);

        return "test";
    }

    private MultiValueMap<String, String> createPayAppPayParam(String itemId) {
        StoreItems item = storeItemsRepository.findById(itemId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_ITEM_ID));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cmd", "payrequest");
        params.add("userid", "blockmonkey");
        params.add("goodname", item.getName());
        params.add("price", String.valueOf(item.getPriceKrw()));
        params.add("recvphone", "01000000000");
        params.add("smsuse", "n");
        params.add("callback", PAYAPP_CALLBACK_URL);

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
}
