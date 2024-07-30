package com.boardgo.domain.test.controller;

import com.boardgo.domain.test.dto.TestRequest;
import com.boardgo.domain.test.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    void save(TestRequest testRequest){
    }

}
