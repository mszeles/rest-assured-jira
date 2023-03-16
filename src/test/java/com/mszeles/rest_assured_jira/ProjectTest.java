package com.mszeles.rest_assured_jira;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import java.io.IOException;
import java.util.Properties;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ProjectTest {

    private static final Properties config = new Properties();
    public static final String PROJECT_KEY = "RAPP";

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
        given().pathParam("projectKey", PROJECT_KEY)
                .when().get("/project/{projectKey}")
                .then().assertThat().statusCode(200);
    }

    @Test
    public void assertionsOnHeader() {
        // Simple header assertion
        given().pathParam("projectKey", PROJECT_KEY)
                .when().get("/project/{projectKey}")
                .then().header("Content-Type", "application/json;charset=UTF-8");

        //Hamcrest assertion - equalTo
        given().pathParam("projectKey", PROJECT_KEY)
                .when().get("/project/{projectKey}")
                .then().header("Content-Type", equalTo("application/json;charset=UTF-8"));

        //Hamcrest assertion - equalToIgnoringCase
        given().pathParam("projectKey", PROJECT_KEY)
                .when().get("/project/{projectKey}")
                .then().header("Content-Type", equalToIgnoringCase("APPLICATION/JSON;CHARSET=UTF-8"));

        //Hamcrest assertion - startsWith
        given().pathParam("projectKey", PROJECT_KEY)
                .when().get("/project/{projectKey}")
                .then().header("Content-Type", startsWith("application/json"));

        //Hamcrest assertion - contains
        given().pathParam("projectKey", PROJECT_KEY)
                .when().get("/project/{projectKey}")
                .then().header("Content-Type", containsString("application/json"));

        //Hamcrest assertion - endsWith
        given().pathParam("projectKey", PROJECT_KEY)
                .when().get("/project/{projectKey}")
                .then().header("Content-Type", endsWith("charset=UTF-8"));

        //Hamcrest assertion - emptyOrNullString
        given().pathParam("projectKey", PROJECT_KEY)
                .when().get("/project/{projectKey}")
                .then().header("NonExistentHeader", emptyOrNullString());

        //Hamcrest assertion - is decorator
        given().pathParam("projectKey", PROJECT_KEY)
                .when().get("/project/{projectKey}")
                .then().header("NonExistentHeader", is(emptyOrNullString()));

        //Hamcrest assertion - not operator
        given().pathParam("projectKey", PROJECT_KEY)
                .when().get("/project/{projectKey}")
                .then().header("Content-Type", not(emptyOrNullString()));

        //Combining assertions with REST Assured
        given().pathParam("projectKey", PROJECT_KEY)
                .when().get("/project/{projectKey}")
                .then().header("Content-Type", containsString("application/json"))
                .header("Content-Type", containsString("charset=UTF-8"));

        //Combining assertions with Hamcrest allOf
        given().pathParam("projectKey", PROJECT_KEY)
                .when().get("/project/{projectKey}")
                .then().header("Content-Type", allOf(containsString("application/json"), containsString("charset=UTF-8")));

        //Combining assertions with Hamcrest anyOf
        given().pathParam("projectKey", PROJECT_KEY)
                .when().get("/project/{projectKey}")
                .then().header("Content-Type", anyOf(containsString("application/json"), containsString("application/xml")));
    }

    @Test
    public void assertionsOnBody() {
        given().pathParam("projectKey", PROJECT_KEY)
                .when().get("/project/{projectKey}")
                .then().body("name", equalTo("REST Assured Practice Project"));

        given().pathParam("projectKey", PROJECT_KEY)
                .when().get("/project/{projectKey}")
                .then().body("avatarUrls.48x48", startsWith(config.getProperty("jiraURI")));

        given().pathParam("projectKey", PROJECT_KEY)
                .when().get("/project/{projectKey}")
                .then().body("issueTypes[0].name", equalTo("Task"));

        given().pathParam("projectKey", PROJECT_KEY)
                .when().get("/project/{projectKey}")
                .then().body("issueTypes.size()", equalTo(3));

        given().pathParam("projectKey", PROJECT_KEY)
                .when().get("/project/{projectKey}")
                .then().body("issueTypes[0].avatarId", greaterThan(10000));

        given().pathParam("projectKey", PROJECT_KEY)
                .when().get("/project/{projectKey}")
                .then().body("issueTypes.name", hasItem("Epic"));

        given().pathParam("projectKey", PROJECT_KEY)
                .when().get("/project/{projectKey}")
                .then().body("issueTypes.name", hasItems("Epic", "Task", "Subtask"));

        given().pathParam("projectKey", PROJECT_KEY)
                .when().get("/project/{projectKey}")
                .then().body("roles", hasKey("Administrator"));

        given().pathParam("projectKey", PROJECT_KEY)
                .when().get("/project/{projectKey}")
                .then().body("issueTypes[2]", hasValue("Subtask"));

        given().pathParam("projectKey", PROJECT_KEY)
                .when().get("/project/{projectKey}")
                .then().body("", hasEntry("projectTypeKey", "software"));

    }

}
