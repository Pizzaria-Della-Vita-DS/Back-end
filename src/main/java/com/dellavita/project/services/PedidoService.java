package com.dellavita.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.entities.Pedido;
import com.dellavita.project.enums.Estado;
import com.dellavita.project.repositories.PedidoRepository;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Transactional
	public List<Pedido> listar(){
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
}
