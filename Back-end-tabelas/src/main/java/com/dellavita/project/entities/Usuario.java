package com.dellavita.project.entities;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;

@MappedSuperclass
public abstract class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String cpf;
	private String nome;
	private String telefone;
	private String genero;
	private String login;
	private String senha;
	

	public Usuario() {
	}

	public Usuario(String cpf, String nome, String telefone, String genero, String login, String senha) {
		this.cpf = cpf;
		this.nome = nome;
		this.telefone = telefone;
		this.genero = genero;
		this.login = login;
		this.senha = senha;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
	public String toString() {
		return "Usuario [cpf=" + cpf + ", nome=" + nome + ", telefone=" + telefone + ", genero=" + genero + ", login="
				+ login + ", senha=" + senha + "]";
	}
	
}
