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
import com.dellavita.project.entities.Cliente;
import com.dellavita.project.services.ClienteService;

@RestController
@RequestMapping(value = "/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@RequestBody Cliente cliente) {
        Cliente salvo = clienteService.criar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponseDTO.fromCliente(salvo));
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable String cpf, @RequestBody Cliente cliente) {
        Cliente atualizado = clienteService.atualizar(cpf, cliente);
        return ResponseEntity.ok(UsuarioResponseDTO.fromCliente(atualizado));
    }
}