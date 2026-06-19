package com.dellavita.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.entities.Funcionario;
import com.dellavita.project.enums.Status;
import com.dellavita.project.repositories.FuncionarioRepository;

@Service
public class FuncionarioService {

	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Transactional
	public List<Funcionario> listar(){
		return funcionarioRepository.findAll();
	}
	
	@Transactional
	public Funcionario criar(Funcionario funcionario) {
		return funcionarioRepository.save(funcionario);
	}
	
	@Transactional
	public Funcionario atualizar(String cpf, Funcionario funcionario) {
		Funcionario funcionarioNovo = funcionarioRepository.findById(cpf).orElseThrow();
		funcionarioNovo.setNome(funcionario.getNome());
		funcionarioNovo.setEmail(funcionario.getEmail());
		funcionarioNovo.setFuncao(funcionario.getFuncao());
		funcionarioNovo.setSenha(funcionario.getSenha());
		funcionarioNovo.setStatus(funcionario.getStatus());
		return funcionarioRepository.save(funcionarioNovo);
	}
	
	@Transactional
	public Funcionario alternarStatus(String cpf, Status status) {
		Funcionario funcionarioAlterado = funcionarioRepository.findById(cpf).orElseThrow();
		funcionarioAlterado.setStatus(status);
		return funcionarioRepository.save(funcionarioAlterado);
	}
}