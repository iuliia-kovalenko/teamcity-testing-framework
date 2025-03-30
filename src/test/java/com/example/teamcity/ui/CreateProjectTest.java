package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.ui.pages.ProjectPage;
import com.example.teamcity.ui.pages.ProjectsPage;
import com.example.teamcity.ui.pages.admin.CreateProjectPage;
import org.testng.annotations.Test;

import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class CreateProjectTest extends BaseUiTest {
    private static final String REPO_URL = "https://github.com/iuliia-kovalenko/teamcity-testing-framework";

    @Test(description = "User should be able to create project", groups = {"Positive"})
    public void userCreatesProject() {

        loginAs(testData.getUser());

        CreateProjectPage.open("_Root")
            .createForm(REPO_URL)
            .setUpProject(testData.getProject().getName(), testData.getBuildType().getName());


        // Проверка состояния API
        // Корректность отправки данных с UI на API
        step("Check that all entities: project, buildType were successfully created with correct data on API level");

        var createdProject = superUserCheckRequests.<Project>getRequest(Endpoint.PROJECTS).read("name:" + testData.getProject().getName());
        softy.assertNotNull(createdProject);

        ///  API: project with name, buildType for this Project with BuildType name
        // Проверка состояния UI
        // Корректность считывания данных и отображение данных на UI
        step("Check that project is visible on Project page (http://localhost:8112/favorite/projects)");

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
        step("Login as user");
        step("Check number of projects");

        // взаимодействие с UI
        step("Open 'Create project page' (http://localhost:8112/admin/createObjectMenu.html)");
        step("Send all project parameters (repository URL)");
        step("Click 'Proceed'");
        step("Set Project Name value is empty");
        step("Click 'Proceed'");

        // Проверка состояния API
        // Корректность отправки данных с UI на API
        step("Check that amount of projects did not change");


        // Проверка состояния UI
        // Корректность считывания данных и отображение данных на UI
        step("Check that error appears 'Project name must not be empty'");
    }
}