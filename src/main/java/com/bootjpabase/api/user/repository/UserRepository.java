package com.bootjpabase.api.user.repository;

import com.bootjpabase.api.user.domain.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(@NotBlank(message = "아이디는 필수입니다") @Length(max = 30, message = "아이디는 30자 이하로 입력해야 합니다.") String userId);
}
