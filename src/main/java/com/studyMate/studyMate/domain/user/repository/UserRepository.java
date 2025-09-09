package com.studyMate.studyMate.domain.user.repository;

import com.studyMate.studyMate.domain.user.data.UserStatus;
import com.studyMate.studyMate.domain.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, String>, UserRepositoryCustom {
    @EntityGraph(attributePaths = {"userOAuths"})
    Optional<User> findByUserIdAndStatus(String id, UserStatus status);
    Optional<User> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);
}
