package com.example.teamcity.api;

import org.testng.annotations.Test;
import static io.qameta.allure.Allure.step;

@Test(groups = {"Regression"})
public class BuildTypeTest extends BaseApiTest{

    @Test(description = "User should be able to create build type", groups = {"Positive", "CRUD"} )
    public void userCreatesBuildTypeTest() {
        step("Create user");
        step("Create project by user");
        step("Create buildtype for project by user");
        step("Check buildtype was created successfully with correct data");


    }

    @Test(description = "User should not be able to create two build types with the same id", groups = {"Negative", "CRUD"} )
    public void userCreatesTwoBuildTypesWithTheSameIdTest() {
        step("Create user");
        step("Create project by user");
        step("Create buildtype1 for project by user");
        step("Create buildtype2 with same id as buildtype1 for project by user");
        step("Check buildtype2 was not created with bad request code");


    }

    @Test(description = "Project admin should be able to create build type for their project", groups = {"Positive", "Roles"} )
    public void projectAdminCreatesBuildTypeTest() {
        step("Create user");
        step("Create project");
        step("Grant user PROJECT_ADMIN role in project");

        step("Create buildtype for project by user");
        step("Check buildtype was not created with successfully");


    }

    @Test(description = "Project admin should not be able to create build type for not their project", groups = {"Negative", "Roles"} )
    public void projectAdminCreatesBuildTypeForAnotherUserProjectTest() {
        step("Create user1");
        step("Create project1");
        step("Grant user1 PROJECT_ADMIN role in project");

        step("Create user2");
        step("Create project2");
        step("Grant user2 PROJECT_ADMIN role in project");

        step("Create buildtype for project1 by user2");
        step("Check buildtype was not created with forbidden code");


    }
}
