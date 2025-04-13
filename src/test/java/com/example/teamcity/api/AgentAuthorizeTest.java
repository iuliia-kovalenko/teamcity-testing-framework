package com.example.teamcity.api;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.api.models.Agents;
import com.example.teamcity.api.requests.AgentsAuthRequest;
import com.example.teamcity.api.spec.Specifications;
import org.testng.annotations.Test;


public class AgentAuthorizeTest extends BaseApiTest {
    @Test(description = "Superuser should be able to authorize Teamcity Agent", groups = {"Setup"})
    public void authorizeAgentTest() {
        AgentsAuthRequest agentsAuthRequest = new AgentsAuthRequest(Specifications.superUserSpec(), Endpoint.AGENTS);

        Agents agents = superUserCheckRequests.<Agents>getRequest(Endpoint.AGENTS).read("?locator=authorized:any");

        var authorisedAgent = agentsAuthRequest.update(agents.getAgent().get(0).getId(), "authorized");

        softy.assertEquals(authorisedAgent, "true");
    }
}
