package com.boardgo.integration.user.controller;

import static com.boardgo.common.constant.HeaderConstant.API_VERSION_HEADER;
import static com.boardgo.common.constant.HeaderConstant.AUTHORIZATION;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

import com.boardgo.integration.support.RestDocsTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

public class UserDocsTest extends RestDocsTestSupport {

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
    @DisplayName("내 개인정보 조회하기")
    void 내_개인정보_조회하기() {
        given(this.spec)
                .log()
                .all()
                .port(port)
                .header(API_VERSION_HEADER, "1")
                .header(AUTHORIZATION, testAccessToken)
                .filter(document("personal-info", getPersonalInfoResponseFieldsSnippet()))
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
                fieldWithPath("prTags").type(JsonFieldType.ARRAY).description("PR태그"));
    }
}
