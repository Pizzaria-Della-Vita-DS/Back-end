package com.dellavita.project.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.entities.Funcionario;
import com.dellavita.project.enums.Funcao;
import com.dellavita.project.enums.Status;
import com.dellavita.project.repositories.FuncionarioRepository;

@Service
public class FuncionarioService {

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	// Lista apenas quem já foi avaliado (ativo ou afastado); quem está
	// pendente de aprovação só aparece na fila de solicitações.
	@Transactional
	public List<Funcionario> listar() {
		return funcionarioRepository.findAll().stream()
			.filter(f -> f.getStatus() != Status.EM_VALIDAÇÃO)
			.collect(Collectors.toList());
	}

	@Transactional
	public List<Funcionario> listarPendentes() {
		return funcionarioRepository.findByStatus(Status.EM_VALIDAÇÃO);
	}

	@Transactional
	public Funcionario criar(Funcionario funcionario) {
		// Ninguém pode se autopromover a Gerente pelo cadastro público.
		if (funcionario.getFuncao() == Funcao.GERENTE) {
			throw new IllegalArgumentException("Não é possível se cadastrar como Gerente.");
		}
		// Todo cadastro nasce pendente, independente do que vier no payload.
		funcionario.setStatus(Status.EM_VALIDAÇÃO);
		return funcionarioRepository.save(funcionario);
	}

	@Transactional
	public Funcionario aprovar(String cpf) {
		Funcionario funcionario = funcionarioRepository.findById(cpf).orElseThrow();
		funcionario.setStatus(Status.ATIVO);
		return funcionarioRepository.save(funcionario);
	}

	@Transactional
	public void recusar(String cpf) {
		funcionarioRepository.deleteById(cpf);
	}

	@Transactional
	public void excluir(String cpf) {
		funcionarioRepository.deleteById(cpf);
	}
}