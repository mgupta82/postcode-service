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

    private static final String ACCESS_TOKEN = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIzSVRITWstOF9VVzJzX3pVZjhZNXMzOHhlMkNLQXZ2X1hCU09RNDZoT3FnIn0.eyJleHAiOjE2MDM2MTcwMzIsImlhdCI6MTYwMzYxNjczMiwianRpIjoiY2NmZDdhMmYtYTkzOS00NWY3LTllMjUtZjMxOWRlMGNmMjA1IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDcwL2F1dGgvcmVhbG1zL3Rlc3QiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiZmQ2Njc5NzMtNmY5OC00MDM4LWJjNWItMmFiZTYzNjBhYmIzIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoicG9zdGNvZGUtY2xpZW50Iiwic2Vzc2lvbl9zdGF0ZSI6IjU1MTg5YTE3LWIwNzUtNDIxMy05NDBhLWRjN2JiZTAwMjk5NCIsImFjciI6IjEiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsImNsaWVudEhvc3QiOiIxNzIuMjYuMC4xIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJjbGllbnRJZCI6InBvc3Rjb2RlLWNsaWVudCIsInByZWZlcnJlZF91c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC1wb3N0Y29kZS1jbGllbnQiLCJjbGllbnRBZGRyZXNzIjoiMTcyLjI2LjAuMSJ9.ELeihF01h4DJYLp6Ht7pCuuZF4xfUFEX-ouX4U-C4eZFB0Iwro1-v3adzsv_MJuSL3J3k_b3wE6ECGTCrUrB0OQ3Y6HGu24W-5eRv4RLnV2MPcHV8_teSv6ryvlThGYYMaKi7O4RkIHlcQOkIMS4EPAkOTOlfKbKdyL1ZPzRxcN_Y13x8pqS3iWM1-zhtz7ptkQYsD4fSiFBnea3KdnOuTWwc9VE6Vlc2NqyiOfgLK_4KzpjcW17uKQ8VEfw4vHCIlamceXVgJFmOaewav9NcNNXo0qv_-T53NGsa9U0dmjaNOeeT2nDj7_HVHRBdM_rgd4kPLDXTF1f8jagnsg8Sw";

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
        httpHeaders.set("content-type", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set("Authorization", "Bearer "+ JwtUtil.generateJwtToken());
        Postcode postcode = Postcode.builder()
                .code(3163)
                .suburb("Carnegie")
                .state("VIC")
                .build();
        HttpEntity<Postcode> request = new HttpEntity<>(postcode);
        ResponseEntity<Postcode> responseEntity = restTemplate.exchange("/postcode/create", HttpMethod.POST,request,Postcode.class);
        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/auth/realms/test/protocol/openid-connect/certs")));
        AssertionErrors.assertEquals("Invalid Http Status",HttpStatus.CREATED,responseEntity.getStatusCode());
        AssertionErrors.assertEquals("Invalid code",3163,responseEntity.getBody().getCode());
        AssertionErrors.assertEquals("Invalid suburb","Carnegie",responseEntity.getBody().getSuburb());
        AssertionErrors.assertEquals("Invalid code","VIC",responseEntity.getBody().getState());
    }

    @Test
    public void testGetPostcode() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("content-type", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set("Authorization", "Bearer "+ JwtUtil.generateJwtToken());
        HttpEntity request = new HttpEntity<>(null);
        ResponseEntity<Postcode> responseEntity = restTemplate.exchange("/postcode/details/3192", HttpMethod.GET,request,Postcode.class);
        AssertionErrors.assertEquals("Invalid Http Status",HttpStatus.OK,responseEntity.getStatusCode());
        AssertionErrors.assertEquals("Invalid code",3192,responseEntity.getBody().getCode());
        AssertionErrors.assertEquals("Invalid suburb","Cheltenham",responseEntity.getBody().getSuburb());
        AssertionErrors.assertEquals("Invalid code","VIC",responseEntity.getBody().getState());
    }

    @Test
    public void testSearchSuburb() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("content-type", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set("Authorization", "Bearer "+ JwtUtil.generateJwtToken());
        HttpEntity request = new HttpEntity<>(null);
        ResponseEntity<Postcode[]> responseEntity = restTemplate.exchange("/postcode/suburb/cha", HttpMethod.GET,request, Postcode[].class);
        AssertionErrors.assertEquals("Invalid Http Status",HttpStatus.OK,responseEntity.getStatusCode());
        AssertionErrors.assertEquals("Incorrect search result",3,responseEntity.getBody().length);
    }

    @Test
    public void testErrorScenario() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("content-type", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.set("Authorization", "Bearer "+ JwtUtil.generateJwtToken());
        Postcode postcode = Postcode.builder()
                .code(3111)
                .suburb(null)
                .state("VIC")
                .build();
        HttpEntity<Postcode> request = new HttpEntity<>(postcode);
        ResponseEntity<ErrorResponse> responseEntity = restTemplate.exchange("/postcode/create", HttpMethod.POST,request,ErrorResponse.class);
        AssertionErrors.assertEquals("Invalid Http Status",HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
        AssertionErrors.assertEquals("Invaid Reason","missing mandatory fields",responseEntity.getBody().getReason());
    }

}
