package com.dellavita.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.entities.Funcionario;
import com.dellavita.project.entities.Solicitacao;
import com.dellavita.project.enums.Funcao;
import com.dellavita.project.enums.Status;
import com.dellavita.project.repositories.FuncionarioRepository;
import com.dellavita.project.repositories.SolicitacaoRepository;

@Service
public class SolicitacaoService {

	@Autowired
	private SolicitacaoRepository solicitacaoRepository;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	

	@Transactional
	public List<Solicitacao> listar(){
		return solicitacaoRepository.findAll();
	}
	
	@Transactional
	public Solicitacao criar(Solicitacao solicitacao) {
		return solicitacaoRepository.save(solicitacao);
	}
	
	@Transactional
	public Funcionario aprovar(Long id) {
		Solicitacao solicitacaoAprovada = solicitacaoRepository.findById(id).orElseThrow();
		String cpf = solicitacaoAprovada.getCpf();
		String nome = solicitacaoAprovada.getNome();
		String email = solicitacaoAprovada.getEmail();
		String senha = solicitacaoAprovada.getSenha();
		Funcao funcao = solicitacaoAprovada.getFuncao();
		Funcionario funcionarioNovo = new Funcionario(cpf, nome, email, senha, funcao, Status.ATIVO);
		return funcionarioRepository.save(funcionarioNovo);
	}
	
	@Transactional
	public void recusar(Long id) {
		solicitacaoRepository.deleteById(id);
	}
}
