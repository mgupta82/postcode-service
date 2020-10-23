package com.auspost.postcode.controller;

import com.auspost.postcode.dto.ErrorResponse;
import com.auspost.postcode.repository.entity.Postcode;
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

    @Test
    public void testAddPostcode() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("content-type", MediaType.APPLICATION_JSON_VALUE);
        Postcode postcode = Postcode.builder()
                .code(3163)
                .suburb("Carnegie")
                .state("VIC")
                .build();
        HttpEntity<Postcode> request = new HttpEntity<>(postcode);
        ResponseEntity<Postcode> responseEntity = restTemplate.exchange("/postcode/create", HttpMethod.POST,request,Postcode.class);
        AssertionErrors.assertEquals("Invalid Http Status",HttpStatus.CREATED,responseEntity.getStatusCode());
        AssertionErrors.assertEquals("Invalid code",3163,responseEntity.getBody().getCode());
        AssertionErrors.assertEquals("Invalid suburb","Carnegie",responseEntity.getBody().getSuburb());
        AssertionErrors.assertEquals("Invalid code","VIC",responseEntity.getBody().getState());
    }

    @Test
    public void testGetPostcode() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("content-type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity request = new HttpEntity<>(null);
        ResponseEntity<Postcode> responseEntity = restTemplate.exchange("/postcode/details/3192", HttpMethod.GET,request,Postcode.class);
        AssertionErrors.assertEquals("Invalid Http Status",HttpStatus.OK,responseEntity.getStatusCode());
        AssertionErrors.assertEquals("Invalid code",3192,responseEntity.getBody().getCode());
        AssertionErrors.assertEquals("Invalid suburb","Cheltenham",responseEntity.getBody().getSuburb());
        AssertionErrors.assertEquals("Invalid code","VIC",responseEntity.getBody().getState());
    }

    @Test
    public void testSearchSuburb() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("content-type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity request = new HttpEntity<>(null);
        ResponseEntity<Postcode[]> responseEntity = restTemplate.exchange("/postcode/suburb/cha", HttpMethod.GET,request, Postcode[].class);
        AssertionErrors.assertEquals("Invalid Http Status",HttpStatus.OK,responseEntity.getStatusCode());
        AssertionErrors.assertEquals("Incorrect search result",3,responseEntity.getBody().length);
    }

    @Test
    public void testErrorScenario() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("content-type", MediaType.APPLICATION_JSON_VALUE);
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
