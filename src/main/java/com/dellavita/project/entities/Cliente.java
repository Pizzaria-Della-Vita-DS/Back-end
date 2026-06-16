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
	public Cliente(Long id, String nome, String telefone, String genero, String login, String senha, String cpf, String endereco) {
		super(id, nome, telefone, genero, senha, cpf);
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
		return "Cliente [id=" + getId() + ", nome=" + getNome() + ", endereco=" + endereco + "]";
	}
}