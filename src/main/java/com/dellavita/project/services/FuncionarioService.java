package com.dellavita.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.entities.Funcionario;
import com.dellavita.project.repositories.ClienteRepository;
import com.dellavita.project.repositories.FuncionarioRepository;
import com.dellavita.project.services.exceptions.RegistroDuplicadoException;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Funcionario criar(Funcionario funcionario) {
        if (funcionarioRepository.existsById(funcionario.getCpf()) || clienteRepository.existsById(funcionario.getCpf())) {
            throw new RegistroDuplicadoException("Já existe um cadastro com este CPF.");
        }
        if (funcionarioRepository.existsByLogin(funcionario.getLogin()) || clienteRepository.existsByLogin(funcionario.getLogin())) {
            throw new RegistroDuplicadoException("Já existe um cadastro com este e-mail.");
        }

        funcionario.setSenha(passwordEncoder.encode(funcionario.getSenha()));
        return funcionarioRepository.save(funcionario);
    }

    @Transactional
    public Funcionario atualizar(String cpf, Funcionario dadosNovos) {
        Funcionario funcionarioExistente = funcionarioRepository.findById(cpf).orElseThrow();
        funcionarioExistente.setNome(dadosNovos.getNome());
        funcionarioExistente.setTelefone(dadosNovos.getTelefone());
        funcionarioExistente.setGenero(dadosNovos.getGenero());
        funcionarioExistente.setFuncao(dadosNovos.getFuncao());
        funcionarioExistente.setSetor(dadosNovos.getSetor());
        return funcionarioRepository.save(funcionarioExistente);
    }
}