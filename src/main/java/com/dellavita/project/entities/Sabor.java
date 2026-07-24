
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
@Table(name = "tb_sabor")
public class Sabor implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String nome;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	    name = "tb_possui",
	    joinColumns = @JoinColumn(name = "sabor_id"),
	    inverseJoinColumns = @JoinColumn(name = "ingrediente_id")
	)
	private List<Ingrediente> ingredientes = new ArrayList<>();
	
	private Double preco;
	
	public Sabor() {
	}   
	
	public Sabor(Long id, String nome, List<Ingrediente> ingredientes, Double preco) {
		super();
		this.id = id;
		this.nome = nome;
		this.ingredientes = ingredientes == null ? new ArrayList<>() : new ArrayList<>(ingredientes);
		this.preco = preco;
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

	public List<Ingrediente> getIngredientes() {
		return ingredientes;
	}

	public void setIngredientes(List<Ingrediente> ingredientes) {
		this.ingredientes = ingredientes == null ? new ArrayList<>() : new ArrayList<>(ingredientes);
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public boolean isDisponivel() {
		return ingredientes != null
				&& !ingredientes.isEmpty()
				&& ingredientes.stream().allMatch(Ingrediente::isDisponivel);
	}

	@Override
	public String toString() {
		return "Sabores [id=" + id + ", nome=" + nome + ", preco=" + preco + "]";
	}	
}
