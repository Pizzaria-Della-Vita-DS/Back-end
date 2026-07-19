package com.dellavita.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dellavita.project.dto.UsuarioResponseDTO;
import com.dellavita.project.entities.Funcionario;
import com.dellavita.project.services.FuncionarioService;

@RestController
@RequestMapping(value = "/api/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@RequestBody Funcionario funcionario) {
        Funcionario salvo = funcionarioService.criar(funcionario);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponseDTO.fromFuncionario(salvo));
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable String cpf, @RequestBody Funcionario funcionario) {
        Funcionario atualizado = funcionarioService.atualizar(cpf, funcionario);
        return ResponseEntity.ok(UsuarioResponseDTO.fromFuncionario(atualizado));
    }
}