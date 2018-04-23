package com.example.main.domain.enums;

import java.util.Arrays;

public enum EstadoPagamento {

	PENDENTE(1, "Pendente"), QUITADO(2, "Quitado"), CANCELADO(2, "Cancelado");

	private Integer cod;
	private String descricao;

	private EstadoPagamento(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}

	public Integer getCod() {
		return cod;
	}

	public String getDescricao() {
		return descricao;
	}

	public static EstadoPagamento toEnum(Integer cod) {
		return Arrays.asList(EstadoPagamento.values()).stream().filter(tipo -> tipo.getCod().equals(cod)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Id inválido."));
	}

}
