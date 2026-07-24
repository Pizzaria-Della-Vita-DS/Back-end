package com.dellavita.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dellavita.project.dto.ClienteCadastroDTO;
import com.dellavita.project.dto.UsuarioResponseDTO;
import com.dellavita.project.dto.PerfilUpdateDTO;
import com.dellavita.project.entities.Cliente;
import com.dellavita.project.services.ClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@Valid @RequestBody ClienteCadastroDTO dados) {
        Cliente salvo = clienteService.criar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponseDTO.fromCliente(salvo));
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable String cpf, @RequestBody PerfilUpdateDTO cliente) {
        Cliente atualizado = clienteService.atualizar(cpf, cliente);
        return ResponseEntity.ok(UsuarioResponseDTO.fromCliente(atualizado));
    }

    @PatchMapping("/{cpf}")
    public ResponseEntity<UsuarioResponseDTO> atualizarParcialmente(
            @PathVariable String cpf,
            @RequestBody PerfilUpdateDTO cliente) {
        Cliente atualizado = clienteService.atualizar(cpf, cliente);
        return ResponseEntity.ok(UsuarioResponseDTO.fromCliente(atualizado));
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> excluir(@PathVariable String cpf) {
        clienteService.excluir(cpf);
        return ResponseEntity.noContent().build();
    }
}
