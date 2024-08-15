package com.boardgo.common.utils;

import com.boardgo.common.exception.OAuth2Exception;
import com.boardgo.domain.user.service.dto.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class SecurityUtils {

    public static Long currentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new OAuth2Exception("인증 정보가 존재하지 않습니다.");
        }
        if (!(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new OAuth2Exception("인증된 사용자가 아닙니다.");
        }
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return user.getId();
    }
}
