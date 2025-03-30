package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class CreateBuildTypePage extends CreateBasePage {
    private static final String PROJECT_SHOW_MODE = "createBuildTypeMenu";
    protected SelenideElement buildTypeNameInput = $("#buildTypeName");
    protected SelenideElement buildTypeRepoUrlInput = $("#url");
    protected SelenideElement repoUrlValidationError = $("#error_url");


    public static CreateBuildTypePage open(String projectId) {
        return Selenide.open(CREATE_URL.formatted(projectId, PROJECT_SHOW_MODE), CreateBuildTypePage.class);
    }

    public CreateBuildTypePage createForm(String url) {
        baseCreateForm(url);
        return this;
    }

    public CreateBuildTypePage inputBuildTypeName(String buildTypeName) {
        buildTypeNameInput.val(buildTypeName);
        return this;

    }

    public CreateBuildTypePage submitBuildType() {
        submitButton.click();
        return this;
    }

    public CreateBuildTypePage inputRepoUrl(String repoUrl) {
        buildTypeRepoUrlInput.val(repoUrl);
        return this;
    }

    public String getRepoUrlValidationErrorMessage() {
        repoUrlValidationError.should(Condition.visible, BASE_WAITING);
        return repoUrlValidationError.text();
    }
}