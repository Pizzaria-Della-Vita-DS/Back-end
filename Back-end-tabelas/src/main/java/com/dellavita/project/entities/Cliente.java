package com.dellavita.project.entities;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_cliente")
public class Cliente extends Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	private String endereco;

	
	public Cliente(String endereco) {
		this.endereco = endereco;
	}

	public Cliente(String cpf, String nome, String telefone, String genero, String login, String senha,
			String endereco) {
		super(cpf, nome, telefone, genero, login, senha);
		this.endereco = endereco;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
}