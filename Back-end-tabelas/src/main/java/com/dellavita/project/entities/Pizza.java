package com.dellavita.project.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.dellavita.project.enums.Tamanho;

@Entity
@Table(name = "tb_pizza")
public class Pizza {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long codigo;
    private Integer preco;
    private Tamanho tamanho;
    @ManyToOne
    @JoinColumn(name = "codigo_pedido_fk")
    private Pedido pedido;


    public Pizza() {
    }

    public Pizza(long codigo, Integer preco, Tamanho tamanho, Pedido pedido) {
        this.codigo = codigo;
        this.preco = preco;
        this.tamanho = tamanho;
        this.pedido = pedido;
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }
    public Integer getPreco() {
        return preco;
    }

    public void setPreco(Integer preco) {
        this.preco = preco;
    }

    public Tamanho getTamanho() {
        return tamanho;
    }

    public void setTamanho(Tamanho tamanho) {
        this.tamanho = tamanho;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

}

