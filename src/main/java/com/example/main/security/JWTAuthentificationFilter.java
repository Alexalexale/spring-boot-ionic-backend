package com.example.main.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.main.dto.CredenciaisDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTAuthentificationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	private JWTUtil jwtUtil;

	public JWTAuthentificationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

		try {

			CredenciaisDTO credenciaisDTO = new ObjectMapper().readValue(request.getInputStream(),
					CredenciaisDTO.class);
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					credenciaisDTO.getEmail(), credenciaisDTO.getSenha(), new ArrayList<>());
			return authenticationManager.authenticate(authentication);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		String token = jwtUtil.generateToken(((UserSS) authResult.getPrincipal()).getUsername());
		response.addHeader("Authorization", "Brarer ".concat(token));
		response.addHeader("access-control-expose-headers", "Authorization");

	}
}
