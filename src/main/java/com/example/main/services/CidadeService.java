package com.example.main.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.main.domain.Cidade;
import com.example.main.repositories.CidadeRepository;

@Service
public class CidadeService {

	@Autowired
	private CidadeRepository cidadeRepository;

	public List<Cidade> findCidadesByIdEstado(Integer idEstado) {
		return cidadeRepository.findByEstadoIdOrderByNome(idEstado);
	}

}