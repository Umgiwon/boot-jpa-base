package com.bootjpabase.api.user.repository;

import com.bootjpabase.api.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);

    boolean existsByUserId(String userId);

    boolean existsByUserPhone(String userPhone);

    boolean existsByUserEmail(String userEmail);
}
