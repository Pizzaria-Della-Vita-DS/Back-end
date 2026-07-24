
package com.dellavita.project.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.dto.LoginRequestDTO;
import com.dellavita.project.dto.UsuarioResponseDTO;
import com.dellavita.project.entities.Cliente;
import com.dellavita.project.entities.Funcionario;
import com.dellavita.project.enums.Status;
import com.dellavita.project.repositories.ClienteRepository;
import com.dellavita.project.repositories.FuncionarioRepository;
import com.dellavita.project.services.exceptions.ContaNaoAprovadaException;
import com.dellavita.project.services.exceptions.CredenciaisInvalidasException;

@Service
public class AuthService {

    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            ClienteRepository clienteRepository,
            FuncionarioRepository funcionarioRepository,
            PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO login(LoginRequestDTO dados) {
        if (dados == null
                || dados.getLogin() == null
                || dados.getLogin().isBlank()
                || dados.getSenha() == null
                || dados.getSenha().isBlank()) {
            throw new CredenciaisInvalidasException("E-mail ou senha incorretos.");
        }

        String login = dados.getLogin().trim();
        Cliente cliente = clienteRepository.findByLoginIgnoreCase(login).orElse(null);
        if (cliente != null && passwordEncoder.matches(dados.getSenha(), cliente.getSenha())) {
            return UsuarioResponseDTO.fromCliente(cliente);
        }

        Funcionario funcionario = funcionarioRepository.findByLoginIgnoreCase(login).orElse(null);
        if (funcionario != null && passwordEncoder.matches(dados.getSenha(), funcionario.getSenha())) {
            if (funcionario.getStatus() != Status.ATIVO) {
                throw new ContaNaoAprovadaException("Cadastro pendente de aprovação ou inativo.");
            }
            return UsuarioResponseDTO.fromFuncionario(funcionario);
        }

        throw new CredenciaisInvalidasException("E-mail ou senha incorretos.");
    }
}
