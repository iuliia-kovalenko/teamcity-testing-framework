package com.example.teamcity.api;

import com.example.teamcity.api.models.*;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import com.example.teamcity.api.spec.ValidationResponseSpecifications;
import org.testng.annotations.Test;

import java.util.List;

import static com.example.teamcity.api.enums.Endpoint.*;
import static com.example.teamcity.api.generators.TestDataGenerator.generate;

@Test(groups = {"Regression"})
public class BuildTypeTest extends BaseApiTest {

    @Test(description = "User should be able to create build type", groups = {"Positive", "CRUD"})
    public void userCreatesBuildTypeTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        testData.getBuildType().setSteps(null);
        userCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        var createdBuildType = userCheckRequests.<BuildType>getRequest(BUILD_TYPES).read(testData.getBuildType().getId());

        softy.assertEquals(testData.getBuildType().getName(), createdBuildType.getName(), "BuildTypeName is not correct");
    }

    @Test(description = "User should not be able to create two build types with the same id", groups = {"Negative", "CRUD"})
    public void userCreatesTwoBuildTypesWithTheSameIdTest() {
        var buildRTypeWithSameId = generate(List.of(testData.getProject()), BuildType.class, testData.getBuildType().getId());

        superUserCheckRequests.getRequest(USERS).create(testData.getUser());

        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        testData.getBuildType().setSteps(null);

        userCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        new UncheckedBase(Specifications.authSpec(testData.getUser()), BUILD_TYPES)
            .create(buildRTypeWithSameId)
            .then().spec(ValidationResponseSpecifications.checkBuildTypeWithIdAlreadyExist(testData.getBuildType().getId()));
    }

    @Test(description = "Project admin should be able to create build type for their project", groups = {"Positive", "Roles"})
    public void projectAdminCreatesBuildTypeTest() {
        superUserCheckRequests.getRequest(PROJECTS).create(testData.getProject());

        Roles rolesUser = Roles.generateRoles(Role.generateProjectAdmin(testData.getProject().getId()));

        var createdUser = (User) superUserCheckRequests.getRequest(USERS).create(testData.getUser());

        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        createdUser.setRoles(rolesUser);

        superUserCheckRequests.getRequest(USERS).update(createdUser.getId(), createdUser);

        var buildTypeProject1 = testData.getBuildType();

        buildTypeProject1.setSteps(null);

        userCheckRequests.getRequest(BUILD_TYPES).create(buildTypeProject1);

        var createdBuildType = userCheckRequests.<BuildType>getRequest(BUILD_TYPES).read(buildTypeProject1.getId());

        softy.assertEquals(buildTypeProject1.getName(), createdBuildType.getName(), "BuildTypeName is not correct");
    }

    @Test(description = "Project admin should not be able to create build type for not their project", groups = {"Negative", "Roles"})
    public void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {
        TestData testData1 = generate();
        TestData testData2 = generate();
        var project1 = testData1.getProject();
        var project2 = testData2.getProject();
        Roles rolesUser1 = Roles.generateRoles(Role.generateProjectAdmin(project1.getId()));
        testData1.getUser().setRoles(rolesUser1);
        Roles rolesUser2 = Roles.generateRoles(Role.generateProjectAdmin(project2.getId()));
        testData2.getUser().setRoles(rolesUser2);
        testData1.getBuildType().setSteps(null);

        superUserCheckRequests.getRequest(PROJECTS).create(project1);
        superUserCheckRequests.getRequest(PROJECTS).create(project2);

        superUserCheckRequests.getRequest(USERS).create(testData1.getUser());
        superUserCheckRequests.getRequest(USERS).create(testData2.getUser());

        new UncheckedBase(Specifications.authSpec(testData2.getUser()), BUILD_TYPES)
            .create(testData1.getBuildType())
            .then().spec(ValidationResponseSpecifications.checkPermissionToEditProject(project1.getId()));
    }
}