package com.dellavita.project.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_cliente")
public class Cliente extends Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	private String endereco;


	// é a relação de Cliente faz Pedido (1:N)
	@OneToMany(mappedBy = "cliente")
	private Set<Pedido> pedidos = new HashSet<>();

	// é a tabela de Cliente Visualiza cardapio Sabor (N:M)
	@ManyToMany
	@JoinTable(
		name = "tb_visualiza_cardapio",
		joinColumns = @JoinColumn(name = "fk_Usuario_cpf"),
		inverseJoinColumns = @JoinColumn(name = "fk_Sabor_nome")
	)
	private Set<Sabor> saboresVisualizados = new HashSet<>();

	
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