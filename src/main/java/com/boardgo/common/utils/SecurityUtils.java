package com.boardgo.common.utils;

import com.boardgo.domain.user.entity.RoleType;
import com.boardgo.domain.user.service.dto.CustomUserDetails;
import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class SecurityUtils {

    private static final Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    public static boolean hasUserRole() {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream()
                .filter(o -> o.getAuthority().equals(RoleType.USER.getCode()))
                .findAny()
                .isPresent();
    }

    public static Long currentUserId() {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return user.getId();
    }
}
