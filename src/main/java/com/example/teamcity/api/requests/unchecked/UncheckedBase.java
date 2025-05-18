package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import com.example.teamcity.api.requests.SearchInterface;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UncheckedBase extends Request implements CrudInterface, SearchInterface {

    public UncheckedBase(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
    }

    @Step("Create entity")
    @Override
    public Response create(BaseModel model) {
        return RestAssured
                   .given()
                   .spec(spec)
                   .body(model)
                   .post(endpoint.getUrl());
    }

    @Step("Read data")
    @Override
    public Response read(String locator) {
        return RestAssured
                   .given()
                   .spec(spec)
                   .get(endpoint.getUrl() + "/" + locator);
    }

    @Step("Update entity")
    @Override
    public Response update(String locator, BaseModel model) {
        return RestAssured
                   .given()
                   .spec(spec)
                   .body(model)
                   .put(endpoint.getUrl() + "/" + locator);
    }

    @Step("Delete entity")
    @Override
    public Response delete(String locator) {

        return RestAssured
                   .given()
                   .spec(spec)
                   .delete(endpoint.getUrl() + "/" + "id:" + locator);
    }

    @Step("Search entity")
    public Response search(String searchField) {
        return RestAssured
                   .given()
                   .spec(spec)
                   .get(endpoint.getUrl() + "/" + searchField);
    }
}