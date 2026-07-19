package com.dellavita.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.dto.LoginRequestDTO;
import com.dellavita.project.dto.UsuarioResponseDTO;
import com.dellavita.project.entities.Cliente;
import com.dellavita.project.entities.Funcionario;
import com.dellavita.project.repositories.ClienteRepository;
import com.dellavita.project.repositories.FuncionarioRepository;
import com.dellavita.project.services.exceptions.CredenciaisInvalidasException;

@Service
public class AuthService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UsuarioResponseDTO login(LoginRequestDTO dados) {
        boolean isFuncionario = "funcionario".equalsIgnoreCase(dados.getTipo());

        if (isFuncionario) {
            Funcionario funcionario = funcionarioRepository.findByLogin(dados.getLogin())
                    .orElseThrow(() -> new CredenciaisInvalidasException("E-mail ou senha incorretos."));

            if (!passwordEncoder.matches(dados.getSenha(), funcionario.getSenha())) {
                throw new CredenciaisInvalidasException("E-mail ou senha incorretos.");
            }
            return UsuarioResponseDTO.fromFuncionario(funcionario);
        }

        Cliente cliente = clienteRepository.findByLogin(dados.getLogin())
                .orElseThrow(() -> new CredenciaisInvalidasException("E-mail ou senha incorretos."));

        if (!passwordEncoder.matches(dados.getSenha(), cliente.getSenha())) {
            throw new CredenciaisInvalidasException("E-mail ou senha incorretos.");
        }
        return UsuarioResponseDTO.fromCliente(cliente);
    }
}