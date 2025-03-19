package com.studyMate.studyMate.domain.user.repository;

import com.studyMate.studyMate.domain.user.dto.GetUserRankingResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    GetUserRankingResponseDto findUsersAndRanking(String userId, Pageable pageable);
}
