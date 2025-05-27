package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class CreateBuildTypePage extends CreateBasePage {
    private static final String PROJECT_SHOW_MODE = "createBuildTypeMenu";
    protected SelenideElement buildTypeNameInput = $("#buildTypeName");
    protected SelenideElement buildTypeRepoUrlInput = $("#url");
    protected SelenideElement repoUrlValidationError = $("#error_url");
    protected SelenideElement successMessage = $("#unprocessed_objectsCreated");


    @Step("Open Create build type page")
    public static CreateBuildTypePage open(String projectId) {
        return Selenide.open(CREATE_URL.formatted(projectId, PROJECT_SHOW_MODE), CreateBuildTypePage.class);
    }

    @Step("Input RepoURL, Submit button")
    public CreateBuildTypePage createForm(String url) {
        baseCreateForm(url);
        return this;
    }

    @Step("Input build type name")
    public CreateBuildTypePage inputBuildTypeName(String buildTypeName) {
        buildTypeNameInput.val(buildTypeName);
        return this;
    }

    @Step("Click submit button")
    public CreateBuildTypePage submitBuildType() {
        submitButton.click();
        successMessage.should(Condition.visible, BASE_WAITING);
        return this;
    }

    @Step("Input Repo Url")
    public CreateBuildTypePage inputRepoUrl(String repoUrl) {
        buildTypeRepoUrlInput.val(repoUrl);
        return this;
    }

    @Step("Get repo url validation message")
    public String getRepoUrlValidationErrorMessage() {
        repoUrlValidationError.should(Condition.visible, BASE_WAITING);
        return repoUrlValidationError.text();
    }
}