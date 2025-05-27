package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.api.spec.ValidationResponseSpecifications;
import com.example.teamcity.ui.pages.BuildTypePage;
import com.example.teamcity.ui.pages.ProjectPage;
import com.example.teamcity.ui.pages.admin.CreateBuildTypePage;
import org.testng.annotations.Test;

import static com.example.teamcity.api.enums.Endpoint.BUILD_TYPES;
import static com.example.teamcity.api.enums.Endpoint.PROJECTS;

@Test(groups = {"Regression"})
public class CreateBuildTypeTest extends BaseUiTest {
    private static final String REPO_URL = "https://github.com/iuliia-kovalenko/teamcity-testing-framework";
    private static final String INVALID_REPO_URL = "github.com/iuliia-kovalenko/teamcity-testing-framework";

    @Test(description = "User should be able to create buildType", groups = {"Positive"})
    public void userCreatesBuildType() {
        loginAs(testData.getUser());

        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        var createdProject = userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        CreateBuildTypePage.open(testData.getProject().getId())
            .createForm(REPO_URL)
            .inputBuildTypeName(testData.getBuildType().getName())
            .submitBuildType(true);

        // Проверка состояния API
        // Корректность отправки данных с UI на API
        var createdBuildType = superUserCheckRequests.<BuildType>getRequest(BUILD_TYPES).read(testData.getBuildType().getName());
        softy.assertNotNull(createdBuildType);

        // Проверка состояния UI
        // Корректность считывания данных и отображение данных на UI
        BuildTypePage.open(createdProject.getId(), createdBuildType.getName())
            .title.shouldHave(Condition.exactText(testData.getBuildType().getName()));

        var buildTypeExists = ProjectPage.open(createdProject.getId()).getBuildTypes()
                                  .stream()
                                  .anyMatch(buildType -> buildType.getName().text().equals(testData.getBuildType().getName()));

        softy.assertTrue(buildTypeExists);
    }

    @Test(description = "User should not be able to create buildType with incorrect repo url", groups = {"Negative"})
    public void userCreatesBuildTypeWithInvalidRepoUrl() {
        loginAs(testData.getUser());

        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        var createdProject = userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        var errorMessage = CreateBuildTypePage.open(testData.getProject().getId())
                               .inputRepoUrl(INVALID_REPO_URL)
                               .submitBuildType(false)
                               .getRepoUrlValidationErrorMessage();
//        softy.assertTrue(errorMessage.contains("git -c credential.helper= ls-remote origin command failed.\n" +
//                                                   "exit code: 128"));

        ValidateElement.containsText(errorMessage, ValidateElement.UiErrors.BUILD_CONFIG_REPO_URL_MUST_BE_CORRECT, INVALID_REPO_URL);

        new UncheckedBase(Specifications.authSpec(testData.getUser()), BUILD_TYPES)
            .read("project:" + createdProject.getId())
            .then().spec(ValidationResponseSpecifications.checkNoBuildTypeFound(createdProject.getId()));
    }

    @Test(description = "User should not be able to create buildType with empty repo url", groups = {"Negative"})
    public void userCreatesBuildTypeWithEmptyRepoUrl() {
        loginAs(testData.getUser());

        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        var createdProject = userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        var errorMessage = CreateBuildTypePage.open(testData.getProject().getId())
                               .submitBuildType(false)
                               .getRepoUrlValidationErrorMessage();

//        softy.assertEquals(errorMessage, "URL must not be empty");
        ValidateElement.byText(errorMessage, ValidateElement.UiErrors.BUILD_CONFIG_REPO_URL_MUST_BE_NOT_NULL);


        new UncheckedBase(Specifications.authSpec(testData.getUser()), BUILD_TYPES)
            .read("project:" + createdProject.getId())
            .then().spec(ValidationResponseSpecifications.checkNoBuildTypeFound(createdProject.getId()));
    }
}