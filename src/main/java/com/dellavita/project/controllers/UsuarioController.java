package com.dellavita.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dellavita.project.entities.Usuario;
import com.dellavita.project.services.UsuarioService;

@RestController
@RequestMapping(value = "/api/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping
	public List<Usuario> findAll(){
		return usuarioService.findAll();
	}
	
	@GetMapping(value="/{id}")
	public Usuario findById(@PathVariable String cpf) {
		return usuarioService.findById(cpf);
	}
	
	
}
