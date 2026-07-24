package com.dellavita.project.services;

import java.time.LocalDate;
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
        validarAtualizacaoInformada(dadosNovos);
        Funcionario funcionario = buscar(cpf);

        if (dadosNovos.getNome() != null) {
            funcionario.setNome(validarTextoObrigatorio(
                    dadosNovos.getNome(),
                    "O nome é obrigatório."));
        }
        if (dadosNovos.getLogin() != null) {
            String login = validarEmail(dadosNovos.getLogin());
            validarLoginDisponivel(login, funcionario.getLogin());
            funcionario.setLogin(login);
        }
        if (dadosNovos.getTelefone() != null) {
            funcionario.setTelefone(validarTextoObrigatorio(
                    dadosNovos.getTelefone(),
                    "O telefone é obrigatório."));
        }
        if (dadosNovos.getGenero() != null) {
            funcionario.setGenero(validarTextoObrigatorio(
                    dadosNovos.getGenero(),
                    "O gênero é obrigatório."));
        }
        if (dadosNovos.getDataNascimento() != null) {
            validarDataNascimento(dadosNovos.getDataNascimento());
            funcionario.setData_nascimento(dadosNovos.getDataNascimento());
        }
        if (dadosNovos.getSetor() != null) {
            funcionario.setSetor(validarTextoObrigatorio(
                    dadosNovos.getSetor(),
                    "O setor é obrigatório."));
        }
        if (dadosNovos.getSenha() != null) {
            atualizarSenha(funcionario, dadosNovos.getSenha());
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

    private void validarAtualizacaoInformada(PerfilUpdateDTO dados) {
        if (dados == null || (
                dados.getNome() == null
                && dados.getLogin() == null
                && dados.getTelefone() == null
                && dados.getSenha() == null
                && dados.getGenero() == null
                && dados.getDataNascimento() == null
                && dados.getSetor() == null)) {
            throw new IllegalArgumentException("Informe o campo que deseja atualizar.");
        }
    }

    private void validarLoginDisponivel(String novoLogin, String loginAtual) {
        if (novoLogin.equalsIgnoreCase(loginAtual)) {
            return;
        }
        if (funcionarioRepository.existsByLoginIgnoreCase(novoLogin)
                || clienteRepository.existsByLoginIgnoreCase(novoLogin)) {
            throw new RegistroDuplicadoException("Já existe um cadastro com este e-mail.");
        }
    }

    private void atualizarSenha(Funcionario funcionario, String novaSenha) {
        if (novaSenha.isBlank() || novaSenha.length() < 6) {
            throw new IllegalArgumentException("A nova senha deve ter pelo menos 6 caracteres.");
        }
        funcionario.setSenha(passwordEncoder.encode(novaSenha));
    }

    private void validarDataNascimento(LocalDate dataNascimento) {
        if (!dataNascimento.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data de nascimento deve estar no passado.");
        }
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

    private String normalizarCpf(String cpf) {
        return cpf.replaceAll("\\D", "");
    }
}
