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

import com.dellavita.project.dto.PedidoRequestDTO;
import com.dellavita.project.dto.PedidoResponseDTO;
import com.dellavita.project.enums.Estado;
import com.dellavita.project.enums.FormaPagamento;
import com.dellavita.project.services.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public List<PedidoResponseDTO> listar() {
        return pedidoService.listar();
    }

    @GetMapping("/ativos")
    public List<PedidoResponseDTO> findAtivos() {
        return pedidoService.findAtivos();
    }

    @GetMapping("/historico")
    public List<PedidoResponseDTO> findHistoricoGeral() {
        return pedidoService.findHistoricoGeral();
    }

    @PostMapping
    public PedidoResponseDTO criar(@RequestBody PedidoRequestDTO pedido) {
        return pedidoService.criar(pedido);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<PedidoResponseDTO> atualizarEstado(
            @PathVariable Long id,
            @RequestBody Estado estado) {
        return ResponseEntity.ok(pedidoService.atualizarEstado(id, estado));
    }

    @PatchMapping("/{id}/forma-pagamento")
    public ResponseEntity<PedidoResponseDTO> atualizarFormaPagamento(
            @PathVariable Long id,
            @RequestBody FormaPagamento formaPagamento) {
        return ResponseEntity.ok(pedidoService.atualizarFormaPagamento(id, formaPagamento));
    }

    @GetMapping("/historico/{clienteCpf}")
    public List<PedidoResponseDTO> findHistoricoCliente(@PathVariable String clienteCpf) {
        return pedidoService.findHistoricoCliente(clienteCpf);
    }
}
