package com.boardgo.domain.user.repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;

@Qualifier("UserPrTagJdbcRepository")
public interface UserPrTagJdbcRepository {
    void bulkInsertPrTags(List<String> prTags, Long userInfoId);
}
