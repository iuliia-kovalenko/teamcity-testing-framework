package com.example.teamcity.ui;

import lombok.Getter;
import org.testng.Assert;

public class ValidateElement {

    @Getter
    public enum UiErrors {
        BUILD_CONFIG_REPO_URL_MUST_BE_NOT_NULL("URL must not be empty"),
        BUILD_CONFIG_REPO_URL_MUST_BE_CORRECT("git -c credential.helper= ls-remote origin command failed.\n" +
                                                  "exit code: 128\n" +
                                                  "stderr: fatal: '%s' does not appear to be a git repository\n" +
                                                  "fatal: Could not read from remote repository.\n" +
                                                  "\n" +
                                                  "Please make sure you have the correct access rights\n" +
                                                  "and the repository exists."),
        PROJECT_NAME_MUST_NOT_BE_EMPTY("Project name must not be empty"),
        PROJECT_NOT_FOUND("Nothing found");

        private final String message;

        UiErrors(String message) {
            this.message = message;
        }

        public String getFormattedMessage(Object... args) {
            return String.format(message, args);
        }
    }

    public static void byText(String actualErrorMessage, UiErrors error) {
        String expectedText = error.getMessage();
        Assert.assertEquals(actualErrorMessage, expectedText,
            String.format("Validation failed. Expected: '%s', Actual: '%s'", expectedText, actualErrorMessage));
    }

    public static void containsText(String actualErrorMessage, UiErrors error, Object... args) {
        String expectedText = error.getFormattedMessage(args);
        Assert.assertTrue(actualErrorMessage.contains(expectedText),
            String.format("Validation failed. Expected to contain: '%s', Actual: '%s'", expectedText, actualErrorMessage));
    }
}