package com.example.teamcity.api.requests.checked;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.generators.TestDataStorage;
import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import com.example.teamcity.api.requests.SearchInterface;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

@SuppressWarnings("unchecked")
public final class CheckedBase<T extends BaseModel> extends Request implements CrudInterface, SearchInterface {
    private final UncheckedBase uncheckedBase;

    public CheckedBase(RequestSpecification spec, Endpoint endpoint) {
        super(spec, endpoint);
        this.uncheckedBase = new UncheckedBase(spec, endpoint);
    }

    //    @Step("Create entity")
    @Override
    public T create(BaseModel model) {
        var createdModel = (T) uncheckedBase
                                   .create(model)
                                   .then()
                                   .assertThat()
                                   .statusCode(HttpStatus.SC_OK)
                                   .extract().as(endpoint.getModelClass());

        TestDataStorage.getStorage().addCreatedEntity(endpoint, createdModel);
        return createdModel;
    }

    //    @Step("Read data")
    @Override
    public T read(String id) {
        return (T) uncheckedBase
                       .read(id)
                       .then()
                       .assertThat()
                       .statusCode(HttpStatus.SC_OK)
                       .extract().as(endpoint.getModelClass());
    }

    //    @Step("Update entity")
    @Override
    public T update(String id, BaseModel model) {
        System.out.println("Обновляемый объект: " + model);
        return (T) uncheckedBase
                       .update(id, model)
                       .then()
                       .assertThat()
                       .statusCode(HttpStatus.SC_OK)
                       .extract().as(endpoint.getModelClass());
    }

    //    @Step("Delete entity")
    @Override
    public Object delete(String id) {
        return uncheckedBase
                   .delete(id)
                   .then()
                   .assertThat()
                   .statusCode(HttpStatus.SC_OK)
                   .extract().asString();
    }

    @Override
    public T search(String searchField) {
        return (T) uncheckedBase
                       .search(searchField)
                       .then()
                       .assertThat()
                       .statusCode(HttpStatus.SC_OK)
                       .extract().as(endpoint.getModelClass());
    }
}