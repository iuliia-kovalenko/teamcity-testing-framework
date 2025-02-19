package com.example.teamcity.api;

import com.example.teamcity.api.enums.RoleType;
import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.models.*;
import com.example.teamcity.api.requests.CheckedRequests;
import com.example.teamcity.api.requests.unchecked.UncheckedBase;
import com.example.teamcity.api.spec.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
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

        userCheckRequests.getRequest(BUILD_TYPES).create(testData.getBuildType());

        new UncheckedBase(Specifications.authSpec(testData.getUser()), BUILD_TYPES)
            .create(buildRTypeWithSameId)
            .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
            .body(Matchers.containsString("The build configuration / template ID \"%s\" is already used by another configuration or template"
                                              .formatted(testData.getBuildType().getId())));
    }

    @Test(description = "Project admin should be able to create build type for their project", groups = {"Positive", "Roles"})
    public void projectAdminCreatesBuildTypeTest() {
        var project = generate(Project.class);
        superUserCheckRequests.getRequest(PROJECTS).create(project);

        Roles rolesUser = Roles.builder()
                              .role(List.of(Role.builder()
                                                .roleId(RoleType.PROJECT_ADMIN.getRoleId())
                                                .scope("p:" + project.getId())
                                                .build()))
                              .build();

        var createdUser = (User) superUserCheckRequests.getRequest(USERS).create(testData.getUser());

        var userCheckRequests = new CheckedRequests(Specifications.authSpec(testData.getUser()));

        createdUser.setRoles(rolesUser);

        superUserCheckRequests.getRequest(USERS).update(createdUser.getId(), createdUser);

        var buildTypeProject1 = generate(List.of(project), BuildType.class);

        userCheckRequests.getRequest(BUILD_TYPES).create(buildTypeProject1);

        var createdBuildType = userCheckRequests.<BuildType>getRequest(BUILD_TYPES).read(buildTypeProject1.getId());

        softy.assertEquals(buildTypeProject1.getName(), createdBuildType.getName(), "BuildTypeName is not correct");
    }

    @Test(description = "Project admin should not be able to create build type for not their project", groups = {"Negative", "Roles"})
    public void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {
        var project1 = generate(Project.class);
        superUserCheckRequests.getRequest(PROJECTS).create(project1);
        var project2 = generate(Project.class);
        superUserCheckRequests.getRequest(PROJECTS).create(project2);

        Roles rolesUser1 = Roles.builder()
                               .role(List.of(Role.builder()
                                                 .roleId(RoleType.PROJECT_ADMIN.getRoleId())
                                                 .scope("p:" + project1.getId())
                                                 .build()))
                               .build();

        Roles rolesUser2 = Roles.builder()
                               .role(List.of(Role.builder()
                                                 .roleId(RoleType.PROJECT_ADMIN.getRoleId())
                                                 .scope("p:" + project2.getId())
                                                 .build()))
                               .build();

        User user1 = TestDataGenerator.generate(User.class, rolesUser1);
        User user2 = TestDataGenerator.generate(User.class, rolesUser2);

        superUserCheckRequests.getRequest(USERS).create(user1);
        superUserCheckRequests.getRequest(USERS).create(user2);

        var buildRTypeProject1 = generate(List.of(project1), BuildType.class);

        new UncheckedBase(Specifications.authSpec(user2), BUILD_TYPES)
            .create(buildRTypeProject1)
            .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
            .body(Matchers.containsString(("You do not have enough permissions to edit project with id: %s")
                                              .formatted(project1.getId())));
    }
}