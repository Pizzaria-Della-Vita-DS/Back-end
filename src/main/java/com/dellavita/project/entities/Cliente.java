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
	
	public Cliente(String cpf, String nome, String email, String senha, String genero, String telefone, String endereco) {
		super(cpf, nome, email, senha, genero, telefone);
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
		return "Cliente [cpf=" + getCpf() + ", nome=" + getNome() + ", endereco=" + endereco + "]";
	}
}