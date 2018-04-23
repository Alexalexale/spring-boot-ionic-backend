package com.example.main.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.example.main.security.UserSS;
import com.example.main.services.exceptions.AuthorizationException;

public class UserService {

	public static UserSS authenticated() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			throw new AuthorizationException("Usuário não logado.");
		}
	}

}
