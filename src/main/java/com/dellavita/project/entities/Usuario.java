package com.dellavita.project.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_usuario")
public class Usuario {

	@Id
<<<<<<< Updated upstream
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
=======
	@Column(length = 11, nullable = false, unique = true)
	@CPF(message = "CPF inválido")
	private String cpf;
>>>>>>> Stashed changes
	private String nome;
	private String telefone;
	private String genero;
	private String login;
	private String senha;
<<<<<<< Updated upstream
	private String cpf;
=======
	private String genero; // AQUI
	private String telefone; // AQUI
>>>>>>> Stashed changes
	
	public Usuario() {
	}
	
<<<<<<< Updated upstream
	public Usuario(Long id, String nome, String telefone, String genero, String login, String senha, String cpf) {
		super();
		this.id = id;
=======
	public Usuario(String cpf, String nome, String email, String senha, String genero, String telefone) { // AQUI
		super();
		this.cpf = cpf; // AQUI
>>>>>>> Stashed changes
		this.nome = nome;
		this.telefone = telefone;
		this.genero = genero;
		this.login = login;
		this.senha = senha;
		this.genero = genero; // AQUI
		this.telefone = telefone; // AQUI
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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
<<<<<<< Updated upstream
	
	

}
=======

	public String getGenero() { // AQUI
		return genero; // AQUI
	} // AQUI

	public void setGenero(String genero) { // AQUI
		this.genero = genero; // AQUI
	} // AQUI

	public String getTelefone() { // AQUI
		return telefone; // AQUI
	} // AQUI

	public void setTelefone(String telefone) { // AQUI
		this.telefone = telefone; // AQUI
	} // AQUI

	@Override
	public String toString() {
		return "Usuario [cpf=" + cpf + ", nome=" + nome + ", email=" + email + ", senha=" + senha + ", genero=" + genero + ", telefone=" + telefone + "]"; // AQUI
	}
	
	@Override
	public String getUsername() {
		return this.email; 
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
>>>>>>> Stashed changes
