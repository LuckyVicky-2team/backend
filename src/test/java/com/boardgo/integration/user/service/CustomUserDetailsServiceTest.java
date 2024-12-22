package com.boardgo.integration.user.service;

import com.boardgo.domain.user.entity.UserInfoEntity;
import com.boardgo.domain.user.entity.enums.ProviderType;
import com.boardgo.domain.user.repository.UserRepository;
import com.boardgo.integration.support.IntegrationTestSupport;
import java.time.LocalDateTime;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomUserDetailsServiceTest extends IntegrationTestSupport {

    @Autowired private UserRepository userRepository;

    @Test
    public void testFindByEmailAndProviderTypeAndDeleteAtIsNotNull() {
        // Given
        UserInfoEntity user =
                UserInfoEntity.builder()
                        .email("test@example.com")
                        .providerType(ProviderType.GOOGLE)
                        .deleteAt(LocalDateTime.now())
                        .build();
        userRepository.save(user);

        // When
        Optional<UserInfoEntity> result =
                userRepository.findByEmailAndProviderTypeAndDeleteAtIsNull(
                        "test@example.com", ProviderType.GOOGLE);

        // Then
        Assertions.assertThat(result.isEmpty()).isTrue();
    }
}
