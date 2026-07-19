package com.dellavita.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dellavita.project.entities.Funcionario;
import com.dellavita.project.services.FuncionarioService;

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
	public Funcionario criar(@RequestBody Funcionario funcionario) {
		return funcionarioService.criar(funcionario);
	}

	@PatchMapping("/{cpf}/aprovar")
	public ResponseEntity<Funcionario> aprovar(@PathVariable String cpf) {
		return ResponseEntity.ok(funcionarioService.aprovar(cpf));
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