package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class BuildTypePage extends BasePage {
    private static final String BUILD_TYPE_URL = "/buildConfiguration/%s?mode=builds";

    public static SelenideElement title = $("h1[class*='BuildTypePageHeader']>span");

    private SelenideElement header = $(".MainPanel__router--gF > div");


    public static BuildTypePage open(String projectId, String buildTypeName) {
        return Selenide.open(BUILD_TYPE_URL.formatted(projectId + "_" + buildTypeName.replace("_", "")), BuildTypePage.class);
    }

    public BuildTypePage() {
        header.shouldBe(Condition.visible, BASE_WAITING);
    }
}