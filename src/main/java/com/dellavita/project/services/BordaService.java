package com.dellavita.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.entities.Borda;
import com.dellavita.project.repositories.BordaRepository;

@Service
public class BordaService {

	@Autowired
	private BordaRepository bordaRepository;
	
	@Transactional
	public List<Borda> listar(){
		return bordaRepository.findAll();
	}
	
	@Transactional
	public Borda criar(Borda borda) {
		return bordaRepository.save(borda);
	}
	
	@Transactional
	public Borda atualizar(Long id, Borda  borda) {
		Borda bordaNovo = bordaRepository.findById(id).orElseThrow();
		bordaNovo.setNome(borda.getNome());
		bordaNovo.setValorAdicional(borda.getValorAdicional());
		return bordaRepository.save(bordaNovo);
	}
	
	@Transactional
	public void excluir(Long id) {
		bordaRepository.deleteById(id);
	}
}
