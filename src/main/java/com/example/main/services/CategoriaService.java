package com.example.main.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.example.main.domain.Categoria;
import com.example.main.repositories.CategoriaRepository;
import com.example.main.services.exceptions.DataIntegrityException;
import com.example.main.services.exceptions.ObjectNotFoundException;
import com.example.main.util.ObjectUtil;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;

	public Categoria findById(Integer idCategoria) {
		return categoriaRepository.findById(idCategoria)
				.orElseThrow(() -> new ObjectNotFoundException("Categoria não encontrado."));
	}

	public Categoria insert(Categoria categoria) {
		categoria.setId(null);
		return categoriaRepository.save(categoria);
	}

	public Categoria update(Categoria categoria) {
		Categoria newCategoria = findById(categoria.getId());
		return categoriaRepository.save(ObjectUtil.mergeBasicData(categoria, newCategoria));
	}

	public void delete(Integer id) {
		findById(id);
		try {
			categoriaRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir uma categoria com produtos", e);
		}
	}

	public List<Categoria> findAll() {
		return categoriaRepository.findAll();
	}

	public Page<Categoria> findPage(Integer page, Integer linesPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPage, Direction.valueOf(direction), orderBy);
		return categoriaRepository.findAll(pageRequest);
	}

}