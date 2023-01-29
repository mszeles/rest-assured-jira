package com.mszeles.rest_assured_jira;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import java.io.IOException;
import java.util.Properties;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ProjectTest {

    private static final Properties config = new Properties();

    @BeforeClass
    public void beforeClass() throws IOException {
        config.load(ProjectTest.class.getClassLoader().getResourceAsStream("config.properties"));
        RestAssured.baseURI = config.getProperty("jiraURI");
        RestAssured.basePath = "/rest/api/2";
        PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
        authScheme.setUserName(config.getProperty("username"));
        authScheme.setPassword(config.getProperty("password"));
        RestAssured.authentication = authScheme;
    }

    @Test
    public void getAllProjects() {
        given().log().uri().queryParam("expand", "description").when().get("/project").then().log().body().assertThat().statusCode(200);
    }

    @Test
    public void getProjectByKey() {
        given().log().uri().pathParam("projectKey", "RAPP").when().get("/project/{projectKey}").then().log().body().assertThat().statusCode(200);
    }
}
