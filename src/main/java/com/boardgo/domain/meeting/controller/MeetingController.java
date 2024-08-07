package com.boardgo.domain.meeting.controller;

import static com.boardgo.common.constant.HeaderConstant.*;

import com.boardgo.domain.meeting.controller.dto.MeetingCreateRequest;
import com.boardgo.domain.meeting.service.MeetingUseCase;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingUseCase meetingUseCase;

    // TODO: HeaderConstant Merge 시에 Header API 버전 추가
    @PostMapping(
            value = "/meeting",
            headers = API_VERSION_HEADER1,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> create(
            @RequestPart("meetingCreateRequest") MeetingCreateRequest meetingCreateRequest,
            @RequestPart("image") MultipartFile imageFile) {
        Long meetingId = meetingUseCase.create(meetingCreateRequest, imageFile);
        return ResponseEntity.created(URI.create(String.valueOf(meetingId))).build();
    }
}
