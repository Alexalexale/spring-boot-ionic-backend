package com.example.main.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.example.main.domain.PagamentoBoleto;
import com.example.main.domain.Pedido;
import com.example.main.domain.Produto;
import com.example.main.domain.enums.EstadoPagamento;
import com.example.main.repositories.ItemPedidoRepository;
import com.example.main.repositories.PagamentoRepository;
import com.example.main.repositories.PedidoRepository;
import com.example.main.security.UserSS;
import com.example.main.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private BoletoService boletoService;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private EmailService emailService;

	public Pedido findById(Integer idCategoria) {
		return pedidoRepository.findById(idCategoria)
				.orElseThrow(() -> new ObjectNotFoundException("Categoria não encontrado."));
	}

	public Pedido insert(Pedido pedido) {

		pedido.setId(null);
		pedido.setInstante(new Date());
		pedido.getPagamento().setId(null);
		pedido.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		pedido.getPagamento().setPedido(pedido);

		if (pedido.getPagamento() instanceof PagamentoBoleto) {
			boletoService.calcularDataVencimento((PagamentoBoleto) pedido.getPagamento(), pedido.getInstante());
		}

		pedidoRepository.save(pedido);

		pagamentoRepository.save(pedido.getPagamento());

		pedido.getItens().forEach(item -> {
			item.setDesconto(0.0);
			Produto produto = produtoService.findById(item.getProduto().getId());
			item.setProduto(produto);
			item.setPreco(produto.getPreco());
			item.setPedido(pedido);
		});

		itemPedidoRepository.saveAll(pedido.getItens());

		pedido.setCliente(clienteService.findById(pedido.getCliente().getId()));

		emailService.senderOrderConfirmationHtmlEmail(pedido);

		return pedido;
	}

	public Page<Pedido> findPage(Integer page, Integer linesPage, String orderBy, String direction) {

		UserSS user = UserService.authenticated();

		PageRequest pageRequest = PageRequest.of(page, linesPage, Direction.valueOf(direction), orderBy);

		return pedidoRepository.findByCliente(clienteService.findById(user.getId()), pageRequest);
	}
}