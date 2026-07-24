package com.dellavita.project.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.dto.ClienteCadastroDTO;
import com.dellavita.project.dto.PerfilUpdateDTO;
import com.dellavita.project.entities.Cliente;
import com.dellavita.project.repositories.ClienteRepository;
import com.dellavita.project.repositories.FuncionarioRepository;
import com.dellavita.project.services.exceptions.RecursoNaoEncontradoException;
import com.dellavita.project.services.exceptions.RegistroDuplicadoException;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(
            ClienteRepository clienteRepository,
            FuncionarioRepository funcionarioRepository,
            PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Cliente criar(ClienteCadastroDTO dados) {
        String cpf = normalizarCpf(dados.getCpf());
        String login = dados.getLogin().trim();
        if (clienteRepository.existsById(cpf) || funcionarioRepository.existsById(cpf)) {
            throw new RegistroDuplicadoException("Já existe um cadastro com este CPF.");
        }
        if (clienteRepository.existsByLoginIgnoreCase(login)
                || funcionarioRepository.existsByLoginIgnoreCase(login)) {
            throw new RegistroDuplicadoException("Já existe um cadastro com este e-mail.");
        }

        Cliente cliente = new Cliente(
                cpf,
                dados.getNome().trim(),
                login,
                passwordEncoder.encode(dados.getSenha()),
                dados.getGenero().trim(),
                dados.getTelefone().trim(),
                dados.getEndereco().trim());
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente atualizar(String cpf, PerfilUpdateDTO dadosNovos) {
        validarPerfil(dadosNovos);
        Cliente cliente = clienteRepository.findById(cpf)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado."));
        validarLoginDisponivel(dadosNovos.getLogin(), cliente.getLogin());

        cliente.setNome(dadosNovos.getNome().trim());
        cliente.setLogin(dadosNovos.getLogin().trim());
        cliente.setTelefone(dadosNovos.getTelefone().trim());
        cliente.setEndereco(normalizarOpcional(dadosNovos.getEndereco()));
        atualizarSenha(cliente, dadosNovos.getSenha());
        return clienteRepository.save(cliente);
    }

    @Transactional
    public void excluir(String cpf) {
        Cliente cliente = clienteRepository.findById(cpf)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado."));
        clienteRepository.delete(cliente);
    }

    private void validarPerfil(PerfilUpdateDTO dados) {
        if (dados == null || dados.getNome() == null || dados.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome é obrigatório.");
        }
        if (dados.getLogin() == null || dados.getLogin().isBlank() || !dados.getLogin().contains("@")) {
            throw new IllegalArgumentException("Informe um e-mail válido.");
        }
        if (dados.getTelefone() == null || dados.getTelefone().isBlank()) {
            throw new IllegalArgumentException("O telefone é obrigatório.");
        }
        if (dados.getSenha() != null && !dados.getSenha().isBlank() && dados.getSenha().length() < 6) {
            throw new IllegalArgumentException("A nova senha deve ter pelo menos 6 caracteres.");
        }
    }

    private void validarLoginDisponivel(String novoLogin, String loginAtual) {
        String loginNormalizado = novoLogin.trim();
        if (loginNormalizado.equalsIgnoreCase(loginAtual)) {
            return;
        }
        if (clienteRepository.existsByLoginIgnoreCase(loginNormalizado)
                || funcionarioRepository.existsByLoginIgnoreCase(loginNormalizado)) {
            throw new RegistroDuplicadoException("Já existe um cadastro com este e-mail.");
        }
    }

    private void atualizarSenha(Cliente cliente, String novaSenha) {
        if (novaSenha != null && !novaSenha.isBlank()) {
            cliente.setSenha(passwordEncoder.encode(novaSenha));
        }
    }

    private String normalizarOpcional(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }

    private String normalizarCpf(String cpf) {
        return cpf.replaceAll("\\D", "");
    }
}
