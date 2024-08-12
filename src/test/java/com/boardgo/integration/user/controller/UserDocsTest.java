package com.boardgo.integration.user.controller;

import static com.boardgo.common.constant.HeaderConstant.API_VERSION_HEADER;
import static com.boardgo.common.constant.HeaderConstant.AUTHORIZATION;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

import com.boardgo.domain.user.controller.dto.SocialSignupRequest;
import com.boardgo.domain.user.entity.ProviderType;
import com.boardgo.domain.user.entity.UserInfoEntity;
import com.boardgo.domain.user.repository.UserRepository;
import com.boardgo.integration.support.RestDocsTestSupport;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

public class UserDocsTest extends RestDocsTestSupport {

    @Autowired private UserRepository userRepository;

    @Test
    @DisplayName("사용자는 이메일을 중복 검사할 수 있다")
    void 사용자는_이메일을_중복_검사할_수_있다() {
        // given

        given(this.spec)
                .log()
                .all()
                .port(port)
                .header(API_VERSION_HEADER, "1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParams("email", "aa@aa.com")
                .filter(
                        document(
                                "checkEmail",
                                queryParameters(
                                        parameterWithName("email")
                                                .description(
                                                        "이메일 주소 형식에 맞아야합니다! 아니면 BadRequest 발생"))))
                .when()
                .get("/check-email")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("사용자는 닉네임을 중복 검사할 수 있다")
    void 사용자는_닉네임을_중복_검사할_수_있다() {

        given(this.spec)
                .log()
                .all()
                .port(port)
                .header(API_VERSION_HEADER, "1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParams("nickName", "nickname")
                .filter(
                        document(
                                "checkNickName",
                                queryParameters(
                                        parameterWithName("nickName")
                                                .description(
                                                        "공백이나 Null은 통과되지 않습니다! 아니면 BadRequest 발생"))))
                .when()
                .get("/check-nickname")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("소셜로그인 시 서비스 회원가입을 한다")
    void 소셜로그인_시_서비스_회원가입을_한다() {
        userRepository.save(
                UserInfoEntity.builder()
                        .email("abc123@google.com")
                        .password(null)
                        .nickName(null)
                        .providerType(ProviderType.GOOGLE)
                        .deleteAt(null)
                        .build());

        SocialSignupRequest socialSignupRequest =
                new SocialSignupRequest("Bread", List.of("ENFJ", "HAPPY"));

        given(this.spec)
                .port(port)
                .log()
                .all()
                .header(API_VERSION_HEADER, "1")
                .header(AUTHORIZATION, testAccessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(
                        document(
                                "social-signup",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                getSocialSignupRequestFieldsSnippet()))
                .body(socialSignupRequest)
                .urlEncodingEnabled(false)
                .when()
                .post("/social/signup")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .log()
                .ifError()
                .extract();
    }

    private RequestFieldsSnippet getSocialSignupRequestFieldsSnippet() {
        return requestFields(
                fieldWithPath("nickName").type(STRING).description("닉네임"),
                fieldWithPath("prTags").type(ARRAY).description("PR태그").optional());
    }

    @Test
    @DisplayName("내 개인정보 조회하기")
    void 내_개인정보_조회하기() {
        userRepository.save(
                UserInfoEntity.builder()
                        .email("53634734")
                        .password(null)
                        .nickName("Bread")
                        .profileImage("앙버터프레즐.jpg")
                        .providerType(ProviderType.KAKAO)
                        .build());

        given(this.spec)
                .log()
                .all()
                .port(port)
                .header(API_VERSION_HEADER, "1")
                .header(AUTHORIZATION, testAccessToken)
                .filter(
                        document(
                                "personal-info",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                getPersonalInfoResponseFieldsSnippet()))
                .when()
                .get("/personal-info")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    private ResponseFieldsSnippet getPersonalInfoResponseFieldsSnippet() {
        return responseFields(
                fieldWithPath("email").type(JsonFieldType.STRING).description("회원 고유 ID"),
                fieldWithPath("nickName").type(JsonFieldType.STRING).description("닉네임"),
                fieldWithPath("profileImage").type(JsonFieldType.STRING).description("프로필 이미지"),
                fieldWithPath("averageGrade").type(JsonFieldType.NUMBER).description("평균 별점"),
                fieldWithPath("prTags")
                        .type(JsonFieldType.ARRAY)
                        .description("PR태그 (없을 경우 빈배열 반환)"));
    }
}
