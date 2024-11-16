package com.boardgo.domain.termsconditions.service;

import com.boardgo.domain.termsconditions.entity.UserTermsConditionsEntity;
import com.boardgo.domain.termsconditions.entity.enums.TermsConditionsType;
import com.boardgo.domain.termsconditions.repository.UserTermsConditionsRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserTermsConditionsCommandServiceV1 implements UserTermsConditionsCommandUseCase {
    private final UserTermsConditionsRepository userTermsConditionsRepository;

    @Override
    public void createAll(List<UserTermsConditionsEntity> userTermsConditionsEntities) {
        userTermsConditionsRepository.saveAll(userTermsConditionsEntities);
    }

    @Override
    public void updatePushTermsCondition(Long userId) {
        UserTermsConditionsEntity userTermsConditionsEntity =
                userTermsConditionsRepository.findByUserInfoIdAndTermsConditionsType(
                        userId, TermsConditionsType.PUSH);
        if (!userTermsConditionsEntity.getAgreement()) {
            userTermsConditionsEntity.updateAgreement(Boolean.TRUE);
            // FIXME 조건에 들어오지만 변경감지 안됨 > 별도의 쓰레드 풀에서 관리하기 때문에 트랜잭션을 분리했더니 변경됨 그럼 서로 다른 쓰레드에서 트랜잭션을
            // 2개를 실행한건가?
        }
    }

    @Override
    public void updatePushTermsCondition(Long userId, boolean flag) {
        UserTermsConditionsEntity userTermsConditionsEntity =
                userTermsConditionsRepository.findByUserInfoIdAndTermsConditionsType(
                        userId, TermsConditionsType.PUSH);
        // 회원의 모든 알림설정이 N 이라면 푸시약관동의 N 변경
        if (flag) {
            userTermsConditionsEntity.updateAgreement(Boolean.FALSE);
        }
    }
}
