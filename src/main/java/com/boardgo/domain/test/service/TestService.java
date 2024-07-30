package com.boardgo.domain.test.service;

import com.boardgo.domain.test.dto.TestRequest;
import com.boardgo.domain.test.dto.TestResponse;
import com.boardgo.domain.test.entity.TestEntity;
import com.boardgo.domain.test.repository.TestDslRepository;
import com.boardgo.domain.test.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;
    private final TestDslRepository testDslRepository;

    void save(TestEntity testEntity){
        testRepository.save(testEntity);
    }

    TestEntity selectAll(){
        return testRepository.selectAll();
    }

    void saveDsl(TestRequest testRequest){   //불가
        testDslRepository.save(testRequest);
    }

    TestResponse selectAllDsl(){ //불가
        return new TestResponse();
    }
}
