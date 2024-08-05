package com.boardgo.common.constant;

import org.springframework.beans.factory.annotation.Value;

public class HeaderConstant {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String API_VERSION_HEADER = "X-API-Version";
    public static final String API_VERSION_HEADER1 = "X-API-Version=1";

    @Value("${spring.dev.host}")
    public static String HOST;

    @Value("${spring.dev.port}")
    public static String PORT;
}
