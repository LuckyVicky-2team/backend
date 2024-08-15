package com.boardgo.integration.meeting.controller;

import static com.boardgo.common.constant.HeaderConstant.API_VERSION_HEADER;
import static com.boardgo.common.constant.HeaderConstant.AUTHORIZATION;
import static com.boardgo.integration.fixture.MeetingFixture.getProgressMeetingEntity;
import static com.boardgo.integration.fixture.UserInfoFixture.socialUserInfoEntity;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

import com.boardgo.domain.meeting.controller.request.MeetingParticipateRequest;
import com.boardgo.domain.meeting.entity.MeetingType;
import com.boardgo.domain.meeting.repository.MeetingRepository;
import com.boardgo.domain.user.entity.ProviderType;
import com.boardgo.domain.user.entity.UserInfoEntity;
import com.boardgo.domain.user.repository.UserRepository;
import com.boardgo.integration.support.RestDocsTestSupport;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestFieldsSnippet;

public class MeetingParticipantDocsTest extends RestDocsTestSupport {
    @Autowired private UserRepository userRepository;
    @Autowired private MeetingRepository meetingRepository;

    //    @Test
    @DisplayName("보드게임 모임 참가하기")
    void 보드게임_모임_참가하기() throws JsonProcessingException {
        Long meetingId = 1L;
        MeetingParticipateRequest participateRequest = new MeetingParticipateRequest(meetingId);
        String requestJson = objectMapper.writeValueAsString(participateRequest);

        UserInfoEntity userInfo = userRepository.save(socialUserInfoEntity(ProviderType.KAKAO));
        meetingRepository.save(getProgressMeetingEntity(userInfo.getId(), MeetingType.FREE, 3));

        given(this.spec)
                .port(port)
                .log()
                .all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(API_VERSION_HEADER, "1")
                .header(AUTHORIZATION, testAccessToken)
                .body(requestJson)
                .filter(
                        document(
                                "meeting-participation",
                                preprocessResponse(prettyPrint()),
                                getParticipationRequestFieldsSnippet()))
                .when()
                .post("/meeting-participant/participation")
                .then()
                .log()
                .ifError()
                .statusCode(HttpStatus.CREATED.value());
    }

    RequestFieldsSnippet getParticipationRequestFieldsSnippet() {
        return requestFields(
                fieldWithPath("meetingId").type(JsonFieldType.NUMBER).description("모임 고유ID"));
    }
}