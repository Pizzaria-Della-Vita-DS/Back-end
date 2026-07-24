package com.dellavita.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;  
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dellavita.project.dto.FuncionarioCadastroDTO;
import com.dellavita.project.dto.UsuarioResponseDTO;
import com.dellavita.project.dto.PerfilUpdateDTO;
import com.dellavita.project.entities.Funcionario;
import com.dellavita.project.services.FuncionarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/funcionarios")
public class FuncionarioController {

	@Autowired
	private FuncionarioService funcionarioService;

	@GetMapping  
	public List<Funcionario> listar() {
		return funcionarioService.listar();
	}

	@GetMapping(value = "/pendentes")  
	public List<Funcionario> listarPendentes() {
		return funcionarioService.listarPendentes();
	}

	@PostMapping
	public ResponseEntity<UsuarioResponseDTO> criar(@Valid @RequestBody FuncionarioCadastroDTO dados) {
		Funcionario salvo = funcionarioService.criar(dados);
		return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponseDTO.fromFuncionario(salvo));
	}

	@PutMapping("/{cpf}")
	public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable String cpf, @RequestBody PerfilUpdateDTO funcionario) {
		Funcionario atualizado = funcionarioService.atualizar(cpf, funcionario);
		return ResponseEntity.ok(UsuarioResponseDTO.fromFuncionario(atualizado));
	}

	@PatchMapping("/{cpf}/aprovar")  
	public ResponseEntity<UsuarioResponseDTO> aprovar(@PathVariable String cpf) {
		Funcionario aprovado = funcionarioService.aprovar(cpf);
		return ResponseEntity.ok(UsuarioResponseDTO.fromFuncionario(aprovado));
	}

	@DeleteMapping("/{cpf}/recusar")  
	public ResponseEntity<Void> recusar(@PathVariable String cpf) {
		funcionarioService.recusar(cpf);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{cpf}")  
	public ResponseEntity<Void> excluir(@PathVariable String cpf) {
		funcionarioService.excluir(cpf);
		return ResponseEntity.noContent().build();
	}
}
