package com.boardgo.domain.test.repository;

import com.boardgo.domain.test.dto.TestDto;
import com.boardgo.domain.test.dto.TestRequest;

public class TestDslRepository {

    public void save(TestRequest testRequest) {}  //불가

    public TestDto selectAll() {    //불가
        return new TestDto();
    }
}
