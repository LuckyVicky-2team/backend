package com.boardgo.domain.meeting;

import com.boardgo.domain.meeting.service.MeetingLikeCommandUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeetingLikeController {
    private final MeetingLikeCommandUseCase meetingLikeCommandUseCase;

    @PostMapping("/meeting/like")
    public ResponseEntity<Void> likeMeetings(
            @RequestParam("meetingIdList") List<Long> meetingIdList) {
        meetingLikeCommandUseCase.createMany(meetingIdList);
        return ResponseEntity.ok().build();
    }
}
