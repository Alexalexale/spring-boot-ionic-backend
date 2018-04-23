package com.example.main.resources;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.main.domain.Pedido;
import com.example.main.services.PedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {

	@Autowired
	private PedidoService pedidoService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Pedido> findById(@PathVariable Integer id) {

		return ResponseEntity.ok(pedidoService.findById(id));

	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody Pedido pedido) {
		pedido = pedidoService.insert(pedido);
		return ResponseEntity
				.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(pedido.getId())).build();
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<Pedido>> findAllPage(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "linesPage", defaultValue = "24") Integer linesPage,
			@RequestParam(name = "orderBy", defaultValue = "instante") String orderBy,
			@RequestParam(name = "direction", defaultValue = "DESC") String direction) {

		return ResponseEntity.ok(pedidoService.findPage(page, linesPage, orderBy, direction));

	}
}