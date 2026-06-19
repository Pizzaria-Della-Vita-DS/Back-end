package com.dellavita.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.entities.Ingrediente;
import com.dellavita.project.repositories.IngredienteRepository;

@Service
public class IngredienteService {

	@Autowired
	private IngredienteRepository ingredienteRepository;
	
	@Transactional
	public List<Ingrediente> listar(){
		return ingredienteRepository.findAll();
	}
	
	@Transactional
	public Ingrediente criar(Ingrediente ingrediente) {
		return ingredienteRepository.save(ingrediente);
	}
	
	@Transactional
	public Ingrediente atualizar(Long id, Ingrediente ingrediente) {
		Ingrediente ingredienteNovo = ingredienteRepository.findById(id).orElseThrow();
		ingredienteNovo.setNome(ingrediente.getNome());
		ingredienteNovo.setCategoria(ingrediente.getCategoria());
		ingredienteNovo.setDisponivel(ingrediente.isDisponivel());
		return ingredienteRepository.save(ingredienteNovo);
	}
	
	@Transactional
	public Ingrediente alternar(Long id, boolean disponivel) {
		Ingrediente ingredienteAlterado = ingredienteRepository.findById(id).orElseThrow();
		ingredienteAlterado.setDisponivel(disponivel);
		return ingredienteRepository.save(ingredienteAlterado);
	}
	
	@Transactional
	public void excluir(Long id) {
		ingredienteRepository.deleteById(id);
	}
	
}
