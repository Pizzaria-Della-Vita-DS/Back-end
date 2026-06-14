package com.dellavita.project.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_funcionario")
public class Funcionario extends Usuario {

	private String rg; // não é PK pois a PK do Funcionario é o CPF, definido em Usuario
    private LocalDate data_nascimento;
	private String email;
    private String setor;


    

    public Funcionario(String rg, LocalDate data_nascimento, String email, String setor) {
        this.rg = rg;
        this.data_nascimento = data_nascimento;
        this.email = email;
        this.setor = setor;
    }

    public Funcionario(String cpf, String nome, String telefone, String genero, String login, String senha, String rg,
            LocalDate data_nascimento, String email, String setor) {
        super(cpf, nome, telefone, genero, login, senha);
        this.rg = rg;
        this.data_nascimento = data_nascimento;
        this.email = email;
        this.setor = setor;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public LocalDate getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(LocalDate data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

}


