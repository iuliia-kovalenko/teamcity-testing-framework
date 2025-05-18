package com.example.teamcity.api;

import com.example.teamcity.api.enums.RoleType;
import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.models.*;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.api.spec.ValidationResponseSpecifications;
import org.testng.annotations.Test;

import java.util.List;

import static com.example.teamcity.api.enums.Endpoint.PROJECTS;
import static com.example.teamcity.api.enums.Endpoint.USERS;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;
import static org.testng.Assert.assertEquals;

@Test(groups = {"Regression"})
public class ProjectSearchTest extends BaseApiTest {

    @Test(description = "SuperUser should be able to find created project by name", groups = {"Positive", "Search"})
    public void superUserSearchProjectTest() {
        TestData testData1 = generate();
        TestData testData2 = generate();
        TestData testData3 = generate();
        Project project1 = superUserCheckRequests.<Project>getRequest(PROJECTS).create(testData1.getProject());
        Project project2 = superUserCheckRequests.<Project>getRequest(PROJECTS).create(testData2.getProject());
        Project project3 = superUserCheckRequests.<Project>getRequest(PROJECTS).create(testData3.getProject());

        var foundProject = superUserCheckRequests.<Project>getRequest(PROJECTS).search("name" + ":" + project1.getName());

        softy.assertEquals(project1, foundProject, "Project was not found by name");
    }

    @Test(description = "System Admin should be able to find created project by name", groups = {"Positive", "Search"})
    public void userSearchProjectTest() {
        TestData testData1 = generate();
        TestData testData2 = generate();
        TestData testData3 = generate();
        superUserCheckRequests.getRequest(USERS).create(testData1.getUser());

        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData1.getUser()));

        Project project1 = userCheckRequests.<Project>getRequest(PROJECTS).create(testData1.getProject());
        userCheckRequests.getRequest(PROJECTS).create(testData2.getProject());
        userCheckRequests.getRequest(PROJECTS).create(testData3.getProject());

        var foundProject = userCheckRequests.<Project>getRequest(PROJECTS).search("name" + ":" + project1.getName());

        softy.assertEquals(project1, foundProject, "Project was not found by name");
    }

    @Test(description = "Project Admin should be able to find their projects", groups = {"Positive", "Search"})
    public void projectAdminSearchProjectTest() {
        TestData testData1 = generate();
        TestData testData2 = generate();
        Project project1 = testData1.getProject();
        Project project2 = testData2.getProject();

        Role projectAdmin1 = Role.builder().roleId(RoleType.PROJECT_ADMIN.getRoleId()).scope("p:" + project1.getId()).build();
        Role projectAdmin2 = Role.builder().roleId(RoleType.PROJECT_ADMIN.getRoleId()).scope("p:" + project2.getId()).build();

        User user = User.builder()
                        .roles(Roles.generateRoles(projectAdmin1, projectAdmin2))
                        .username(testData1.getUser().getUsername())
                        .password(testData1.getUser().getPassword())
                        .build();

        superUserCheckRequests.getRequest(PROJECTS).create(project1);
        superUserCheckRequests.getRequest(PROJECTS).create(project2);

        superUserCheckRequests.getRequest(USERS).create(user);

        var userCheckRequests = new CheckedRequests(Specifications.authSpec(user));

        var foundProject1 = userCheckRequests.<Project>getRequest(PROJECTS)
                                .search("name:" + project1.getName());
        var foundProject2 = userCheckRequests.<Project>getRequest(PROJECTS)
                                .search("name:" + project2.getName());

        assertEquals(foundProject1, project1, "Project was not found by name");
        assertEquals(foundProject2, project2, "Project was not found by name");

        softy.assertAll();
    }

    @Test(description = "SuperUser should not be able to find non existing projects", groups = {"Negative", "Search"})
    public void superUserSearchNonExistingProjectTest() {

        var searchQuery = "name:" +  RandomData.getString(12);
        new UncheckedBase(Specifications.superUserSpec(), PROJECTS)
            .search(searchQuery)
            .then().spec(ValidationResponseSpecifications.checkSearchProject(searchQuery));
    }
}