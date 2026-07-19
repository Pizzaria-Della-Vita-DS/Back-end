package com.dellavita.project.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UsuarioResponseDTO login(LoginRequestDTO dados) {
        boolean isFuncionario = "funcionario".equalsIgnoreCase(dados.getTipo());
        logger.info("Tentativa de login: login='{}', tipo='{}'", dados.getLogin(), dados.getTipo());

        if (isFuncionario) {
            Funcionario funcionario = funcionarioRepository.findByLogin(dados.getLogin())
                    .orElseThrow(() -> {
                        logger.warn("Funcionário não encontrado com login: {}", dados.getLogin());
                        return new CredenciaisInvalidasException("E-mail ou senha incorretos.");
                    });

            boolean senhaOk = passwordEncoder.matches(dados.getSenha(), funcionario.getSenha());
            logger.info("Funcionário encontrado. Senha confere? {}", senhaOk);

            if (!senhaOk) {
                throw new CredenciaisInvalidasException("E-mail ou senha incorretos.");
            }
            return UsuarioResponseDTO.fromFuncionario(funcionario);
        }

        Cliente cliente = clienteRepository.findByLogin(dados.getLogin())
                .orElseThrow(() -> {
                    logger.warn("Cliente não encontrado com login: {}", dados.getLogin());
                    return new CredenciaisInvalidasException("E-mail ou senha incorretos.");
                });

        boolean senhaOk = passwordEncoder.matches(dados.getSenha(), cliente.getSenha());
        logger.info("Cliente encontrado. Senha confere? {}", senhaOk);

        if (!senhaOk) {
            throw new CredenciaisInvalidasException("E-mail ou senha incorretos.");
        }
        return UsuarioResponseDTO.fromCliente(cliente);
    }
}