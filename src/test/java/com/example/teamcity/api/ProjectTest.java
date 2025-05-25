package com.example.teamcity.api;

import com.example.teamcity.api.enums.RoleType;
import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.models.*;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.api.spec.ValidationResponseSpecifications;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;

import static com.example.teamcity.api.enums.Endpoint.PROJECTS;
import static com.example.teamcity.api.enums.Endpoint.USERS;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;

@Test(groups = {"Regression"})
public class ProjectTest extends BaseApiTest {
    private static final int PROJECT_ID_LENGTH_LIMIT = 225;

    @Test(description = "System admin should be able to create project", groups = {"Positive", "Crud"})
    public void systemAdminCreatesProjectTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        var createdProject = userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        softy.assertEquals(createdProject.getId(), testData.getProject().getId());
    }

    @Test(description = "SuperUser should be able to create project", groups = {"Positive", "Crud"})
    public void superUserCreatesProjectTest() {
        var createdProject = superUserCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        softy.assertEquals(createdProject.getId(), testData.getProject().getId());
    }

    @Test(description = "System admin should not be able to create project with empty name", groups = {"Negative", "Crud"})
    public void systemAdminCreatesProjectWithEmptyNameTest() {
        superUserCheckRequests.<User>getRequest(USERS).create(testData.getUser());

        var projectWithEmptyName = testData.getProject();
        projectWithEmptyName.setName("");

        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
            .create(projectWithEmptyName)
            .then().spec(ValidationResponseSpecifications.checkProjectWithEmptyName(testData.getProject().getId()));

    }

    @Test(description = "SuperUser should not be able to create project with empty name", groups = {"Negative", "Crud"})
    public void superUserCreatesProjectWithEmptyNameTest() {
        var projectWithEmptyName = testData.getProject();
        projectWithEmptyName.setName("");

        new UncheckedBase(Specifications.superUserSpec(), PROJECTS)
            .create(projectWithEmptyName)
            .then().spec(ValidationResponseSpecifications.checkProjectWithEmptyName(testData.getProject().getId()));
    }

    @Test(description = "System admin should not be able to create project with empty ID", groups = {"Negative", "Crud"})
    public void systemAdminCreatesProjectWithEmptyIdTest() {
        superUserCheckRequests.<User>getRequest(USERS).create(testData.getUser());

        var project = testData.getProject();
        project.setId("");

        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
            .create(project)
            .then().spec(ValidationResponseSpecifications.checkProjectWithEmptyId(testData.getProject().getId()));
    }

    @Test(description = "SuperUser should not be able to create project with empty ID", groups = {"Negative", "Crud"})
    public void superUserCreatesProjectWithEmptyIdTest() {
        testData.getProject().setId("");

        new UncheckedBase(Specifications.superUserSpec(), PROJECTS)
            .create(testData.getProject())
            .then().spec(ValidationResponseSpecifications.checkProjectWithEmptyId(testData.getProject().getId()));
    }

    @Test(description = "System admin should not be able to create project with the same name", groups = {"Negative", "Crud"})
    public void systemAdminCreatesTwoProjectsWithTheSameINameTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());

        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        userCheckRequests.getRequest(PROJECTS).create(testData.getProject());

        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
            .create(testData.getProject())
            .then().spec(ValidationResponseSpecifications.checkProjectWithNameAlreadyExist(testData.getProject().getName()));
    }

    @Test(description = "SuperUser should not be able to create project with the same name", groups = {"Negative", "Crud"})
    public void superUserCreatesTwoProjectsWithTheSameNameTest() {
        superUserCheckRequests.getRequest(PROJECTS).create(testData.getProject());

        new UncheckedBase(Specifications.superUserSpec(), PROJECTS)
            .create(testData.getProject())
            .then().spec(ValidationResponseSpecifications.checkProjectWithNameAlreadyExist(testData.getProject().getName()));
    }

    @Test(description = "System admin should not be able to create project with the same ID", groups = {"Negative", "Crud"})
    public void systemAdminCreatesTwoProjectsWithTheSameIdTest() {
        TestData testData1 = generate();
        TestData testData2 = generate();
        var project1 = testData1.getProject();
        var project2 = testData2.getProject();
        project2.setId(project1.getId());

        superUserCheckRequests.getRequest(USERS).create(testData.getUser());

        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        userCheckRequests.getRequest(PROJECTS).create(project1);

        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
            .create(project2)
            .then().spec(ValidationResponseSpecifications.checkProjectWithIdAlreadyExist(project2.getId()));
    }

    @Test(description = "SuperUser should not be able to create project with the same ID", groups = {"Negative", "Crud"})
    public void superUserCreatesTwoProjectsWithTheSameIdTest() {
        TestData testData1 = generate();
        TestData testData2 = generate();
        var project1 = testData1.getProject();
        var project2 = testData2.getProject();
        project2.setId(project1.getId());

        superUserCheckRequests.getRequest(PROJECTS).create(project1);

        new UncheckedBase(Specifications.superUserSpec(), PROJECTS)
            .create(project2)
            .then().spec(ValidationResponseSpecifications.checkProjectWithIdAlreadyExist(project2.getId()));
    }


    @DataProvider(name = "InvalidProjectIdsStartsWithNonLetter")
    public static Object[] invalidProjectIds() {
        return new Object[]{
            "7_teamcity_test", "%_teamcity_test", "_teamcity_test"};
    }

    @Test(dataProvider = "InvalidProjectIdsStartsWithNonLetter",
        description = "SuperUser should not be able to create project, that starts with non letter character and with non latin letters",
        groups = {"Negative", "Crud"})
    public void superUserCreatesProjectWithInvalidCharactersTest(String invalidId) {
        var project = testData.getProject();
        project.setId(invalidId);

        new UncheckedBase(Specifications.superUserSpec(), PROJECTS)
            .create(project)
            .then().spec(ValidationResponseSpecifications.checkProjectStartsWithNonLetterCharacter(project.getId()));
    }

    @Test(dataProvider = "InvalidProjectIdsStartsWithNonLetter",
        description = "System Admin should not be able to create project, that starts with non letter character and with non latin letters",
        groups = {"Negative", "Crud"})
    public void systemAdminCreatesProjectWithInvalidCharactersTest(String invalidId) {
        var project = testData.getProject();
        project.setId(invalidId);

        superUserCheckRequests.getRequest(USERS).create(testData.getUser());

        new CheckedRequests(Specifications.authSpec(testData.getUser()));

        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
            .create(project)
            .then().spec(ValidationResponseSpecifications.checkProjectStartsWithNonLetterCharacter(project.getId()));
    }

    @Test(description = "SuperUser should not be able to create project with id, that contains non latin letters",
        groups = {"Negative", "Crud"})
    public void superUserCreatesProjectWithNonLatinLettersTest() {
        String invalidId = "кириллица";
        var project = testData.getProject();
        project.setId(invalidId);

        new UncheckedBase(Specifications.superUserSpec(), PROJECTS)
            .create(project)
            .then().spec(ValidationResponseSpecifications.checkProjectContainsOnlyLatinLettersAndUnderscores(project.getId()));
    }

    @Test(description = "System Admin should not be able to create project with id, that contains non latin letters",
        groups = {"Negative", "Crud"})
    public void systemAdminCreatesProjectWithNonLatinLettersTest() {
        String invalidId = "кириллица";
        var project = testData.getProject();
        project.setId(invalidId);

        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        new CheckedRequests(Specifications.authSpec(testData.getUser()));

        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
            .create(project)
            .then().spec(ValidationResponseSpecifications.checkProjectContainsOnlyLatinLettersAndUnderscores(project.getId()));
    }

    @Test(description = "SuperUser should not be able to create project with id length > 225",
        groups = {"Negative", "Crud"})
    public void superUserCreatesProjectWithLongIdLengthTest() {
        testData.getProject().setId(RandomData.getString(PROJECT_ID_LENGTH_LIMIT + 1));

        new UncheckedBase(Specifications.superUserSpec(), PROJECTS)
            .create(testData.getProject())
            .then().spec(ValidationResponseSpecifications.checkProjectIdsLengthLess255(testData.getProject().getId()));
    }

    @Test(description = "System Admin should not be able to create project with id length > 225",
        groups = {"Negative", "Crud"})
    public void systemAdminCreatesProjectWithLongIdLengthTest() {
        testData.getProject().setId(RandomData.getString(PROJECT_ID_LENGTH_LIMIT + 1));

        superUserCheckRequests.getRequest(USERS).create(testData.getUser());

        new CheckedRequests(Specifications.authSpec(testData.getUser()));

        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
            .create(testData.getProject())
            .then().spec(ValidationResponseSpecifications.checkProjectIdsLengthLess255(testData.getProject().getId()));
    }

    @DataProvider(name = "Roles")
    public static Object[][] roles() {
        return new Object[][]{
            {RoleType.PROJECT_VIEWER},
            {RoleType.PROJECT_DEVELOPER},
            {RoleType.AGENT_MANAGER}
        };
    }

    @Test(dataProvider = "Roles",
        description = "Users with roles Project viewer, Project developer, Agent manager" +
                          " should not be able to create project of global scope")
    public void userWithCertainRolesCreatesProjectTest(RoleType roleType) {
        Role role = Role.builder().roleId(roleType.getRoleId()).scope("g").build();
        var roles = Roles.builder().role(Collections.singletonList(role)).build();
        testData.getUser().setRoles(roles);

        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        new CheckedRequests(Specifications.authSpec(testData.getUser()));

        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
            .create(testData.getProject())
            .then().spec(ValidationResponseSpecifications.checkUsersRoleCreateProject());
    }

    @Test(description = "Unauthorized user should not be able to create Project")
    public void unauthorizedUserCreatesProjectTest() {

        new UncheckedBase(Specifications.authSpec(testData.getUser()), PROJECTS)
            .create(testData.getProject())
            .then().spec(ValidationResponseSpecifications.checkUnauthUserCreatesResource());
    }
}