package com.example.main.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.main.domain.Categoria;
import com.example.main.domain.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

	Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias,
			Pageable pageable);

}
