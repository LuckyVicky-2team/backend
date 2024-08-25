package com.boardgo.integration.review.controller;

import static com.boardgo.common.constant.HeaderConstant.API_VERSION_HEADER;
import static com.boardgo.common.constant.HeaderConstant.AUTHORIZATION;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

import com.boardgo.domain.review.controller.request.ReviewCreateRequest;
import com.boardgo.integration.support.RestDocsTestSupport;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.RequestPartsSnippet;

public class ReviewRestDocs extends RestDocsTestSupport {

    @Test
    @DisplayName("리뷰 모임 목록 조회하기")
    void 리뷰_모임_목록_조회하기() {
        given(this.spec)
                .log()
                .all()
                .port(port)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .header(API_VERSION_HEADER, "1")
                .header(AUTHORIZATION, testAccessToken)
                .multiPart("reviewType", "FINISH")
                .filter(
                        document(
                                "get-review-meetings",
                                getReviewTypeRequestPartBodySnippet(),
                                getReviewMeetingsResponseFieldsSnippet()))
                .when()
                .get("/review/meetings")
                .then()
                .log()
                .ifError()
                .statusCode(HttpStatus.OK.value());
    }

    private RequestPartsSnippet getReviewTypeRequestPartBodySnippet() {
        return requestParts(partWithName("reviewType").description("상황별 타입(PRE_PROGRESS/FINISH)"));
    }

    private ResponseFieldsSnippet getReviewMeetingsResponseFieldsSnippet() {
        return responseFields(
                List.of(
                        fieldWithPath("[].meetingId")
                                .type(JsonFieldType.NUMBER)
                                .description("모임 고유Id"),
                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("모임 제목"),
                        fieldWithPath("[].thumbnail")
                                .type(JsonFieldType.STRING)
                                .description("모임 썸네일"),
                        fieldWithPath("[].city").type(JsonFieldType.STRING).description("도시"),
                        fieldWithPath("[].county")
                                .type(JsonFieldType.STRING)
                                .description("모임 구(ex. 강남구, 성동구)"),
                        fieldWithPath("[].meetingDate")
                                .type(JsonFieldType.STRING)
                                .description("모임 날짜")));
    }

    @Test
    @DisplayName("리뷰 작성하기")
    void 리뷰_작성하기() {
        ReviewCreateRequest request =
                new ReviewCreateRequest(3L, 2L, 5, List.of(1L, 2L, 4L, 9L, 10L));

        given(this.spec)
                .port(port)
                .log()
                .all()
                .header(API_VERSION_HEADER, "1")
                .header(AUTHORIZATION, testAccessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("create-review", getReviewCreateRequestFieldsSnippet()))
                .body(writeValueAsString(request))
                .urlEncodingEnabled(false)
                .when()
                .post("/review")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .log()
                .ifError()
                .extract();
    }

    private RequestFieldsSnippet getReviewCreateRequestFieldsSnippet() {
        return requestFields(
                fieldWithPath("revieweeId").type(NUMBER).description("리뷰 받은 참여자 고유Id"),
                fieldWithPath("meetingId").type(NUMBER).description("모임 고유Id"),
                fieldWithPath("rating").type(NUMBER).description("평점"),
                fieldWithPath("evaluationTagList").type(ARRAY).description("평가태그 목록"));
    }
}
