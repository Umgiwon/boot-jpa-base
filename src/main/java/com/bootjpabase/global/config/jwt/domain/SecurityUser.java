package com.bootjpabase.global.config.jwt.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@NoArgsConstructor
public class SecurityUser implements UserDetails {

    private final Set<GrantedAuthority> authorities = new HashSet<>();
    private final boolean accountNonExpired = true;
    private final boolean accountNonLocked = true;
    private final boolean credentialsNonExpired = true;
    private final boolean enabled = true;
    private String id;
    private String userName;
    private String password;
    private Map<String, Object> information = new HashMap<>();

    public SecurityUser(String id, String userName, String password, Map<String, Object> information) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.information = information;
    }

    @Override
    public String getUsername() {
        return "";
    }

}
