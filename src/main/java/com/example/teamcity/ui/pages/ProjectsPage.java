package com.example.teamcity.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.teamcity.ui.elements.ProjectElement;
import com.example.teamcity.ui.elements.SideBarElement;
import io.qameta.allure.Step;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;

public class ProjectsPage extends BasePage {
    private static final String PROJECTS_URL = "/favorite/projects";

    private ElementsCollection projectsElements = $$("div[class*='Subproject__container']");

    private SelenideElement header = $(".MainPanel__router--gF > div");

    private SelenideElement projectNameInput = $("#search-projects");

    private SelenideElement sectionFavorites = $x("//span[text()='FAVORITES']");

    private ElementsCollection sideBarRowElements = $$("[role='row']");

    private SelenideElement notFoundMessage = $x("//span[text()='Nothing found']");


    //  ElementCollection -> List<ProjectElement>
    // UI elements -> List<Object>
    // ElementCollection -> List<BasePageElement>

    @Step("Open Projects page")
    public static ProjectsPage open() {
        return Selenide.open(PROJECTS_URL, ProjectsPage.class);
    }

    public ProjectsPage() {
        header.shouldBe(Condition.visible, BASE_WAITING);
    }

    public List<ProjectElement> getProjects() {
        return generatePageElements(projectsElements, ProjectElement::new);
    }

    public List<SideBarElement> getSideBarElements() {
        return generatePageElements(sideBarRowElements, SideBarElement::new);
    }

    @Step("Input project name in search field")
    public ProjectsPage inputProjectNameInToSearchField(String projectName) {
        projectNameInput.val(projectName);
        sectionFavorites.should(Condition.disappear, BASE_WAITING);
        return this;
    }

    @Step("Get not found project by name validation message")
    public String getNotFoundValidationErrorMessage() {
        notFoundMessage.should(Condition.visible, BASE_WAITING);
        return notFoundMessage.text();
    }
 }