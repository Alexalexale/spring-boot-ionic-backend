package com.example.main.dto;

import java.io.Serializable;

import com.example.main.domain.Estado;

public class EstadoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	private String nome;

	public EstadoDTO() {
		super();
	}

	public EstadoDTO(Estado estado) {
		this.id = estado.getId();
		this.setNome(estado.getNome());
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

}