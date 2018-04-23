package com.example.main.domain.enums;

import java.util.Arrays;

public enum Perfil {

	ADM(1, "ROLE_ADMIN"), CLIENTE(2, "ROLE_CLIENTE");

	private Integer cod;
	private String descricao;

	private Perfil(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}

	public Integer getCod() {
		return cod;
	}

	public String getDescricao() {
		return descricao;
	}

	public static Perfil toEnum(Integer cod) {
		return Arrays.asList(Perfil.values()).stream().filter(tipo -> tipo.getCod().equals(cod)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Id inválido."));
	}

}
