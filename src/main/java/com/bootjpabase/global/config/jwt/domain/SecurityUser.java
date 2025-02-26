package com.bootjpabase.global.config.jwt.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@NoArgsConstructor
public class SecurityUser implements UserDetails {

    private String id;
    private String password;
    private Set<GrantedAuthority> authorities = new HashSet<>();
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;
    private Map<String, Object> information = new HashMap<>();

    public SecurityUser(String id, String password, Map<String, Object> information) {
        this.id = id;
        this.password = password;
        this.information = information;
    }

    @Override
    public String getUsername() {
        return "";
    }

}
