package com.boardgo.domain.meeting;

import static com.boardgo.common.constant.HeaderConstant.*;

import com.boardgo.domain.meeting.controller.request.MyPageMeetingFilterRequest;
import com.boardgo.domain.meeting.service.MyPageMeetingQueryUseCase;
import com.boardgo.domain.meeting.service.response.MeetingMyPageResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MyPageMeetingController {
    private final MyPageMeetingQueryUseCase myPageMeetingQueryUseCase;

    @GetMapping(value = "/my/meeting", headers = API_VERSION_HEADER1)
    public ResponseEntity<List<MeetingMyPageResponse>> getMyPageMeetingByFilter(
            MyPageMeetingFilterRequest filter) {
        List<MeetingMyPageResponse> result =
                myPageMeetingQueryUseCase.findByFilter(filter.filter());
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }
}
