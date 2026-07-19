package com.dellavita.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dellavita.project.dto.LoginRequestDTO;
import com.dellavita.project.dto.UsuarioResponseDTO;
import com.dellavita.project.services.AuthService;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping(value = "/login")
	public ResponseEntity<UsuarioResponseDTO> login(@RequestBody LoginRequestDTO dados) {
		UsuarioResponseDTO resposta = authService.login(dados);
		return ResponseEntity.ok(resposta);
	}
}