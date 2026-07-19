package com.dellavita.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.entities.Cliente;
import com.dellavita.project.repositories.ClienteRepository;
import com.dellavita.project.repositories.FuncionarioRepository;
import com.dellavita.project.services.exceptions.RegistroDuplicadoException;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Cliente criar(Cliente cliente) {
        if (clienteRepository.existsById(cliente.getCpf()) || funcionarioRepository.existsById(cliente.getCpf())) {
            throw new RegistroDuplicadoException("Já existe um cadastro com este CPF.");
        }
        if (clienteRepository.existsByLogin(cliente.getLogin()) || funcionarioRepository.existsByLogin(cliente.getLogin())) {
            throw new RegistroDuplicadoException("Já existe um cadastro com este e-mail.");
        }

        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente atualizar(String cpf, Cliente dadosNovos) {
        Cliente clienteExistente = clienteRepository.findById(cpf).orElseThrow();
        clienteExistente.setNome(dadosNovos.getNome());
        clienteExistente.setTelefone(dadosNovos.getTelefone());
        clienteExistente.setGenero(dadosNovos.getGenero());
        clienteExistente.setEndereco(dadosNovos.getEndereco());
        return clienteRepository.save(clienteExistente);
    }
}