package com.boardgo.oauth2.handler;

import static com.boardgo.common.constant.HeaderConstant.AUTHORIZATION;
import static com.boardgo.common.constant.TimeConstant.ACCESS_TOKEN_DURATION;

import com.boardgo.jwt.JWTUtil;
import com.boardgo.oauth2.entity.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String accessToken =
                jwtUtil.createJwt(
                        oAuth2User.getId(), oAuth2User.getRoleType(), ACCESS_TOKEN_DURATION);
        response.addCookie(createCookies(AUTHORIZATION, accessToken));
        response.sendRedirect("http://localhost:3000/home"); // FIXME: 개발&운영 서버 리다이렉트 주소/home 주소로 변환
    }

    private Cookie createCookies(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(Math.toIntExact(ACCESS_TOKEN_DURATION));
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
