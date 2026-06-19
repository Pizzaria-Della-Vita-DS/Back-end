package com.dellavita.project.entities;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_cliente")
public class Cliente extends Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	private String endereco;
	private String telefone;
	
	public Cliente() {
		super();
	}
	
	// Updated constructor to include all inherited fields + the specific Cliente field
	public Cliente(String cpf, String nome, String email, String senha, String endereco, String telefone ) {
		super(cpf, nome, email, senha);
		this.endereco = endereco;
		this.telefone = telefone;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	@Override
	public String toString() {
		// You can now call getters from the superclass directly!
		return "Cliente [cpf=" + getCpf() + ", nome=" + getNome() + ", endereco=" + endereco + "]";
	}
}