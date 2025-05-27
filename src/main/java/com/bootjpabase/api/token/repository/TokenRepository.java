package com.bootjpabase.api.token.repository;

import com.bootjpabase.api.token.domain.entity.RefreshToken;
import com.bootjpabase.api.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByToken(String token);

    RefreshToken findByUser(User user);

    RefreshToken findByUser_UserSn(Long userSn);
}
