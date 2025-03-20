package com.bootjpabase.global.config.jwt.repository;

import com.bootjpabase.api.user.domain.entity.User;
import com.bootjpabase.global.config.jwt.domain.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByToken(String token);

    RefreshToken findByUser(User user);
}
