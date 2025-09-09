package com.studyMate.studyMate.domain.user.repository;

import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.domain.user.entity.UserOAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserOAuthRepository extends JpaRepository<UserOAuth, String> {
    Optional<UserOAuth> findByUser(User user);
}
