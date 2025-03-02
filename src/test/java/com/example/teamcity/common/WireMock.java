package com.example.teamcity.common;

import com.example.teamcity.api.models.BaseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import lombok.SneakyThrows;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.common.ContentTypes.APPLICATION_JSON;
import static com.github.tomakehurst.wiremock.common.ContentTypes.CONTENT_TYPE;

public final class WireMock {

    private static WireMockServer wireMockServer;

    private WireMock() {
    }

    @SneakyThrows
    public static void setupServer(MappingBuilder mappingBuilder, int status, BaseModel model) {
        if (wireMockServer == null) {
            wireMockServer = new WireMockServer(8081);
            wireMockServer.start();
            System.out.println("WireMockServer started on port 8081");
        }

        var jsonModel = new ObjectMapper().writeValueAsString(model);
        System.out.println("Serialized model: " + jsonModel);

        wireMockServer.stubFor(mappingBuilder
                                   .willReturn(aResponse()
                                                   .withStatus(status)
                                                   .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                                                   .withBody(jsonModel)));

        System.out.println("WireMock stub configured for: " + mappingBuilder);
    }

    public static void stopServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
            wireMockServer = null;
        }
    }
}