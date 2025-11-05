package ua.com.vladyslav.spribe.api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import ua.com.vladyslav.spribe.logging.RestAssuredLogFilter;

public class BaseApiClient {
    protected final RequestSpecification spec;

    public BaseApiClient(String baseUri, String basePath) {
        this.spec = new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setBasePath(basePath)
                .addFilter(new RestAssuredLogFilter())
                .addFilter(new AllureRestAssured())
                .setContentType(ContentType.JSON)
                .build();
    }
}
