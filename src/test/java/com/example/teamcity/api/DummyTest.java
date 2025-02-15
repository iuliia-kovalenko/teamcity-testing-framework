package com.example.teamcity.api;

import com.example.teamcity.api.models.User;
import com.example.teamcity.api.spec.Specifications;
import org.testng.annotations.Test;
import io.restassured.RestAssured;


public class DummyTest extends BaseApiTest {
    @Test
    public void userShouldBeAbleGetAllProjects() {
        RestAssured
            .given()
            .spec(Specifications
                      .authSpec(User.builder()
                                    .username("admin").password("admin")
                                    .build()))
            .get("/app/rest/projects/");
    }

}
