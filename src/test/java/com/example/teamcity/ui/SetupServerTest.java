package com.example.teamcity.ui;

import com.example.teamcity.ui.pages.setup.FirstStartPage;
import org.testng.annotations.Test;


public class SetupServerTest extends BaseUiTest {
    @Test(description = "Setup Teamcity server", groups = {"Setup"})
    private void setupTeamcityServerTest() {
        FirstStartPage.open().setUpFirstStart();
    }
}