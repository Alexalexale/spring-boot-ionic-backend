package com.example.main.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.main.domain.Cidade;
import com.example.main.domain.Cliente;
import com.example.main.domain.Endereco;
import com.example.main.domain.enums.Perfil;
import com.example.main.domain.enums.TipoCliente;
import com.example.main.dto.ClienteInsertDTO;
import com.example.main.repositories.ClienteRepository;
import com.example.main.security.UserSS;
import com.example.main.services.enums.ExtensionAccepted;
import com.example.main.services.exceptions.AuthorizationException;
import com.example.main.services.exceptions.DataIntegrityException;
import com.example.main.services.exceptions.ObjectNotFoundException;
import com.example.main.util.ObjectUtil;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private S3Service s3Service;

	@Autowired
	private ImageService imageService;

	@Value("${img.prefix.client.profile}")
	private String prefix;

	@Value("${img.profile.size}")
	private Integer defaultImgSize;

	public Cliente findById(Integer idCliente) {

		UserSS user = UserService.authenticated();

		if (!user.hasRole(Perfil.ADM) && !idCliente.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado.");
		}

		return clienteRepository.findById(idCliente)
				.orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado."));
	}

	public Cliente findByEmail(String email) {
		UserSS user = UserService.authenticated();

		if (!user.hasRole(Perfil.ADM) && !user.getUsername().equals(email)) {
			throw new AuthorizationException("Acesso negado.");
		}

		return clienteRepository.findByEmail(email)
				.orElseThrow(() -> new ObjectNotFoundException("Cliente não encontrado."));
	}

	@Transactional
	public Cliente insert(Cliente cliente) {
		cliente.setId(null);
		return clienteRepository.save(cliente);
	}

	public Cliente update(Cliente cliente) {
		Cliente newCliente = findById(cliente.getId());
		return clienteRepository.save(ObjectUtil.mergeBasicData(cliente, newCliente));
	}

	public void delete(Integer id) {
		findById(id);
		try {
			clienteRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir uma cliente com pedidos.", e);
		}
	}

	public List<Cliente> findAll() {
		return clienteRepository.findAll();
	}

	public Page<Cliente> findPage(Integer page, Integer linesPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPage, Direction.valueOf(direction), orderBy);
		return clienteRepository.findAll(pageRequest);
	}

	public Cliente fromDto(ClienteInsertDTO clienteInsertDTO) {

		Cliente cliente = ObjectUtil.fromDto(clienteInsertDTO, Cliente.class);
		cliente.setId(null);
		cliente.setSenha(bCryptPasswordEncoder.encode(clienteInsertDTO.getSenha()));
		cliente.setTipo(TipoCliente.toEnum(clienteInsertDTO.getTipo()));

		Endereco endereco = ObjectUtil.fromDto(clienteInsertDTO, Endereco.class);
		endereco.setId(null);
		endereco.setCliente(cliente);
		endereco.setCidade(new Cidade(clienteInsertDTO.getCidadeId()));

		cliente.getEnderecos().add(endereco);

		cliente.getTelefones().addAll(Arrays.asList(clienteInsertDTO.getTelefone1(), clienteInsertDTO.getTelefone2(),
				clienteInsertDTO.getTelefone3()));

		return cliente;
	}

	public URI uploadProfilePicture(MultipartFile multipartFile) {
		String extension = ExtensionAccepted.JPG.getType();
		Cliente cliente = findById(UserService.authenticated().getId());
		BufferedImage jpgImage = imageService
				.resize(imageService.cropSquare(imageService.getJpgImageFromFile(multipartFile)), defaultImgSize);
		String fileName = prefix.concat(cliente.getId().toString()).concat(".").concat(extension);
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, extension), fileName, "image");
	}

}