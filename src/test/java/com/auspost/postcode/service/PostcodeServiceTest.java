package com.auspost.postcode.service;

import com.auspost.postcode.repository.entity.Postcode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.AssertionErrors;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class PostcodeServiceTest {
    @Autowired
    private PostcodeService postcodeService;

    @Test
    public void testGetByPostcode() {
        Postcode postcode = postcodeService.getByCode("3192");
        Assertions.assertNotNull(postcode);
        AssertionErrors.assertEquals("Invalid postcode",3192,postcode.getCode());
        AssertionErrors.assertEquals("Invalid Suburb","Cheltenham",postcode.getSuburb());
        AssertionErrors.assertEquals("Invalid State","VIC",postcode.getState());
    }

    @Test
    public void testSavePostcode() {
        Postcode persisted = postcodeService
                .saveCode(Postcode.builder()
                        .code(3063).suburb("Oakleigh").state("VIC").build());
        Assertions.assertNotNull(persisted);
        AssertionErrors.assertEquals("Invalid postcode",3063,persisted.getCode());
        AssertionErrors.assertEquals("Invalid Suburb","Oakleigh",persisted.getSuburb());
        AssertionErrors.assertEquals("Invalid State","VIC",persisted.getState());
    }

    @Test
    public void testSearchBySuburb() {
        List<Postcode> postcodes = postcodeService.searchBySuburb("cha");
        Assertions.assertNotNull(postcodes);
        AssertionErrors.assertEquals("mismatch number of results",3,postcodes.size());
    }
}
