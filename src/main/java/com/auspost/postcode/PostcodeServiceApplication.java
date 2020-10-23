package com.auspost.postcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class PostcodeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostcodeServiceApplication.class, args);
	}

}
