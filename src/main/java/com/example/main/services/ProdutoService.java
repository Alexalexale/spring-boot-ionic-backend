package com.example.main.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.example.main.domain.Categoria;
import com.example.main.domain.Produto;
import com.example.main.repositories.CategoriaRepository;
import com.example.main.repositories.ProdutoRepository;
import com.example.main.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	public Produto findById(Integer idProduto) {
		return produtoRepository.findById(idProduto)
				.orElseThrow(() -> new ObjectNotFoundException("Produto não encontrado."));
	}

	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPage, String orderBy,
			String direction) {

		PageRequest pageRequest = PageRequest.of(page, linesPage, Direction.valueOf(direction), orderBy);

		List<Categoria> findAllById = new ArrayList<>();

		if (ids.isEmpty()) {
			findAllById = categoriaRepository.findAll();
		} else {
			findAllById = categoriaRepository.findAllById(ids);
		}

		return produtoRepository.findDistinctByNomeContainingAndCategoriasIn(nome, findAllById, pageRequest);

	}
}