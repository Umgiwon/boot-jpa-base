package com.bootjpabase.global.domain.auditoraware;

import com.bootjpabase.global.config.jwt.domain.SecurityUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * BaseEntity의 등록자, 수정자에 대한 정보를 넣기 위한 컴포넌트
 */
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.of("SYSTEM"); // 인증되지 않은 경우 SYSTEM으로 설정
        }

        SecurityUser securityManager = (SecurityUser) authentication.getPrincipal();

        // 현재 인증된 사용자 정보 가져오기
        return Optional.ofNullable(securityManager.getId());
    }
}
