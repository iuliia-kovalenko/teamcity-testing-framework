package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.pages.BasePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public abstract class CreateBasePage extends BasePage {
    protected static final String CREATE_URL = "/admin/createObjectMenu.html?projectId=%s&showMode=%s";

    protected SelenideElement urlInput = $("#url");
    protected SelenideElement submitButton = $(Selectors.byAttribute("value", "Proceed"));
    protected SelenideElement buildtypeNameInput = $("#buildTypeName");
    protected SelenideElement conectionSuccessfulMessage = $(".connectionSuccessful");


    @Step("Input RepoURL, Submit button")
    protected void baseCreateForm(String url) {
        urlInput.val(url);
        submitButton.click();
        conectionSuccessfulMessage.should(Condition.appear, BASE_WAITING);
    }
}