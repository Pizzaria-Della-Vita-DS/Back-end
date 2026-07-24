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
        validarAtualizacaoInformada(dadosNovos);
        Cliente cliente = clienteRepository.findById(cpf)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado."));

        if (dadosNovos.getNome() != null) {
            cliente.setNome(validarTextoObrigatorio(dadosNovos.getNome(), "O nome é obrigatório."));
        }
        if (dadosNovos.getLogin() != null) {
            String login = validarEmail(dadosNovos.getLogin());
            validarLoginDisponivel(login, cliente.getLogin());
            cliente.setLogin(login);
        }
        if (dadosNovos.getTelefone() != null) {
            cliente.setTelefone(validarTextoObrigatorio(
                    dadosNovos.getTelefone(),
                    "O telefone é obrigatório."));
        }
        if (dadosNovos.getEndereco() != null) {
            cliente.setEndereco(normalizarOpcional(dadosNovos.getEndereco()));
        }
        if (dadosNovos.getGenero() != null) {
            cliente.setGenero(validarTextoObrigatorio(
                    dadosNovos.getGenero(),
                    "O gênero é obrigatório."));
        }
        if (dadosNovos.getSenha() != null) {
            atualizarSenha(cliente, dadosNovos.getSenha());
        }
        return clienteRepository.save(cliente);
    }

    @Transactional
    public void excluir(String cpf) {
        Cliente cliente = clienteRepository.findById(cpf)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado."));
        clienteRepository.delete(cliente);
    }

    private void validarAtualizacaoInformada(PerfilUpdateDTO dados) {
        if (dados == null || (
                dados.getNome() == null
                && dados.getLogin() == null
                && dados.getTelefone() == null
                && dados.getEndereco() == null
                && dados.getSenha() == null
                && dados.getGenero() == null)) {
            throw new IllegalArgumentException("Informe o campo que deseja atualizar.");
        }
    }

    private void validarLoginDisponivel(String novoLogin, String loginAtual) {
        if (novoLogin.equalsIgnoreCase(loginAtual)) {
            return;
        }
        if (clienteRepository.existsByLoginIgnoreCase(novoLogin)
                || funcionarioRepository.existsByLoginIgnoreCase(novoLogin)) {
            throw new RegistroDuplicadoException("Já existe um cadastro com este e-mail.");
        }
    }

    private void atualizarSenha(Cliente cliente, String novaSenha) {
        if (novaSenha.isBlank() || novaSenha.length() < 6) {
            throw new IllegalArgumentException("A nova senha deve ter pelo menos 6 caracteres.");
        }
        cliente.setSenha(passwordEncoder.encode(novaSenha));
    }

    private String validarEmail(String email) {
        String normalizado = email.trim();
        if (normalizado.isEmpty() || !normalizado.contains("@")) {
            throw new IllegalArgumentException("Informe um e-mail válido.");
        }
        return normalizado;
    }

    private String validarTextoObrigatorio(String valor, String mensagem) {
        if (valor.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
        return valor.trim();
    }

    private String normalizarOpcional(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }

    private String normalizarCpf(String cpf) {
        return cpf.replaceAll("\\D", "");
    }
}
