package com.dellavita.project.entities;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_cliente")
public class Cliente extends Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	private String endereco;
	
	public Cliente() {
		super();
	}
	
	// Updated constructor to include all inherited fields + the specific Cliente field
	public Cliente(String cpf, String nome, String telefone, String genero, String login, String senha, String endereco) {
		super(cpf, nome, telefone, genero, senha);
		this.endereco = endereco;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	@Override
	public String toString() {
		// You can now call getters from the superclass directly!
		return "Cliente [cpf=" + getCpf() + ", nome=" + getNome() + ", endereco=" + endereco + "]";
	}
}