package com.studyMate.studyMate.domain.question.data;

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
    LEVEL_TEST(Type.NONE);


    public enum Type {
        MAQ, SAQ, NONE
    }

    QuestionCategory(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    private final Type type;
}
