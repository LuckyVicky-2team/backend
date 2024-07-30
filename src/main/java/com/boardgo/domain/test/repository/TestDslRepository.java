package com.boardgo.domain.test.repository;

import com.boardgo.domain.test.dto.TestDto;
import com.boardgo.domain.test.entity.TestEntity;

public class TestDslRepository {

    public void save(TestEntity testEntity) {}

    public TestDto selectAll() {    //불가
        return new TestDto();
    }
}
