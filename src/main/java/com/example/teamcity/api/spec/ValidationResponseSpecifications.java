package com.example.teamcity.api.spec;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;

public class ValidationResponseSpecifications {
    public static ResponseSpecification checkProjectWithNameAlreadyExist(String projectName) {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_BAD_REQUEST);
        responseSpecBuilder.expectBody(Matchers.containsString("Project with this name already exists: %s".formatted(projectName)));
        return responseSpecBuilder.build();
    }

    public static ResponseSpecification checkBuildTypeWithIdAlreadyExist(String buildTypeId) {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_BAD_REQUEST);
        responseSpecBuilder.expectBody(Matchers.containsString("The build configuration / template ID \"%s\" is already used by another configuration or template".
                                                                   formatted(buildTypeId)));
        return responseSpecBuilder.build();
    }

    public static ResponseSpecification checkPermissionToEditProject(String projectId) {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_FORBIDDEN);
        responseSpecBuilder.expectBody(Matchers.containsString("You do not have enough permissions to edit project with id: %s".
                                                                   formatted(projectId)));
        return responseSpecBuilder.build();
    }

    public static ResponseSpecification checkNoBuildTypeFound(String projectId) {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_NOT_FOUND);
        responseSpecBuilder.expectBody(Matchers.containsString("Nothing is found by locator 'count:1,project:%s'".
                                                                   formatted(projectId)));
        return responseSpecBuilder.build();
    }

    public static ResponseSpecification checkProjectWithEmptyName(String projectId) {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_BAD_REQUEST);
        responseSpecBuilder.expectBody(Matchers.containsString("Project name cannot be empty.".
                                                                   formatted(projectId)));
        return responseSpecBuilder.build();
    }

    public static ResponseSpecification checkProjectWithEmptyId(String projectId) {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        responseSpecBuilder.expectBody(Matchers.containsString("Project ID must not be empty.".
                                                                   formatted(projectId)));
        return responseSpecBuilder.build();
    }

    public static ResponseSpecification checkProjectStartsWithNonLetterCharacter(String projectId) {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        responseSpecBuilder.expectBody(Matchers.containsString(("Project ID \"%s\" is invalid:" +
                                                                    " starts with non-letter character '%s'." +
                                                                    " ID should start with a latin letter and contain only latin letters," +
                                                                    " digits and underscores (at most 225 characters).").
                                                                   formatted(projectId, projectId.charAt(0))));
        return responseSpecBuilder.build();
    }

    public static ResponseSpecification checkProjectContainsOnlyLatinLettersAndUnderscores(String projectId) {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        responseSpecBuilder.expectBody(Matchers.containsString(("ID should start with a latin letter and contain only latin letters," +
                                                                    " digits and underscores (at most 225 characters).")));
        return responseSpecBuilder.build();
    }


    public static ResponseSpecification checkProjectIdsLengthLess255(String projectId) {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        responseSpecBuilder.expectBody(Matchers.containsString(("Project ID \"%s\" is invalid:" +
                                                                    " it is %s characters long while the maximum length is 225. " +
                                                                    "ID should start with a latin letter and contain only latin letters," +
                                                                    " digits and underscores (at most 225 characters).").
                                                                   formatted(projectId, projectId.length())));
        return responseSpecBuilder.build();
    }

    public static ResponseSpecification checkUsersRoleCreateProject() {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_FORBIDDEN);
        responseSpecBuilder.expectBody(Matchers.containsString("You do not have \"Create subproject\" permission in project with internal id: _Root\n" +
                                                                    "Access denied. Check the user has enough permissions to perform the operation."));
        return responseSpecBuilder.build();
    }

    public static ResponseSpecification checkUnauthUserCreatesProject() {
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(HttpStatus.SC_UNAUTHORIZED);
        responseSpecBuilder.expectBody(Matchers.containsString("Incorrect username or password.\n" +
                                                                   "To login manually go to \"/login.html\" page"));
        return responseSpecBuilder.build();
    }
}