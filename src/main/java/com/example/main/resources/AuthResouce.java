package com.example.main.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.main.dto.EmailDTO;
import com.example.main.security.JWTUtil;
import com.example.main.security.UserSS;
import com.example.main.services.AuthService;
import com.example.main.services.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthResouce {

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private AuthService authService;

	@RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSS user = UserService.authenticated();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Brarer ".concat(token));
		response.addHeader("access-control-expose-headers", "Authorization");
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/forgot", method = RequestMethod.POST)
	public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO emailDTO) {
		authService.sendNewPassoword(emailDTO.getEmail());
		return ResponseEntity.noContent().build();
	}

}