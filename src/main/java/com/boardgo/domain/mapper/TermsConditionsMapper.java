package com.boardgo.domain.mapper;

import com.boardgo.domain.termsconditions.controller.request.TermsConditionsCreateRequest;
import com.boardgo.domain.termsconditions.entity.UserTermsConditionsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TermsConditionsMapper {
    TermsConditionsMapper INSTANCE = Mappers.getMapper(TermsConditionsMapper.class);

    UserTermsConditionsEntity toUserTermsConditionsEntity(TermsConditionsCreateRequest request);
}
