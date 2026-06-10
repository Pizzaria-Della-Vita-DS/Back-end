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
@RequestMapping(value = "/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping
	public List<Usuario> findAll(){
		return usuarioService.findAll();
	}
	
	@GetMapping(value="/{id}")
	public Usuario findById(@PathVariable Long id) {
		return usuarioService.findById(id);
	}
	
	
}
