package com.boardgo.domain.test.controller;

import com.boardgo.domain.test.dto.TestRequest;
import com.boardgo.domain.test.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    void save(TestRequest testRequest){
    }

}
