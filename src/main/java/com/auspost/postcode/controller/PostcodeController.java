package com.auspost.postcode.controller;

import com.auspost.postcode.repository.entity.Postcode;
import com.auspost.postcode.service.PostcodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Slf4j
@RequestMapping(path = "/postcode")
public class PostcodeController {

    @Autowired
    private PostcodeService postcodeService;

    @PostMapping(path ="/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Postcode> addPostcode(@RequestBody Postcode postcode) throws Exception {
        Postcode persisted = postcodeService.saveCode(postcode);
        return ResponseEntity
                .created(new URI("/create/"+persisted.getCode()))
                .body(persisted);
    }

    @GetMapping(path ="/details/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Postcode> addPostcode(@PathVariable String code) {
        Postcode postcode = postcodeService.getByCode(code);
        if (postcode == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity<>(postcode, HttpStatus.OK);
    }

}
