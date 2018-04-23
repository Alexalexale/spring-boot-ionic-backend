package com.example.main.resources;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.main.domain.Categoria;
import com.example.main.dto.CategoriaDTO;
import com.example.main.services.CategoriaService;
import com.example.main.util.ObjectUtil;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService categoriaService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Categoria> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(categoriaService.findById(id));
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO categoriaDTO) {
		Categoria categoria = ObjectUtil.fromDto(categoriaDTO, Categoria.class);
		categoria = categoriaService.insert(categoria);
		return ResponseEntity
				.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(categoria.getId()))
				.build();
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO categoriaDTO, @PathVariable Integer id) {
		Categoria categoria = ObjectUtil.fromDto(categoriaDTO, Categoria.class);
		categoria.setId(id);
		categoria = categoriaService.update(categoria);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<CategoriaDTO>> findAll() {
		return ResponseEntity
				.ok(categoriaService.findAll().stream().map(CategoriaDTO::new).collect(Collectors.toList()));
	}

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<CategoriaDTO>> findAllPage(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "linesPage", defaultValue = "24") Integer linesPage,
			@RequestParam(name = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(name = "direction", defaultValue = "ASC") String direction) {

		return ResponseEntity.ok(categoriaService.findPage(page, linesPage, orderBy, direction).map(CategoriaDTO::new));

	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		categoriaService.delete(id);
		return ResponseEntity.noContent().build();
	}

}