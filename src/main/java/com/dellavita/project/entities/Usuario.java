package com.dellavita.project.entities;

import java.util.Collection; // AQUI
import java.util.List; // AQUI

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.security.core.GrantedAuthority; // AQUI
import org.springframework.security.core.authority.SimpleGrantedAuthority; // AQUI
import org.springframework.security.core.userdetails.UserDetails; // AQUI

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;


@MappedSuperclass
public class Usuario implements UserDetails {

	@Id
	@Column(length = 11, nullable = false, unique = true)
	@CPF(message = "CPF inválido")
	private String cpf;
	private String nome;
	private String telefone;
	private String genero;
	private String login;
	private String senha;
	
	public Usuario() {
	}
	
	public Usuario(String cpf, String nome, String login, String senha, String genero, String telefone) { // AQUI
		super();
		this.cpf = cpf;
		this.nome = nome;
		this.login = login;
		this.senha = senha;
		this.genero = genero;
		this.telefone = telefone;
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
		return "Usuario [cpf=" + cpf + ", nome=" + nome + ", login=" + login + ", senha=" + senha + ", genero=" + genero + ", telefone=" + telefone + "]"; // AQUI
	}
	
	@Override
	public String getUsername() {
		return this.login; // AQUI
	}

	@Override
	public String getPassword() {
		return this.senha; 
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_CLIENTE"));
	}

	@Override public boolean isAccountNonExpired()     { return true; }
	@Override public boolean isAccountNonLocked()      { return true; }
	@Override public boolean isCredentialsNonExpired() { return true; }
	@Override public boolean isEnabled()               { return true; }
}