
package com.dellavita.project.entities;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_ingrediente")
public class Ingrediente implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String nome;
	
	private boolean disponivel;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "ingredientes")
	private List<Sabor> sabores;
	
	public Ingrediente() {
	}
	
	public Ingrediente(Long id, String nome, boolean disponivel) {
		super();
		this.id = id;
		this.nome = nome;
		this.disponivel = disponivel;
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

	public boolean isDisponivel() {
		return disponivel;
	}

	public void setDisponivel(boolean disponivel) {
		this.disponivel = disponivel;
	}

	public List<Sabor> getSabores() { // AQUI
		return sabores; // AQUI
	} // AQUI

	public void setSabores(List<Sabor> sabores) { // AQUI
		this.sabores = sabores; // AQUI
	} // AQUI

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Ingrediente [id=" + id + ", nome=" + nome + ", disponivel=" + disponivel + "]";
	}	
}