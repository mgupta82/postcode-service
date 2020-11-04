package com.auspost.postcode.controller;

import com.auspost.postcode.JwtUtil;
import com.auspost.postcode.dto.ErrorResponse;
import com.auspost.postcode.repository.entity.Postcode;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.AssertionErrors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts= {"/scripts/test-search.sql"})
public class PostcodeControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    private static WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8089));

    @BeforeAll
    public static void setup() {
        WireMock.configureFor("localhost", 8089);
        wireMockServer.start();
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/auth/realms/test/protocol/openid-connect/certs"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("jwks_response.json")));
    }

    @AfterAll
    public static void finish() {
        wireMockServer.stop();
    }

    @Test
    public void testAddPostcode() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("content-type", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add("Authorization", "Bearer "+ JwtUtil.generateJwtToken());
        Postcode postcode = Postcode.builder()
                .code(3163)
                .suburb("Carnegie")
                .state("VIC")
                .build();
        HttpEntity<Postcode> request = new HttpEntity<>(postcode,httpHeaders);
        ResponseEntity<Postcode> responseEntity = restTemplate.exchange("/api/create", HttpMethod.POST,request,Postcode.class);
        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/auth/realms/test/protocol/openid-connect/certs")));
        AssertionErrors.assertEquals("Invalid Http Status",HttpStatus.CREATED,responseEntity.getStatusCode());
        AssertionErrors.assertEquals("Invalid code",3163,responseEntity.getBody().getCode());
        AssertionErrors.assertEquals("Invalid suburb","Carnegie",responseEntity.getBody().getSuburb());
        AssertionErrors.assertEquals("Invalid code","VIC",responseEntity.getBody().getState());
    }

    @Test
    public void testGetPostcode() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("content-type", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add("Authorization", "Bearer "+ JwtUtil.generateJwtToken());
        HttpEntity request = new HttpEntity<>(httpHeaders);
        ResponseEntity<Postcode> responseEntity = restTemplate.exchange("/api/details/3192", HttpMethod.GET,request,Postcode.class);
        AssertionErrors.assertEquals("Invalid Http Status",HttpStatus.OK,responseEntity.getStatusCode());
        AssertionErrors.assertEquals("Invalid code",3192,responseEntity.getBody().getCode());
        AssertionErrors.assertEquals("Invalid suburb","Cheltenham",responseEntity.getBody().getSuburb());
        AssertionErrors.assertEquals("Invalid code","VIC",responseEntity.getBody().getState());
    }

    @Test
    public void testSearchSuburb() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("content-type", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add("Authorization", "Bearer "+ JwtUtil.generateJwtToken());
        HttpEntity request = new HttpEntity<>(httpHeaders);
        ResponseEntity<Postcode[]> responseEntity = restTemplate.exchange("/api/suburb/cha", HttpMethod.GET,request, Postcode[].class);
        AssertionErrors.assertEquals("Invalid Http Status",HttpStatus.OK,responseEntity.getStatusCode());
        AssertionErrors.assertEquals("Incorrect search result",3,responseEntity.getBody().length);
    }

    @Test
    public void testErrorScenario() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("content-type", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add("Authorization", "Bearer "+ JwtUtil.generateJwtToken());
        Postcode postcode = Postcode.builder()
                .code(3111)
                .suburb(null)
                .state("VIC")
                .build();
        HttpEntity<Postcode> request = new HttpEntity<>(postcode,httpHeaders);
        ResponseEntity<ErrorResponse> responseEntity = restTemplate.exchange("/api/create", HttpMethod.POST,request,ErrorResponse.class);
        AssertionErrors.assertEquals("Invalid Http Status",HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
        AssertionErrors.assertEquals("Invaid Reason","missing mandatory fields",responseEntity.getBody().getReason());
    }

}
