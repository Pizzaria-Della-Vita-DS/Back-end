package com.dellavita.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dellavita.project.entities.Funcionario;
import com.dellavita.project.enums.Status;
import com.dellavita.project.services.FuncionarioService;

@RestController
@RequestMapping(value="/funcionarios")
public class FuncionarioController {

	@Autowired
	private FuncionarioService funcionarioService;
	
	@GetMapping
	public List<Funcionario> listar(){
		return funcionarioService.listar();
	}
	
	@PostMapping
    public Funcionario criar(@RequestBody Funcionario funcionario) {
        return funcionarioService.criar(funcionario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> atualizar(@PathVariable String cpf, @RequestBody Funcionario funcionario) {
        return ResponseEntity.ok(funcionarioService.atualizar(cpf, funcionario));
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<Funcionario> alternar(@PathVariable String cpf, @RequestBody Status status){
    	return ResponseEntity.ok(funcionarioService.alternarStatus(cpf, status));
    }
}