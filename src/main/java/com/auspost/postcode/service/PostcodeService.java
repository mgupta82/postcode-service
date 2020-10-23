package com.auspost.postcode.service;

import com.auspost.postcode.repository.PostcodeRepository;
import com.auspost.postcode.repository.entity.Postcode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PostcodeService {
    @Autowired
    private PostcodeRepository postcodeRepository;

    public Postcode getByCode(String code) {
        return postcodeRepository.findById(Integer.valueOf(code)).orElse(null);
    }

    public Postcode saveCode(Postcode postcode) {
        return postcodeRepository.save(postcode);
    }

}
