package com.example.teamcity.ui.elements;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

@Getter
public class SideBarElement extends BasePageElement {
    private SelenideElement name;
    private SelenideElement link;
    private SelenideElement button;


    public SideBarElement(SelenideElement element) {
        super(element);
        this.name = find("[class^='ProjectsTreeItem__name'][title]");
        this.link = find("a");
        this.button = find("button");
    }
}