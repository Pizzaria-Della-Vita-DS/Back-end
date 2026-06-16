
package com.dellavita.project.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_sabor")
public class Sabor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String nome;
	private Integer preco;

	// é da tabela de Pizza Possui Sabor (M:N)
	@ManyToMany(mappedBy = "sabores")
	private Set<Pizza> pizzas = new HashSet<>();

	// é da tabela de Funcionario Gerencia Sabor (M:N)
	@ManyToMany(mappedBy = "saboresGerenciados")
	private Set<Funcionario> funcionariosGerentes = new HashSet<>();

	// é da tabela de Cliente Visualiza cardapio Sabor (M:N)
	@ManyToMany(mappedBy = "saboresVisualizados")
	private Set<Cliente> clientesVisualizadores = new HashSet<>();

	// é a tabela de Sabor Possui Ingrediente (N:M)
	@ManyToMany
	@JoinTable(
		name = "tb_possui_sabor_ingrediente",
		joinColumns = @JoinColumn(name = "fk_Sabor_nome"),
		inverseJoinColumns = @JoinColumn(name = "fk_Ingrediente_nome")
	)
	private Set<Ingrediente> ingredientes = new HashSet<>();


	public Sabor() {
	}

	public Sabor(String nome, Integer preco) {
		this.nome = nome;
		this.preco = preco;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getPreco() {
		return preco;
	}

	public void setPreco(Integer preco) {
		this.preco = preco;
	}

	public Set<Ingrediente> getIngredientes() {
		return ingredientes;
	}

	public void setIngredientes(Set<Ingrediente> ingredientes) {
		this.ingredientes = ingredientes;
	}
}
