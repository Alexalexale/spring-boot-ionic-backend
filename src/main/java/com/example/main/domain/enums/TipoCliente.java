package com.example.main.domain.enums;

import java.util.Arrays;

public enum TipoCliente {

	PESSOA_FISICA(1, "Pessoa Física"), PESSOA_JURIDICA(2, "Pessoa Jurídica");

	private Integer cod;
	private String descricao;

	private TipoCliente(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}

	public Integer getCod() {
		return cod;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoCliente toEnum(Integer cod) {
		return Arrays.asList(TipoCliente.values()).stream().filter(tipo -> tipo.getCod().equals(cod)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Id inválido."));
	}
}