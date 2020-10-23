package com.auspost.postcode.service;

import com.auspost.postcode.repository.PostcodeRepository;
import com.auspost.postcode.repository.entity.Postcode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@Slf4j
public class PostcodeService {
    @Autowired
    private PostcodeRepository postcodeRepository;

    @Autowired
    private EntityManager entityManager;

    public Postcode getByCode(String code) {
        log.info("Finding Postcode = {}",code);
        return postcodeRepository.findById(Integer.valueOf(code)).orElse(null);
    }

    public Postcode insertCode(Postcode postcode) {
        log.info("Saving Postcode = {}",postcode);
        entityManager.persist(postcode);
        return postcode;
    }

    public Postcode saveCode(Postcode postcode) {
        log.info("Saving Postcode = {}",postcode);
        return postcodeRepository.save(postcode);
    }

    public List<Postcode> searchBySuburb(String suburb) {
        log.info("Searching for Suburb starting with = {}",suburb);
        return postcodeRepository.findBySuburbStartsWithIgnoreCase(suburb);
    }

}
