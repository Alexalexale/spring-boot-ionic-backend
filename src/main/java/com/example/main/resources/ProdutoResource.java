package com.example.main.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.main.domain.Produto;
import com.example.main.dto.ProdutoDTO;
import com.example.main.resources.utils.URL;
import com.example.main.services.ProdutoService;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {

	@Autowired
	private ProdutoService produtoService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Produto> findById(@PathVariable Integer id) {

		return ResponseEntity.ok(produtoService.findById(id));

	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<ProdutoDTO>> findAllPage(@RequestParam(name = "nome", defaultValue = "") String nome,
			@RequestParam(name = "categorias", defaultValue = "") String categorias,
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "linesPage", defaultValue = "24") Integer linesPage,
			@RequestParam(name = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(name = "direction", defaultValue = "ASC") String direction) {

		List<Integer> ids = URL.decodeIntList(categorias);
		String nomeDecode = URL.decodeParam(nome);

		Page<ProdutoDTO> pageProdutos = produtoService.search(nomeDecode, ids, page, linesPage, orderBy, direction)
				.map(ProdutoDTO::new);

		return ResponseEntity.ok(pageProdutos);

	}

}