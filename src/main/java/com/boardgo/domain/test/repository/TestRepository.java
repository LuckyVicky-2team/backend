package com.boardgo.domain.test.repository;


import com.boardgo.domain.test.entity.TestEntity;

public interface TestRepository {
    void save(TestEntity testEntity);
    TestEntity selectAll();
}
