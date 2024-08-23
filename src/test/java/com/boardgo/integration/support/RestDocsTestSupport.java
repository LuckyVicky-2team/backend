package com.boardgo.integration.support;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.snippet.Attributes.Attribute;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestExecutionListeners(
        value = {AcceptanceTestExecutionListener.class},
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@ActiveProfiles("test")
public abstract class RestDocsTestSupport {
    @Value("${spring.jwt.test-token}")
    protected String testAccessToken;

    protected RequestSpecification spec;

    @LocalServerPort protected int port;

    // FIXME: 접근제어자 private 로 변경하고 커스텀한 writeValueAsString 메소드 사용하도록 변경하는게 어떤가요?
    protected static ObjectMapper objectMapper = new ObjectMapper();

    protected static Attribute constraints(final String value) {
        return new Attribute("constraints", value);
    }

    protected static String writeValueAsString(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException je) {
        }
        return null;
    }

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        objectMapper.registerModule(new JavaTimeModule());
        this.spec =
                new RequestSpecBuilder()
                        .addFilter(
                                documentationConfiguration(restDocumentation)
                                        .operationPreprocessors()
                                        .withRequestDefaults(
                                                modifyUris().host("54.180.60.122").port(8080),
                                                // 여기도 포트를 설정
                                                modifyHeaders()
                                                        .remove("Content-Length")
                                                        .remove("Host"),
                                                prettyPrint())
                                        .withResponseDefaults(
                                                modifyHeaders()
                                                        .remove("Content-Length")
                                                        .remove("X-Content-Type-Options")
                                                        .remove("X-XSS-Protection")
                                                        .remove("Cache-Control")
                                                        .remove("Pragma")
                                                        .remove("Expires")
                                                        .remove("X-Frame-Options")
                                                        .remove("Transfer-Encoding"),
                                                prettyPrint()))
                        .build();
        RestAssured.port = port; // RestAssured에 포트를 설정
    }
}
