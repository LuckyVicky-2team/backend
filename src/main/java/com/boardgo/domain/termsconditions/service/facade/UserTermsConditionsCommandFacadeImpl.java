package com.boardgo.domain.termsconditions.service.facade;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import com.boardgo.common.exception.CustomIllegalArgumentException;
import com.boardgo.domain.mapper.TermsConditionsMapper;
import com.boardgo.domain.termsconditions.controller.request.TermsConditionsCreateRequest;
import com.boardgo.domain.termsconditions.entity.TermsConditionsEntity;
import com.boardgo.domain.termsconditions.entity.UserTermsConditionsEntity;
import com.boardgo.domain.termsconditions.service.TermsConditionsQueryUseCase;
import com.boardgo.domain.termsconditions.service.UserTermsConditionsCommandUseCase;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserTermsConditionsCommandFacadeImpl implements UserTermsConditionsCommandFacade {
    private final UserTermsConditionsCommandUseCase userTermsConditionsCommandUseCase;
    private final TermsConditionsQueryUseCase termsConditionsQueryUseCase;
    private final TermsConditionsMapper termsConditionsMapper;

    @Override
    public void createUserTermsConditions(
            List<TermsConditionsCreateRequest> termsConditionsCreateRequest, Long userId) {
        List<UserTermsConditionsEntity> userTermsConditionsEntities = new ArrayList<>();

        List<TermsConditionsEntity> termsConditionsEntities =
                termsConditionsQueryUseCase.getTermsConditions(List.of(TRUE, FALSE));
        if (termsConditionsEntities.size() != termsConditionsCreateRequest.size()) {
            throw new CustomIllegalArgumentException("약관동의 항목의 갯수가 일치하지 않습니다");
        }

        Map<String, TermsConditionsEntity> termsConditionsMap =
                termsConditionsEntities.stream()
                        .collect(
                                Collectors.toMap(
                                        termsConditionsEntity ->
                                                termsConditionsEntity.getType().name(),
                                        termsConditionsEntity -> termsConditionsEntity));

        termsConditionsCreateRequest.forEach(
                termsConditions -> {
                    TermsConditionsEntity termsConditionsEntity =
                            termsConditionsMap.get(termsConditions.termsConditionsType());
                    if (termsConditionsEntity.isRequired() && !termsConditions.agreement()) {
                        throw new CustomIllegalArgumentException("약관동의 필수항목은 필수로 선택되어야 합니다");
                    }

                    userTermsConditionsEntities.add(
                            termsConditionsMapper.toUserTermsConditionsEntity(
                                    termsConditionsEntity, termsConditions.agreement(), userId));
                });
        userTermsConditionsCommandUseCase.createAll(userTermsConditionsEntities);
    }
}
