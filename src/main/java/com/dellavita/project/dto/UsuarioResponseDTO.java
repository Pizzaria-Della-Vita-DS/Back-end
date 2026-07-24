package com.dellavita.project.dto;

import java.time.LocalDate;

import com.dellavita.project.entities.Cliente;
import com.dellavita.project.entities.Funcionario;

public class UsuarioResponseDTO {

    private String cpf;
    private String nome;
    private String login;
    private String genero;
    private String telefone;
    private String tipo;

    private String endereco;

    private String funcao;
    private String status;
    private String setor;
    private String rg;
    private LocalDate dataNascimento;

    public UsuarioResponseDTO() {
    }

    public static UsuarioResponseDTO fromCliente(Cliente cliente) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.cpf = cliente.getCpf();
        dto.nome = cliente.getNome();
        dto.login = cliente.getLogin();
        dto.genero = cliente.getGenero();
        dto.telefone = cliente.getTelefone();
        dto.tipo = "cliente";
        dto.endereco = cliente.getEndereco();
        return dto;
    }

    public static UsuarioResponseDTO fromFuncionario(Funcionario funcionario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.cpf = funcionario.getCpf();
        dto.nome = funcionario.getNome();
        dto.login = funcionario.getLogin();
        dto.genero = funcionario.getGenero();
        dto.telefone = funcionario.getTelefone();
        dto.tipo = "funcionario";
        dto.funcao = funcionario.getFuncao() != null ? funcionario.getFuncao().name() : null;
        dto.status = funcionario.getStatus() != null ? funcionario.getStatus().name() : null;
        dto.setor = funcionario.getSetor();
        dto.rg = funcionario.getRg();
        dto.dataNascimento = funcionario.getData_nascimento();
        return dto;
    }

    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }

    public String getGenero() {
        return genero;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getTipo() {
        return tipo;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getFuncao() {
        return funcao;
    }

    public String getStatus() {
        return status;
    }

    public String getSetor() {
        return setor;
    }

    public String getRg() {
        return rg;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
}
