package com.boardgo.common.config;

import com.boardgo.common.exception.KakaoException;
import com.boardgo.common.exception.advice.dto.ErrorCode;
import com.boardgo.domain.kakao.KakaoClient;
import com.boardgo.domain.kakao.KakaoOAuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final KakaoOAuthProperties properties;

    @Bean
    KakaoClient createRestClient() {
        RestClient kakaoClient =
                RestClient.builder()
                        .baseUrl(properties.baseUrl())
                        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .defaultHeader(
                                HttpHeaders.CONTENT_TYPE,
                                String.valueOf(MediaType.APPLICATION_FORM_URLENCODED))
                        .defaultStatusHandler(
                                HttpStatusCode::is5xxServerError,
                                ((request, response) -> {
                                    throw new KakaoException(
                                            request.getURI().toString(),
                                            ErrorCode.INTERNAL_SERVER_ERROR);
                                }))
                        .build();

        RestClientAdapter adapter = RestClientAdapter.create(kakaoClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(KakaoClient.class);
    }
}
