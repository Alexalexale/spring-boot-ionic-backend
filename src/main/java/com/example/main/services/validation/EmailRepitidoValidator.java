package com.example.main.services.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.main.repositories.ClienteRepository;

public class EmailRepitidoValidator implements ConstraintValidator<EmailRepitido, String> {

	@Autowired
	private ClienteRepository clienteRepository;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		return !clienteRepository.findByEmail(value).isPresent();
	}

}
