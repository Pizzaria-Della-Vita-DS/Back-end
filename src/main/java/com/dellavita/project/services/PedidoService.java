package com.dellavita.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.entities.Pedido;
import com.dellavita.project.enums.Estado;
import com.dellavita.project.enums.FormaPagamento;
import com.dellavita.project.repositories.PedidoRepository;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Transactional
    public List<Pedido> listar(){
        return pedidoRepository.findAll();
    }

    public List<Pedido> findAtivos() { // AQUI
		// Temporariamente retorna todos. Você pode alterar a lógica depois para buscar apenas estados específicos.
		return pedidoRepository.findAll(); // AQUI
	} // AQUI

    @Transactional
    public List<Pedido> findHistorico() { // AQUI
		// Por enquanto retornamos todos. Futuramente, você pode alterar o Repository 
		// para buscar apenas onde "estado = ENTREGUE ou FINALIZADO"
		return pedidoRepository.findAll(); 
	}


    @Transactional
    public Pedido criar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido atualizarEstado(Long id, Estado estado) {
        Pedido pedidoAlterado = pedidoRepository.findById(id).orElseThrow();
        pedidoAlterado.setEstado(estado);
        return pedidoRepository.save(pedidoAlterado);
    }

    @Transactional
    public Pedido atualizarFormaPagamento(Long id, FormaPagamento formaPagamento) {
        Pedido pedidoAlterado = pedidoRepository.findById(id).orElseThrow();
        pedidoAlterado.setForma_pagamento(formaPagamento);
        return pedidoRepository.save(pedidoAlterado);
    }
}