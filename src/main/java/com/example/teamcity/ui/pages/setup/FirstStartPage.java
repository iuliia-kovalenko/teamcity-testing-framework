package com.example.teamcity.ui.pages.setup;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.api.models.BaseModel;
import com.example.teamcity.ui.pages.BasePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class FirstStartPage extends BasePage {
    private final SelenideElement proceedButton = $("#proceedButton");
    private final SelenideElement restoreButton = $("#restoreButton");
    private final SelenideElement dbTypeSelect = $("#dbType");
    private final SelenideElement acceptLicenceCheckbox = $("#accept");
    private final SelenideElement submitButton = $("input[type='submit']");

    public FirstStartPage() {
        restoreButton.shouldBe(Condition.visible, LONG_WAITING);
    }

    @Step("Open First start page")
    public static FirstStartPage open() {
        return Selenide.open("/", FirstStartPage.class);
    }

    @Step("Click proceed button, accept agreement, click submit")
    public FirstStartPage setUpFirstStart() {
        proceedButton.click();
        dbTypeSelect.shouldBe(Condition.visible, LONG_WAITING);
        proceedButton.click();
        acceptLicenceCheckbox.should(Condition.exist,LONG_WAITING).scrollTo().click();
        submitButton.click();
        return this;
    }
}
