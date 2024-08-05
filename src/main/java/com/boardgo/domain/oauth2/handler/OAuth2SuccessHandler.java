package com.boardgo.domain.oauth2.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    /*
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
        response.sendRedirect("http://localhost:3000/"); // FIXME: 개발&운영 서버 리다이렉트 주소
    }

    private Cookie createCookies(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(Math.toIntExact(ACCESS_TOKEN_DURATION));
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

     */
}
