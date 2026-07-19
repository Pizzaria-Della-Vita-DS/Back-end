package com.dellavita.project.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.dto.UsuarioResponseDTO;
import com.dellavita.project.repositories.ClienteRepository;
import com.dellavita.project.repositories.FuncionarioRepository;
import com.dellavita.project.services.exceptions.RecursoNaoEncontradoException;

@Service
public class UsuarioService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Transactional(readOnly = true)
	public List<UsuarioResponseDTO> findAll() {
		Stream<UsuarioResponseDTO> clientes = clienteRepository.findAll().stream()
				.map(UsuarioResponseDTO::fromCliente);

		Stream<UsuarioResponseDTO> funcionarios = funcionarioRepository.findAll().stream()
				.map(UsuarioResponseDTO::fromFuncionario);

		return Stream.concat(clientes, funcionarios).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public UsuarioResponseDTO findByCpf(String cpf) {
		return clienteRepository.findById(cpf)
				.map(UsuarioResponseDTO::fromCliente)
				.or(() -> funcionarioRepository.findById(cpf).map(UsuarioResponseDTO::fromFuncionario))
				.orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com o CPF informado."));
	}

}