
package com.dellavita.project.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_borda")
public class Borda implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String nome;
	private Double preco;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "tb_borda_ingrediente",
		joinColumns = @JoinColumn(name = "borda_id"),
		inverseJoinColumns = @JoinColumn(name = "ingrediente_id")
	)
	private List<Ingrediente> ingredientes = new ArrayList<>();
	
	public Borda() {
	}
	
	public Borda(Long id, String nome, Double preco) {
		this(id, nome, preco, new ArrayList<>());
	}

	public Borda(Long id, String nome, Double preco, List<Ingrediente> ingredientes) {
		super();
		this.id = id;
		this.nome = nome;
		this.preco = preco;
		this.ingredientes = ingredientes == null ? new ArrayList<>() : new ArrayList<>(ingredientes);
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

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public List<Ingrediente> getIngredientes() {
		return ingredientes;
	}

	public void setIngredientes(List<Ingrediente> ingredientes) {
		this.ingredientes = ingredientes == null ? new ArrayList<>() : new ArrayList<>(ingredientes);
	}

	public boolean isDisponivel() {
		return ingredientes == null || ingredientes.stream().allMatch(Ingrediente::isDisponivel);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Borda [id=" + id + ", nome=" + nome + ", preco=" + preco + "]";
	}
}
