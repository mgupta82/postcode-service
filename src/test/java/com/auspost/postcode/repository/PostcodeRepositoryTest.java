package com.auspost.postcode.repository;

import com.auspost.postcode.repository.entity.Postcode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.AssertionErrors;

@SpringBootTest
@ActiveProfiles("test")
public class PostcodeRepositoryTest {

    @Autowired
    private PostcodeRepository postcodeRepository;

    @Test
    public void testGetByPostcode() {
        Postcode postcode = postcodeRepository.findById(3192).orElse(null);
        Assertions.assertNotNull(postcode);
        AssertionErrors.assertEquals("Invalid postcode",3192,postcode.getCode());
        AssertionErrors.assertEquals("Invalid Suburb","Cheltenham",postcode.getSuburb());
        AssertionErrors.assertEquals("Invalid State","VIC",postcode.getState());
    }

}
