package com.example.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.example.main.services.DBService;
import com.example.main.services.EmailService;
import com.example.main.services.MockEmailService;

@Configuration
@Profile("test")
public class ProfileTestConfig {

	@Autowired
	private DBService DBService;

	@Bean
	public boolean instantiateDataBase() {

		DBService.instatiateTestDataBase();

		return true;

	}

	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}

}