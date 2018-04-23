package com.example.main.resources;

import java.net.URI;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.main.domain.Cliente;
import com.example.main.dto.ClienteDTO;
import com.example.main.dto.ClienteInsertDTO;
import com.example.main.services.ClienteService;
import com.example.main.util.ObjectUtil;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

	@Autowired
	private ClienteService clienteService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Cliente> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(clienteService.findById(id));
	}

	@RequestMapping(value = "/email", method = RequestMethod.GET)
	public ResponseEntity<Cliente> findByEmail(@RequestParam(name = "email") String email) {
		return ResponseEntity.ok(clienteService.findByEmail(email));
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody ClienteInsertDTO clienteInsertDTO) {
		Cliente cliente = clienteService.fromDto(clienteInsertDTO);
		cliente = clienteService.insert(cliente);
		return ResponseEntity
				.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(cliente.getId())).build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO categoriaDTO, @PathVariable Integer id) {
		Cliente categoria = ObjectUtil.fromDto(categoriaDTO, Cliente.class);
		categoria.setId(id);
		categoria = clienteService.update(categoria);
		return ResponseEntity.noContent().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ClienteDTO>> findAll() {
		return ResponseEntity.ok(clienteService.findAll().stream().map(ClienteDTO::new).collect(Collectors.toList()));
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<ClienteDTO>> findAllPage(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "linesPage", defaultValue = "24") Integer linesPage,
			@RequestParam(name = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(name = "direction", defaultValue = "ASC") String direction) {

		return ResponseEntity.ok(clienteService.findPage(page, linesPage, orderBy, direction).map(ClienteDTO::new));

	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		clienteService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/picture", method = RequestMethod.POST)
	public ResponseEntity<Void> uploadProfilePicture(@RequestParam(name = "file") MultipartFile multipartFile) {
		URI uri = clienteService.uploadProfilePicture(multipartFile);
		return ResponseEntity.created(uri).build();
	}

}