package com.studyMate.studyMate.global.util;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static com.studyMate.studyMate.global.util.StringUtil.isNullOrEmpty;

@Component
@RequiredArgsConstructor
public class EncryptionUtil {
    /**
     * 암호화 (Bcrypt)
     */
    public String encryptBcrypt(String plainText) {
        if (isNullOrEmpty(plainText)) {
            throw new IllegalArgumentException("plain text can not be null");
        }

        // Bcrypt 해시 생성
        return BCrypt.hashpw(plainText, BCrypt.gensalt());
    }

    /**
     * 비교 검증 (Bcrypt)
     */
    public boolean compareBcrypt(String plainText, String cipherText) {
        if (isNullOrEmpty(plainText)) {
            throw new IllegalArgumentException("plain text can not be null");
        }

        if (isNullOrEmpty(cipherText)) {
            throw new IllegalArgumentException("cipher text can not be null");
        }

        // 해시와 평문 비교
        return BCrypt.checkpw(plainText, cipherText);
    }

    /**
     * UTF-8 Standard URL Decoder
     */
    public String decodeUrl(String encodedUrl) {
        if (isNullOrEmpty(encodedUrl)) {
            throw new IllegalArgumentException("encoded url can not be null");
        }

        return URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8);
    }
}
