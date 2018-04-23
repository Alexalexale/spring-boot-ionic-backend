package com.example.main.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.example.main.domain.Cliente;
import com.example.main.services.validation.EmailRepitido;

public class ClienteDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	@NotEmpty(message = "Preenchimento obrigatório.")
	@Size(max = 120, min = 5, message = "O temanho deve ser entre 5 e 120.")
	private String nome;

	@Email(message = "Email inválido.")
	@NotEmpty(message = "Preenchimento obrigatório.")
	@EmailRepitido(message = "Email já cadastrado.")
	private String email;

	public ClienteDTO() {
		super();
	}

	public ClienteDTO(Cliente cliente) {
		super();
		this.id = cliente.getId();
		this.nome = cliente.getNome();
		this.email = cliente.getEmail();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}