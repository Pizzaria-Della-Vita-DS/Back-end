package com.dellavita.project.entities;

import java.io.Serializable;

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
	
	public Funcionario(String cpf, String nome, String email, String senha, Funcao funcao, Status status) {
		super(cpf, nome, email, senha);
		this.funcao = funcao;
		this.status = status;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Funcionario [funcao=" + funcao + ", status=" + status + "]";
	}
	
	
}
