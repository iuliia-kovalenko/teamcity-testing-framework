package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class CreateProjectPage extends CreateBasePage {
    private static final String PROJECT_SHOW_MODE = "createProjectMenu";
    private SelenideElement projectNameInput = $("#projectName");
    private SelenideElement refreshButton = $("#updateDiscoveryContainer");


    public static CreateProjectPage open(String projectId) {

        return Selenide.open(CREATE_URL.formatted(projectId, PROJECT_SHOW_MODE), CreateProjectPage.class);
    }

    public CreateProjectPage createForm(String url) {
        baseCreateForm(url);
        return this;
    }

    public void setUpProject(String projectName, String buildTypeName) {
        projectNameInput.val(projectName);
        buildtypeNameInput.val(buildTypeName);
        submitButton.click();
        refreshButton.shouldBe(Condition.visible, BASE_WAITING);

//        return page(ProjectsPage.class);
    }
}