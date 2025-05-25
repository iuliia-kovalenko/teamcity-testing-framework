package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class CreateProjectPage extends CreateBasePage {
    private static final String PROJECT_SHOW_MODE = "createProjectMenu";
    private SelenideElement projectNameInput = $("#projectName");
    private SelenideElement refreshButton = $("#updateDiscoveryContainer");
    private SelenideElement emptyProjectNameErrorMessage = $(".error#error_projectName");

    @Step("Open Create project page")
    public static CreateProjectPage open(String projectId) {

        return Selenide.open(CREATE_URL.formatted(projectId, PROJECT_SHOW_MODE), CreateProjectPage.class);
    }

    @Step("Input RepoURL, Submit button")
    public CreateProjectPage createForm(String url) {
        baseCreateForm(url);
        return this;
    }

    @Step("Input project name, input build type name, click submit button")
    public CreateProjectPage setUpProject(String projectName, String buildTypeName) {
        projectNameInput.val(projectName);
        buildtypeNameInput.val(buildTypeName);
        submitButton.click();
//        refreshButton.shouldBe(Condition.visible, BASE_WAITING);

//        return page(ProjectsPage.class);
        return this;
    }

    @Step("Get empty project name validation message")
    public String getEmptyProjectNameValidationErrorMessage() {
        emptyProjectNameErrorMessage.should(Condition.visible, BASE_WAITING);
        return emptyProjectNameErrorMessage.text();
    }
}