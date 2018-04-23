package com.example.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.example.main.services.DBService;
import com.example.main.services.EmailService;
import com.example.main.services.SmtpEmailService;

@Configuration
@Profile("dev")
public class ProfileProdConfig {

	@Autowired
	private DBService DBService;

	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;

	@Bean
	public boolean instantiateDataBase() {

		if (!"create".equals(strategy)) {
			return false;
		}
		DBService.instatiateTestDataBase();

		return true;

	}

	@Bean
	public EmailService emnailService() {
		return new SmtpEmailService();
	}

}