package com.dellavita.project.services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.dto.FuncionarioCadastroDTO;
import com.dellavita.project.dto.PerfilUpdateDTO;
import com.dellavita.project.entities.Funcionario;
import com.dellavita.project.enums.Funcao;
import com.dellavita.project.enums.Status;
import com.dellavita.project.repositories.ClienteRepository;
import com.dellavita.project.repositories.FuncionarioRepository;
import com.dellavita.project.services.exceptions.RecursoNaoEncontradoException;
import com.dellavita.project.services.exceptions.RegistroDuplicadoException;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public FuncionarioService(
            FuncionarioRepository funcionarioRepository,
            ClienteRepository clienteRepository,
            PasswordEncoder passwordEncoder) {
        this.funcionarioRepository = funcionarioRepository;
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<Funcionario> listar() {
        return funcionarioRepository.findAll().stream()
                .filter(funcionario -> funcionario.getStatus() != Status.EM_VALIDAÇÃO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Funcionario> listarPendentes() {
        return funcionarioRepository.findByStatus(Status.EM_VALIDAÇÃO);
    }

    @Transactional
    public Funcionario criar(FuncionarioCadastroDTO dados) {
        if (dados.getFuncao() == Funcao.GERENTE) {
            throw new IllegalArgumentException("Não é possível se cadastrar como gerente.");
        }
        String cpf = normalizarCpf(dados.getCpf());
        String login = dados.getLogin().trim();
        if (funcionarioRepository.existsById(cpf) || clienteRepository.existsById(cpf)) {
            throw new RegistroDuplicadoException("Já existe um cadastro com este CPF.");
        }
        if (funcionarioRepository.existsByLoginIgnoreCase(login)
                || clienteRepository.existsByLoginIgnoreCase(login)) {
            throw new RegistroDuplicadoException("Já existe um cadastro com este e-mail.");
        }

        Funcionario funcionario = new Funcionario(
                cpf,
                dados.getNome().trim(),
                login,
                passwordEncoder.encode(dados.getSenha()),
                dados.getGenero().trim(),
                dados.getTelefone().trim(),
                dados.getFuncao(),
                Status.EM_VALIDAÇÃO,
                dados.getRg().trim(),
                dados.getDataNascimento(),
                dados.getSetor().trim());
        return funcionarioRepository.save(funcionario);
    }

    @Transactional
    public Funcionario atualizar(String cpf, PerfilUpdateDTO dadosNovos) {
        validarPerfil(dadosNovos);
        Funcionario funcionario = buscar(cpf);
        validarLoginDisponivel(dadosNovos.getLogin(), funcionario.getLogin());

        funcionario.setNome(dadosNovos.getNome().trim());
        funcionario.setLogin(dadosNovos.getLogin().trim());
        funcionario.setTelefone(dadosNovos.getTelefone().trim());
        if (dadosNovos.getSenha() != null && !dadosNovos.getSenha().isBlank()) {
            funcionario.setSenha(passwordEncoder.encode(dadosNovos.getSenha()));
        }
        return funcionarioRepository.save(funcionario);
    }

    @Transactional
    public Funcionario aprovar(String cpf) {
        Funcionario funcionario = buscar(cpf);
        funcionario.setStatus(Status.ATIVO);
        return funcionarioRepository.save(funcionario);
    }

    @Transactional
    public void recusar(String cpf) {
        funcionarioRepository.delete(buscar(cpf));
    }

    @Transactional
    public void excluir(String cpf) {
        funcionarioRepository.delete(buscar(cpf));
    }

    private Funcionario buscar(String cpf) {
        return funcionarioRepository.findById(cpf)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado."));
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
        if (funcionarioRepository.existsByLoginIgnoreCase(loginNormalizado)
                || clienteRepository.existsByLoginIgnoreCase(loginNormalizado)) {
            throw new RegistroDuplicadoException("Já existe um cadastro com este e-mail.");
        }
    }

    private String normalizarCpf(String cpf) {
        return cpf.replaceAll("\\D", "");
    }
}
