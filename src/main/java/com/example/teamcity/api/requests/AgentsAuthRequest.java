package com.example.teamcity.api.requests;

import com.example.teamcity.api.enums.Endpoint;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import static com.example.teamcity.api.enums.Endpoint.AGENTS;

public class AgentsAuthRequest extends Request {


    public AgentsAuthRequest(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
    }
    @Step("Update Teamcity Agent")
    public String update(String id, String locator) {
        return RestAssured.given()
                   .spec(spec)
                   .header("Content-Type", "text/plain")
                   .header("Accept", "*/*")
                   .body("true")
                   .put(AGENTS.getUrl() + "/id:" + id + "/" + locator)
                   .then()
                   .assertThat()
                   .statusCode(HttpStatus.SC_OK)
                   .extract().asString();

    }
}