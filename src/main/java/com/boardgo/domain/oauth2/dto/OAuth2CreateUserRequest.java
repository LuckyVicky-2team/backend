package com.boardgo.domain.oauth2.dto;

import com.boardgo.domain.oauth2.entity.ProviderType;

public record OAuth2CreateUserRequest(String email, ProviderType providerType) {}
