package com.studyMate.studyMate.global.util;

import org.springframework.stereotype.Component;

@Component
public class StringUtil {
    public static boolean isNullOrEmpty(String str){
        return str == null || str.isEmpty();
    }
}
