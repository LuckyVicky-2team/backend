package com.boardgo.domain.user.controller;

import static com.boardgo.common.constant.HeaderConstant.API_VERSION_HEADER1;
import static com.boardgo.common.constant.HeaderConstant.AUTHORIZATION;
import static com.boardgo.common.constant.HeaderConstant.BEARER;
import static com.boardgo.common.utils.CookieUtil.getCookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TokenControllerV1 {

    @GetMapping(value = "/token", headers = API_VERSION_HEADER1)
    public ResponseEntity<Void> getToken(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> jwtToken =
                Optional.ofNullable(
                        getCookie(request, AUTHORIZATION)
                                .map(Cookie::getValue)
                                .orElseThrow(
                                        () ->
                                                new IllegalArgumentException(
                                                        "토큰내놔"))); // FIXME: dev pull 받아서 커스텀 예외
        response.addHeader(AUTHORIZATION, BEARER + jwtToken.get());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // TODO. refresh 토큰

}
