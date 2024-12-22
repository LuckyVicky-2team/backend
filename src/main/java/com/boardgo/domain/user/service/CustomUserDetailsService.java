package com.boardgo.domain.user.service;

import com.boardgo.common.exception.CustomUnAuthorizedException;
import com.boardgo.domain.user.entity.UserInfoEntity;
import com.boardgo.domain.user.entity.enums.ProviderType;
import com.boardgo.domain.user.repository.UserRepository;
import com.boardgo.domain.user.service.response.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserInfoEntity userInfoEntity =
                userRepository
                        .findByEmailAndProviderTypeAndDeleteAtIsNull(email, ProviderType.LOCAL)
                        .orElseThrow(() -> new CustomUnAuthorizedException("계정이 존재하지 않음"));
        return new CustomUserDetails(userInfoEntity);
    }
}
