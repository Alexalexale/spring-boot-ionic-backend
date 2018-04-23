package com.example.main.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.main.dto.CidadeDTO;
import com.example.main.dto.EstadoDTO;
import com.example.main.services.CidadeService;
import com.example.main.services.EstadoService;

@RestController
@RequestMapping(value = "/estados")
public class EstadoResource {

	@Autowired
	private EstadoService estadoService;

	@Autowired
	private CidadeService cidadeService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<EstadoDTO>> findAll() {
		return ResponseEntity.ok(estadoService.findAll().stream().map(EstadoDTO::new).collect(Collectors.toList()));
	}

	@RequestMapping(value = "/{estado_id}/cidades", method = RequestMethod.GET)
	public ResponseEntity<List<CidadeDTO>> findCidadesEstado(@PathVariable(name = "estado_id") Integer idEstado) {
		return ResponseEntity.ok(cidadeService.findCidadesByIdEstado(idEstado).stream().map(CidadeDTO::new).collect(Collectors.toList()));
	}

}