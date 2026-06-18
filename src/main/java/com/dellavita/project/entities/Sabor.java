package com.dellavita.project.entities;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_sabor")
public class Sabor implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	@OneToMany
	@JoinColumn(name ="id_ingredientes")
	private Ingrediente ingredientes;
	private Double preco;
	
	public Sabor() {
	}	
	
	public Sabor(Long id, String name, Ingrediente ingredientes, Double preco) {
		super();
		this.id = id;
		this.name = name;
		this.ingredientes = ingredientes;
		this.preco = preco;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Ingrediente getIngredientes() {
		return ingredientes;
	}

	public void setIngredientes(Ingrediente ingredientes) {
		this.ingredientes = ingredientes;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	@Override
	public String toString() {
		return "Sabores [id=" + id + ", name=" + name + ", preco=" + preco + "]";
	}
	
	
	
}
