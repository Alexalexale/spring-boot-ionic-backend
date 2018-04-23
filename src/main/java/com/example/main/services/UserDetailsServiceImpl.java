package com.example.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.main.domain.Cliente;
import com.example.main.repositories.ClienteRepository;
import com.example.main.security.UserSS;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Cliente cliente = clienteRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
		return new UserSS(cliente.getId(), cliente.getEmail(), cliente.getSenha(), cliente.getPerfis());
	}

}