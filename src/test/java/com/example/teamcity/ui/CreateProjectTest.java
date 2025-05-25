package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.ui.pages.ProjectPage;
import com.example.teamcity.ui.pages.ProjectsPage;
import com.example.teamcity.ui.pages.admin.CreateProjectPage;
import org.testng.annotations.Test;

@Test(groups = {"Regression"})
public class CreateProjectTest extends BaseUiTest {
    private static final String REPO_URL = "https://github.com/iuliia-kovalenko/teamcity-testing-framework";

    @Test(description = "User should be able to create project", groups = {"Positive"})
    public void userCreatesProject() {
        // Подготовка окружения
        loginAs(testData.getUser());

        // взаимодействие с UI
        CreateProjectPage.open("_Root")
            .createForm(REPO_URL)
            .setUpProject(testData.getProject().getName(), testData.getBuildType().getName());


        // Проверка состояния API
        // Корректность отправки данных с UI на API
        var createdProject = superUserCheckRequests.<Project>getRequest(Endpoint.PROJECTS).read("name:" + testData.getProject().getName());
        softy.assertNotNull(createdProject);

        // Проверка состояния UI
        // Корректность считывания данных и отображение данных на UI
        ProjectPage.open(createdProject.getId())
            .title.shouldHave(Condition.exactText(testData.getProject().getName()));

        var projectExists = ProjectsPage.open().getProjects()
                                .stream()
                                .anyMatch(project -> project.getName().text().equals(testData.getProject().getName()));

        softy.assertTrue(projectExists);
    }

    @Test(description = "User should not be able to create Project without name", groups = {"Negative"})
    public void userCreatesProjectWithoutName() {
        // подготовка окружения
        loginAs(testData.getUser());
        var projectsBefore = superUserCheckRequests.<Project>getRequest(Endpoint.PROJECTS).read("");
        String projectsCountBefore = projectsBefore.getCount();

        // взаимодействие с UI
        String errorMessage = CreateProjectPage.open("_Root")
                                  .createForm(REPO_URL)
                                  .setUpProject("", testData.getBuildType().getName())
                                  .getEmptyProjectNameValidationErrorMessage();

        // Проверка состояния UI
        // Корректность считывания данных и отображение данных на UI
        ValidateElement.containsText(errorMessage, ValidateElement.UiErrors.PROJECT_NAME_MUST_NOT_BE_EMPTY);

        // Проверка состояния API
        // Корректность отправки данных с UI на API
        var projectsAfter = superUserCheckRequests.<Project>getRequest(Endpoint.PROJECTS).read("");
        String projectsCountAfter = projectsAfter.getCount();
        softy.assertEquals(projectsCountAfter, projectsCountBefore, "The quantity After is not equal the quantity before");
    }
}