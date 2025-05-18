package com.example.teamcity.api;

import com.example.teamcity.api.generators.RandomData;
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
    private static final int BUILDTYPE_ID_LENGTH_LIMIT = 225;

    @Test(description = "System Admin should be able to create build type", groups = {"Positive", "CRUD"})
    public void userCreatesBuildTypeTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        testData.getBuildType().setSteps(null);
        userCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        var createdBuildType = userCheckRequests.<BuildType>getRequest(BUILD_TYPES).read("id:" + testData.getBuildType().getId());

        softy.assertEquals(testData.getBuildType().getName(), createdBuildType.getName(), "BuildTypeName is not correct");
    }

    @Test(description = "SuperUser should be able to create build type", groups = {"Positive", "CRUD"})
    public void superUserCreatesBuildTypeTest() {

        superUserCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        testData.getBuildType().setSteps(null);
        superUserCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        var createdBuildType = superUserCheckRequests.<BuildType>getRequest(BUILD_TYPES).read("id:" + testData.getBuildType().getId());

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

    @Test(description = "SuperUser should not be able to create two build types with the same id", groups = {"Negative", "CRUD"})
    public void superUserCreatesTwoBuildTypesWithTheSameIdTest() {
        var buildRTypeWithSameId = generate(List.of(testData.getProject()), BuildType.class, testData.getBuildType().getId());

        superUserCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        testData.getBuildType().setSteps(null);

        superUserCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        new UncheckedBase(Specifications.superUserSpec(), BUILD_TYPES)
            .create(buildRTypeWithSameId)
            .then().spec(ValidationResponseSpecifications.checkBuildTypeWithIdAlreadyExist(testData.getBuildType().getId()));
    }

    @Test(description = "Project admin should be able to create build type for their project", groups = {"Positive", "Roles"})
    public void projectAdminCreatesBuildTypeTest() {
        superUserCheckRequests.getRequest(PROJECTS).create(testData.getProject());

        Roles rolesUser = Roles.generateRoles(List.of(Role.generateProjectAdmin(testData.getProject().getId())));

        var createdUser = (User) superUserCheckRequests.getRequest(USERS).create(testData.getUser());

        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        createdUser.setRoles(rolesUser);
        System.out.println(createdUser);
        superUserCheckRequests.getRequest(USERS).update(createdUser.getUsername(), createdUser);

        var buildTypeProject1 = testData.getBuildType();

        buildTypeProject1.setSteps(null);

        userCheckRequests.getRequest(BUILD_TYPES).create(buildTypeProject1);

        var createdBuildType = userCheckRequests.<BuildType>getRequest(BUILD_TYPES).read("id:" + buildTypeProject1.getId());

        softy.assertEquals(buildTypeProject1.getName(), createdBuildType.getName(), "BuildTypeName is not correct");
    }

    @Test(description = "Project admin should not be able to create build type for not their project", groups = {"Negative", "Roles"})
    public void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {
        TestData testData1 = generate();
        TestData testData2 = generate();
        var project1 = testData1.getProject();
        var project2 = testData2.getProject();
        Roles rolesUser1 = Roles.generateRoles(List.of(Role.generateProjectAdmin(project1.getId())));
        testData1.getUser().setRoles(rolesUser1);
        Roles rolesUser2 = Roles.generateRoles(List.of(Role.generateProjectAdmin(project2.getId())));
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

    @Test(description = "System admin should not be able to create build type with ID > 225 symbols", groups = {"Negative", "CRUD"})
    public void userCreatesBuildTypeWithIdExceedingMaxLengthTest() {
        superUserCheckRequests.getRequest(USERS).create(testData.getUser());
        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        userCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        testData.getBuildType().setSteps(null);
        testData.getBuildType().setId(RandomData.getString(BUILDTYPE_ID_LENGTH_LIMIT + 1));

        new UncheckedBase(Specifications.authSpec(testData.getUser()), BUILD_TYPES)
            .create(testData.getBuildType())
            .then().spec(ValidationResponseSpecifications.checkBuildTypeIdsLengthLess255(testData.getBuildType().getId(),
                testData.getBuildType().getId().length()));
    }

    @Test(description = "SuperUser should not be able to create build type with ID > 225 symbols", groups = {"Negative", "CRUD"})
    public void superUserCreatesBuildTypeWithIdExceedingMaxLengthTest() {
        superUserCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());
        testData.getBuildType().setSteps(null);
        testData.getBuildType().setId(RandomData.getString(BUILDTYPE_ID_LENGTH_LIMIT + 1));

        new UncheckedBase(Specifications.superUserSpec(), BUILD_TYPES)
            .create(testData.getBuildType())
            .then().spec(ValidationResponseSpecifications.checkBuildTypeIdsLengthLess255(testData.getBuildType().getId(),
                testData.getBuildType().getId().length()));
    }

    @Test(description = "Unauthorized user should not be able to create BuildType")
    public void unauthorizedUserCreatesBuildType() {
        superUserCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        new UncheckedBase(Specifications.authSpec(testData.getUser()), BUILD_TYPES)
            .create(testData.getBuildType())
            .then().spec(ValidationResponseSpecifications.checkUnauthUserCreatesResource());
    }
}