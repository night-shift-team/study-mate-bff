package com.studyMate.studyMate.domain.question.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * LEVEL_TEST only use in history table.
 */
public enum QuestionCategory {
    ALGORITHUM_MAQ(Type.MAQ),
    OS_MAQ(Type.MAQ),
    NETWORK_MAQ(Type.MAQ),
    DB_MAQ(Type.MAQ),
    DESIGN_MAQ(Type.MAQ),
    ALGORITHUM_SAQ(Type.SAQ),
    OS_SAQ(Type.SAQ),
    NETWORK_SAQ(Type.SAQ),
    DB_SAQ(Type.SAQ),
    DESIGN_SAQ(Type.SAQ),
    LEVEL_TEST(Type.NONE)
    ;

    /**
     * 대 분류용 (객관식, 주관식)
     */
    public enum Type {
        MAQ, SAQ, NONE
    }

    private final Type type;

    QuestionCategory(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    /**
     * LEVEL Test를 제외한 카테고리 목록을 반환
     */
    public static List<QuestionCategory> getQuestionCategoryList() {
        return Arrays.stream(QuestionCategory.values())
                .filter(qc -> qc != QuestionCategory.LEVEL_TEST)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리 리스트를 클라이언트에 출력할 뷰 네임으로 변경.
     */
    public static String getQuestionCategoryViewName(QuestionCategory originalCategory) {
        return switch(originalCategory) {
            case ALGORITHUM_MAQ -> "알고리즘-객관식";
            case ALGORITHUM_SAQ -> "알고리즘-객관식";
            case OS_MAQ -> "운영체제-객관식";
            case OS_SAQ -> "운영체제-주관식";
            case DB_MAQ -> "데이터베이스-객관식";
            case DB_SAQ -> "데이터베이스-주관식";
            case NETWORK_MAQ -> "네트워크-객관식";
            case NETWORK_SAQ -> "네트워크-주관식";
            case DESIGN_MAQ -> "디자인패턴-객관식";
            case DESIGN_SAQ -> "디자인패턴-주관식";
            case LEVEL_TEST -> "레벨-테스트";
            default -> "미등록-카테고리";
        };
    }
}
