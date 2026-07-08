package com.dellavita.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.repositories.UsuarioRepository;
import com.dellavita.project.entities.*;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Transactional
	public List<Usuario> findAll(){
		return usuarioRepository.findAll();
	}
	
	@Transactional
	public Usuario findById(String cpf) {
		return usuarioRepository.findById(cpf).get();
	}
	
}
