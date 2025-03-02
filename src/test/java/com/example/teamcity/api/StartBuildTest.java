package com.example.teamcity.api;

import com.example.teamcity.api.models.Build;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.Steps;
import org.testng.annotations.Test;

import static com.example.teamcity.api.enums.Endpoint.*;

public class StartBuildTest extends BaseApiTest {
    @Test(description = "Build can be executed and completed successfully", groups = {"Positive", "CRUD"})
    public void buildStartAndSuccessfulCompletionTest() {
        Steps steps = Steps.createDefaultSteps();
        BuildType buildType = testData.getBuildType();
        buildType.setSteps(steps);
        var build = testData.getBuild();

        superUserCheckRequests.<Project>getRequest(PROJECTS).create(testData.getProject());

        superUserCheckRequests.getRequest(BUILD_TYPES).create(buildType);

        var createdBuildTypeId = superUserCheckRequests.<BuildType>getRequest(BUILD_TYPES).read(buildType.getId());

        build.setBuildTypeId(createdBuildTypeId.getId());

        var runBuild = (Build) superUserCheckRequests.getRequest(BUILD_QUEUE).create(build);

        var passedBuild = (Build) superUserCheckRequests.getRequest(BUILD).read(runBuild.getId());

        softy.assertEquals(passedBuild.getStatus(), "SUCCESS", "Build is not successful");
    }
}
