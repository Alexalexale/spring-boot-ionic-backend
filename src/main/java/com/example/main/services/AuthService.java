package com.example.main.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.main.domain.Cliente;
import com.example.main.repositories.ClienteRepository;
import com.example.main.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public void sendNewPassoword(String email) {
		Cliente cliente = clienteRepository.findByEmail(email)
				.orElseThrow(() -> new ObjectNotFoundException("Email não cadastrado."));

		String newPassword = newPassword();

		cliente.setSenha(bCryptPasswordEncoder.encode(newPassword));

		clienteRepository.save(cliente);

		emailService.sendNewPasswordEmail(cliente, newPassword);
	}

	private String newPassword() {
		char[] vet = new char[10];
		for (int i = 0; i < vet.length; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	}

	private char randomChar() {
		Random random = new Random();
		int opt = random.nextInt(3);
		switch (opt) {
		case 0:
			return (char) (random.nextInt(10) + 48);

		case 1:
			return (char) (random.nextInt(26) + 65);

		default:
			return (char) (random.nextInt(26) + 97);
		}
	}

}
