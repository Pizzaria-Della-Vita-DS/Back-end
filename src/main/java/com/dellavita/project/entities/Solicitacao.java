package com.dellavita.project.entities;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import com.dellavita.project.enums.Funcao;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_solicitacao")
public class Solicitacao implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String cpf;
	private String senha;	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Funcao funcao;
	private String email;
	@JsonFormat(pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate data;
	
	public Solicitacao() {
	}

	public Solicitacao(Long id, String nome, String cpf, String senha, Funcao funcao, String email,
			LocalDate data) {
		super();
		this.id = id;
		this.nome = nome;
		this.cpf = cpf;
		this.senha = senha;
		this.funcao = funcao;
		this.email = email;
		this.data = data;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Funcao getFuncao() {
		return funcao;
	}

	public void setFuncao(Funcao funcao) {
		this.funcao = funcao;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Solicitacao [id=" + id + ", nome=" + nome + ", cpf=" + cpf + ", senha=" + senha +
				", funcao=" + funcao + ", email=" + email + ", data=" + data + "]";
	}
	
	
	
}
