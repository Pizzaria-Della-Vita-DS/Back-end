package com.dellavita.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dellavita.project.entities.Pedido;
import com.dellavita.project.enums.Estado;
import com.dellavita.project.enums.FormaPagamento;
import com.dellavita.project.services.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

	@Autowired
	private PedidoService pedidoService;

	@GetMapping
	public List<Pedido> listar(){
		return pedidoService.listar();
	}

	@GetMapping(value = "/ativos")
	public List<Pedido> findAtivos() {
		return pedidoService.findAtivos();
	}

	@PostMapping
	public Pedido criar(@RequestBody Pedido pedido) {
		return pedidoService.criar(pedido);
	}

	@PatchMapping("/{id}/estado")
	public ResponseEntity<Pedido> alternar(@PathVariable Long id, @RequestBody Estado estado){
		return ResponseEntity.ok(pedidoService.atualizarEstado(id, estado));
	}

	@PatchMapping("/{id}/forma-pagamento")
	public ResponseEntity<Pedido> alternarFormaPagamento(@PathVariable Long id, @RequestBody FormaPagamento formaPagamento){
		return ResponseEntity.ok(pedidoService.atualizarFormaPagamento(id, formaPagamento));
	}

	@GetMapping(value = "/historico")
	public List<Pedido> findHistorico() {
		return pedidoService.findHistorico();
	}
}