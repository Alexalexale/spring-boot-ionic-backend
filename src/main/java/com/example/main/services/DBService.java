package com.example.main.services;

import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.main.domain.Categoria;
import com.example.main.domain.Cidade;
import com.example.main.domain.Cliente;
import com.example.main.domain.Endereco;
import com.example.main.domain.Estado;
import com.example.main.domain.ItemPedido;
import com.example.main.domain.PagamentoBoleto;
import com.example.main.domain.PagamentoCartao;
import com.example.main.domain.Pedido;
import com.example.main.domain.Produto;
import com.example.main.domain.enums.EstadoPagamento;
import com.example.main.domain.enums.Perfil;
import com.example.main.domain.enums.TipoCliente;
import com.example.main.repositories.CategoriaRepository;
import com.example.main.repositories.CidadeRepository;
import com.example.main.repositories.ClienteRepository;
import com.example.main.repositories.EnderecoRepository;
import com.example.main.repositories.EstadoRepository;
import com.example.main.repositories.ItemPedidoRepository;
import com.example.main.repositories.PagamentoRepository;
import com.example.main.repositories.PedidoRepository;
import com.example.main.repositories.ProdutoRepository;

@Service
public class DBService {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public void instatiateTestDataBase() {

		Categoria informatica = new Categoria(null, "Informatica");
		Categoria escritorio = new Categoria(null, "Escritorio");
		Categoria eletronico = new Categoria(null, "Eletronico");
		Categoria cozinha = new Categoria(null, "Cozinha");
		Categoria roupa = new Categoria(null, "Roupa");
		Categoria jogos = new Categoria(null, "Jogos");

		Produto computador = new Produto(null, "Computador", 2000D);
		Produto impressora = new Produto(null, "Impressora", 800D);
		Produto mouse = new Produto(null, "Mouse", 80D);

		informatica.getProdutos().addAll(Arrays.asList(computador, impressora, mouse));
		escritorio.getProdutos().addAll(Arrays.asList(impressora));

		computador.getCategorias().addAll(Arrays.asList(informatica));
		impressora.getCategorias().addAll(Arrays.asList(informatica, escritorio));
		mouse.getCategorias().addAll(Arrays.asList(informatica));

		Estado mg = new Estado(null, "Minas Gerais");
		Estado sp = new Estado(null, "São Paulo");

		Cidade uberlandia = new Cidade(null, "Uberlândia", mg);
		Cidade cidadeSp = new Cidade(null, "São Paulo", sp);
		Cidade guarulhos = new Cidade(null, "Guarulhos", sp);

		mg.getCidades().add(uberlandia);
		sp.getCidades().addAll(Arrays.asList(cidadeSp, guarulhos));

		Cliente maria = new Cliente(null, "Maria Silva", "alessandro_jump@hotmail.com", "22246013607",
				TipoCliente.PESSOA_FISICA, bCryptPasswordEncoder.encode("teste"));

		Cliente ale = new Cliente(null, "Alessandro Duarte Junior", "alessandroduartejunior@gmail.com", "22246013607",
				TipoCliente.PESSOA_FISICA, bCryptPasswordEncoder.encode("teste"));

		ale.addPerfil(Perfil.ADM);

		maria.getTelefones().addAll(Arrays.asList("29617345", "945132245"));
		ale.getTelefones().addAll(Arrays.asList("29617300", "945137786"));

		Endereco ruaFlores = new Endereco(null, "Rua Flores", "300", "Apto 301", "Jardim", "0855612", maria,
				uberlandia);

		Endereco avenidadeMatos = new Endereco(null, "Avenidade Matos", "45", "Apto 34", "Paulista", "0800012", maria,
				cidadeSp);

		Endereco ruaCruz = new Endereco(null, "Rua Cruz", "45", "Apto 12", "Centro", "0800000", ale, cidadeSp);

		maria.getEnderecos().addAll(Arrays.asList(ruaFlores, avenidadeMatos));
		ale.getEnderecos().addAll(Arrays.asList(ruaCruz));

		Pedido p1 = new Pedido(null, new Date(), maria, ruaFlores);
		Pedido p2 = new Pedido(null, new Date(), maria, avenidadeMatos);

		PagamentoCartao pagamentoP1 = new PagamentoCartao(null, EstadoPagamento.QUITADO, p1, 6);
		p1.setPagamento(pagamentoP1);

		PagamentoBoleto pagamentoP2 = new PagamentoBoleto(null, EstadoPagamento.PENDENTE, p2, new Date(), null);
		p2.setPagamento(pagamentoP2);

		maria.getPedidos().addAll(Arrays.asList(p1, p2));

		ItemPedido computadorPedido1 = new ItemPedido(p1, computador, 0.0, 1, 2000.0);
		ItemPedido mousePedido1 = new ItemPedido(p1, mouse, 0.0, 2, 80.0);
		ItemPedido impressoraPedido2 = new ItemPedido(p2, impressora, 100.0, 1, 800.0);

		p1.getItens().addAll(Arrays.asList(computadorPedido1, mousePedido1));

		p2.getItens().add(impressoraPedido2);

		computador.getItens().add(computadorPedido1);
		mouse.getItens().add(mousePedido1);
		impressora.getItens().add(impressoraPedido2);

		categoriaRepository.saveAll(Arrays.asList(escritorio, informatica, eletronico, cozinha, roupa, jogos));

		produtoRepository.saveAll(Arrays.asList(computador, impressora, mouse));

		estadoRepository.saveAll(Arrays.asList(mg, sp));

		cidadeRepository.saveAll(Arrays.asList(uberlandia, cidadeSp, guarulhos));

		clienteRepository.saveAll(Arrays.asList(maria, ale));

		enderecoRepository.saveAll(Arrays.asList(ruaFlores, avenidadeMatos));

		pedidoRepository.saveAll(Arrays.asList(p1, p2));

		pagamentoRepository.saveAll(Arrays.asList(pagamentoP1, pagamentoP2));

		itemPedidoRepository.saveAll(Arrays.asList(computadorPedido1, mousePedido1, impressoraPedido2));

	}

}
