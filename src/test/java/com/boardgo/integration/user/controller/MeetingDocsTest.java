package com.boardgo.integration.user.controller;

import static com.boardgo.common.constant.HeaderConstant.*;
import static io.restassured.RestAssured.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.boardgo.domain.meeting.controller.dto.MeetingCreateRequest;
import com.boardgo.integration.support.RestDocsTestSupport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class MeetingDocsTest extends RestDocsTestSupport {
	@Test
	@DisplayName("사용자는 모임을 만들 수 있다")
	void 사용자는_모임을_만들_수_있다() throws JsonProcessingException {
		//given
		MeetingCreateRequest request = new MeetingCreateRequest(
			"Test meeting content",
			"FREE",
			10,
			"Seoul",
			"Gangnam",
			"37.5665",
			"126.9780",
			LocalDateTime.now().plusDays(1),
			Arrays.asList(1L, 2L),
			Arrays.asList(1L, 2L)
		);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		System.out.println("objectMapper.writeValueAsString(request) = " + objectMapper.writeValueAsString(request));

		File imageFile = new File("src/test/resources/static/image/Backend RestDocs.png");
		System.out.println("imageFile = " + imageFile);
		System.out.println("imageFile.getAbsolutePath() = " + imageFile.getAbsolutePath());
		System.out.println(testAccessToken);
		given(this.spec)
			.log().all()
			.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
			.header(API_VERSION_HEADER, "1")
			.header(AUTHORIZATION, testAccessToken)
			.multiPart("meetingCreateRequest", request, MediaType.APPLICATION_JSON_VALUE)
			.multiPart("image", imageFile, MediaType.IMAGE_PNG_VALUE)
			.filter(document("create-meeting",
				requestParts(
					partWithName("meetingCreateRequest").description("모임 상세 모임"),
					partWithName("image").description("썸네일 이미지 파일")
				),
				requestPartFields("meetingCreateRequest",
					fieldWithPath("content").description("모임 내용"),
					fieldWithPath("type").description("모임 타입 : FREE or ACCEPT"),
					fieldWithPath("limitParticipant").description("최대 참가자 수"),
					fieldWithPath("city").description("도시"),
					fieldWithPath("county").description("구(ex. 강남구, 성동구)"),
					fieldWithPath("latitude").description("위도"),
					fieldWithPath("longitude").description("경도"),
					fieldWithPath("meetingDatetime").description(
						"미팅 날짜 (format: yyyy-MM-dd HH:mm:ss)"),
					fieldWithPath("boardGameIdList").description("보드게임 id List(Array)"),
					fieldWithPath("genreIdList").description("보드게임장르 id List(Array)")
				)
			))
			.when()
			.post("/meeting")
			.then()
			.statusCode(HttpStatus.CREATED.value());

	}
}
