
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
        if (clienteRepository.existsByLoginIgnoreCase(cliente.getLogin()) || funcionarioRepository.existsByLoginIgnoreCase(cliente.getLogin())) {
            throw new RegistroDuplicadoException("Já existe um cadastro com este e-mail.");
        }

        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente atualizar(String cpf, Cliente dadosNovos) {
        Cliente clienteExistente = clienteRepository.findById(cpf).orElseThrow();

        if (dadosNovos.getLogin() != null && !dadosNovos.getLogin().isBlank()
                && !dadosNovos.getLogin().equalsIgnoreCase(clienteExistente.getLogin())) {
            if (clienteRepository.existsByLoginIgnoreCase(dadosNovos.getLogin())
                    || funcionarioRepository.existsByLoginIgnoreCase(dadosNovos.getLogin())) {
                throw new RegistroDuplicadoException("Já existe um cadastro com este e-mail.");
            }
            clienteExistente.setLogin(dadosNovos.getLogin());
        }

        clienteExistente.setNome(dadosNovos.getNome());
        clienteExistente.setTelefone(dadosNovos.getTelefone());
        clienteExistente.setGenero(dadosNovos.getGenero());
        clienteExistente.setEndereco(dadosNovos.getEndereco());

        if (dadosNovos.getSenha() != null && !dadosNovos.getSenha().isBlank()) {
            clienteExistente.setSenha(passwordEncoder.encode(dadosNovos.getSenha()));
        }

        return clienteRepository.save(clienteExistente);
    }
}