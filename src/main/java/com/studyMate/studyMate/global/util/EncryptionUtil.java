package com.studyMate.studyMate.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import static com.studyMate.studyMate.global.util.StringUtil.isNullOrEmpty;

@Component
@RequiredArgsConstructor
public class EncryptionUtil {
    private static final int STRENGTH = 12;
    private final BCryptPasswordEncoder encoder;

    /**
     * 암호화 (Bcrypt)
     */
    public String encryptBcrypt(String plainText) {
        if(isNullOrEmpty(plainText)) {
            throw new IllegalArgumentException("plain text can not be null");
        }

        return encoder.encode(plainText);
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

        return encoder.matches(plainText, cipherText);
    }
}
