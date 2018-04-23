package com.example.main.services.validation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import com.example.main.domain.enums.TipoCliente;
import com.example.main.dto.ClienteInsertDTO;
import com.example.main.services.validation.groups.PessoaFisica;
import com.example.main.services.validation.groups.PessoaJuridica;

public class ClienteGroupSequenceProvider implements DefaultGroupSequenceProvider<ClienteInsertDTO> {

	@Override
	public List<Class<?>> getValidationGroups(ClienteInsertDTO object) {

		List<Class<?>> groups = new ArrayList<>();

		groups.add(ClienteInsertDTO.class);

		if (object != null) {

			TipoCliente tipoCliente = TipoCliente.toEnum(object.getTipo());

			if (TipoCliente.PESSOA_FISICA.equals(tipoCliente)) {
				groups.add(PessoaFisica.class);
			} else if (TipoCliente.PESSOA_JURIDICA.equals(tipoCliente)) {
				groups.add(PessoaJuridica.class);
			}
		}

		return groups;
	}

}