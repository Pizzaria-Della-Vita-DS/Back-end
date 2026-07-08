package com.dellavita.project.entities;

import java.io.Serializable;
import java.time.LocalDate;

import com.dellavita.project.enums.Funcao;
import com.dellavita.project.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_funcionario")
public class Funcionario extends Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Funcao funcao;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;
	
	private String rg;
	
	private LocalDate data_nascimento;
	
	private String setor;
	
	public Funcionario() {
		super();
	}

	public Funcionario(String cpf, String nome, String email, String senha, String genero, String telefone, Funcao funcao, Status status, String rg, LocalDate data_nascimento, String setor) {
		super(cpf, nome, email, senha, genero, telefone);
		this.funcao = funcao;
		this.status = status;
		this.rg = rg;
		this.data_nascimento = data_nascimento;
		this.setor = setor;
	}

	public Funcao getFuncao() {
		return funcao;
	}

	public void setFuncao(Funcao funcao) {
		this.funcao = funcao;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public LocalDate getData_nascimento() { // AQUI
		return data_nascimento; // AQUI
	} // AQUI

	public void setData_nascimento(LocalDate data_nascimento) { // AQUI
		this.data_nascimento = data_nascimento; // AQUI
	} // AQUI

	public String getSetor() { // AQUI
		return setor; // AQUI
	} // AQUI

	public void setSetor(String setor) { // AQUI
		this.setor = setor; // AQUI
	} // AQUI

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Funcionario [funcao=" + funcao + ", status=" + status + ", rg=" + rg + ", setor=" + setor + "]"; // AQUI
	}
}