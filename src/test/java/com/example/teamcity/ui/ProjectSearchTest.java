package com.example.teamcity.ui;

import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.TestData;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.ui.pages.ProjectsPage;
import org.testng.annotations.Test;

import static com.example.teamcity.api.enums.Endpoint.PROJECTS;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;


@Test(groups = {"Regression"})
public class ProjectSearchTest extends BaseUiTest {

    @Test(description = "User should be able to find created project on the sideBar by name", groups = {"Positive", "Search"})
    public void searchExistingProjectByNameTest() {
        TestData testData1 = generate();
        TestData testData2 = generate();
        TestData testData3 = generate();

        loginAs(testData1.getUser());

        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData1.getUser()));

        Project project1 = userCheckRequests.<Project>getRequest(PROJECTS).create(testData1.getProject());
        userCheckRequests.getRequest(PROJECTS).create(testData2.getProject());
        userCheckRequests.getRequest(PROJECTS).create(testData3.getProject());

        var project = ProjectsPage.open().
                          inputProjectNameInToSearchField(testData1.getProject().getName())
                          .getSideBarElements()
                          .stream()
                          .anyMatch(sideBarElement -> sideBarElement.getName().text().equals(project1.getName()));

        softy.assertTrue(project);
    }

    @Test(description = "User should not be able to find non existing project on the sideBar", groups = {"Negative", "Search"})
    public void searchNonExistingProjectTest() {

        loginAs(testData.getUser());

        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));
        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        String nonExistingProjectName = RandomData.getString(30);

        String actualErrorMessage = ProjectsPage.open().
                                        inputProjectNameInToSearchField(nonExistingProjectName)
                                        .getNotFoundValidationErrorMessage();

        softy.assertEquals(actualErrorMessage, ValidateElement.UiErrors.PROJECT_NOT_FOUND.getMessage());
    }
}