
package com.dellavita.project.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_ingrediente")
public class Ingrediente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String nome;
	@Column(name = "esta_disponivel")
	private Boolean estaDisponivel;


    // é da tabela de Sabor Possui Ingrediente (M:N)
	@ManyToMany(mappedBy = "ingredientes")
	private Set<Sabor> sabores = new HashSet<>();

	// é da tabela de Funcionario Gerencia estoque Ingrediente (M:N)
	@ManyToMany(mappedBy = "ingredientesGerenciados")
	private Set<Funcionario> funcionariosGerentes = new HashSet<>();


	public Ingrediente() {
	}

	public Ingrediente(String nome, Boolean estaDisponivel) {
		this.nome = nome;
		this.estaDisponivel = estaDisponivel;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Boolean getEstaDisponivel() {
		return estaDisponivel;
	}

	public void setEstaDisponivel(Boolean estaDisponivel) {
		this.estaDisponivel = estaDisponivel;
	}
}
