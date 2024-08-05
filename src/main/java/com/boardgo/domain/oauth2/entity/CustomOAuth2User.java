package com.boardgo.domain.oauth2.entity;

import com.boardgo.domain.user.entity.RoleType;
import com.boardgo.domain.user.entity.UserInfoEntity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User {
    private final Long id;
    private final String email;
    private final String nickname;
    private final ProviderType providerType;
    private final RoleType roleType;
    private final Collection<GrantedAuthority> authorities; // FIXME 얘는 추가왜해?
    private Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) () -> RoleType.USER.toString());
        return collection;
    }

    @Override
    public String getName() {
        return this.nickname;
    }

    public static CustomOAuth2User create(UserInfoEntity user) {
        return new CustomOAuth2User(
                user.getId(),
                user.getEmail(),
                user.getNickName(),
                user.getProviderType(),
                RoleType.USER,
                Collections.singletonList(new SimpleGrantedAuthority(RoleType.USER.getCode())));
    }

    public static CustomOAuth2User create(UserInfoEntity user, Map<String, Object> attributes) {
        CustomOAuth2User customOAuth2User = create(user);
        customOAuth2User.attributes = attributes;
        return customOAuth2User;
    }
}
