package com.dellavita.project.services;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.dto.PedidoRequestDTO;
import com.dellavita.project.dto.PedidoRequestDTO.PizzaRequestDTO;
import com.dellavita.project.dto.PedidoResponseDTO;
import com.dellavita.project.entities.Borda;
import com.dellavita.project.entities.Cliente;
import com.dellavita.project.entities.Pedido;
import com.dellavita.project.entities.Pizza;
import com.dellavita.project.entities.Sabor;
import com.dellavita.project.enums.Estado;
import com.dellavita.project.enums.FormaPagamento;
import com.dellavita.project.enums.Tamanho;
import com.dellavita.project.repositories.BordaRepository;
import com.dellavita.project.repositories.ClienteRepository;
import com.dellavita.project.repositories.PedidoRepository;
import com.dellavita.project.repositories.SaborRepository;

@Service
public class PedidoService {

    private static final double TAXA_ENTREGA = 7.00;
    private static final Collection<Estado> ESTADOS_ENCERRADOS =
            List.of(Estado.ENTREGUE, Estado.FINALIZADO, Estado.CANCELADO);

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final SaborRepository saborRepository;
    private final BordaRepository bordaRepository;

    public PedidoService(
            PedidoRepository pedidoRepository,
            ClienteRepository clienteRepository,
            SaborRepository saborRepository,
            BordaRepository bordaRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.saborRepository = saborRepository;
        this.bordaRepository = bordaRepository;
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listar() {
        return converter(pedidoRepository.listarMaisRecentes());
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> findAtivos() {
        return converter(pedidoRepository.listarAtivos(ESTADOS_ENCERRADOS));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> findHistoricoGeral() {
        return converter(pedidoRepository.listarMaisRecentes());
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> findHistoricoCliente(String clienteCpf) {
        if (clienteCpf == null || clienteCpf.isBlank()) {
            throw new IllegalArgumentException("O CPF do cliente é obrigatório.");
        }
        return converter(pedidoRepository.listarPorCliente(clienteCpf));
    }

    @Transactional
    public PedidoResponseDTO criar(PedidoRequestDTO dados) {
        validarPedido(dados);

        Cliente cliente = clienteRepository.findById(dados.getClienteCpf())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setEstado(Estado.CONFIRMANDO_PEDIDO);
        pedido.setForma_pagamento(dados.getFormaPagamento());
        pedido.setEndereco_entrega(dados.getEnderecoEntrega());
        pedido.setData_hora(LocalDateTime.now());

        List<Pizza> pizzas = new ArrayList<>();
        for (PizzaRequestDTO item : dados.getPizzas()) {
            pizzas.add(criarPizza(item, pedido));
        }

        pedido.setPizzas(pizzas);
        double subtotal = pizzas.stream().mapToDouble(Pizza::getPreco).sum();
        double taxaEntrega = dados.getEnderecoEntrega() == null || dados.getEnderecoEntrega().isBlank()
                ? 0.0
                : TAXA_ENTREGA;
        pedido.setPreco_total(arredondar(subtotal + taxaEntrega));
        return PedidoResponseDTO.fromEntity(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponseDTO atualizarEstado(Long id, Estado estado) {
        if (estado == null) {
            throw new IllegalArgumentException("O estado do pedido é obrigatório.");
        }
        Pedido pedido = buscarPedido(id);
        pedido.setEstado(estado);
        return PedidoResponseDTO.fromEntity(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponseDTO atualizarFormaPagamento(Long id, FormaPagamento formaPagamento) {
        if (formaPagamento == null) {
            throw new IllegalArgumentException("A forma de pagamento é obrigatória.");
        }
        Pedido pedido = buscarPedido(id);
        pedido.setForma_pagamento(formaPagamento);
        return PedidoResponseDTO.fromEntity(pedidoRepository.save(pedido));
    }

    private Pedido buscarPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado."));
    }

    private Pizza criarPizza(PizzaRequestDTO item, Pedido pedido) {
        if (item == null) {
            throw new IllegalArgumentException("Os dados da pizza são obrigatórios.");
        }

        Tamanho tamanho = converterTamanho(item.getTamanho());
        List<Long> saboresIds = item.getSaboresIds() == null
                ? List.of()
                : item.getSaboresIds().stream().filter(Objects::nonNull).distinct().toList();
        if (saboresIds.isEmpty()) {
            throw new IllegalArgumentException("Cada pizza deve possuir ao menos um sabor.");
        }

        List<Sabor> sabores = saborRepository.findAllById(saboresIds);
        if (sabores.size() != saboresIds.size()) {
            throw new IllegalArgumentException("Um ou mais sabores do pedido não foram encontrados.");
        }
        if (sabores.size() > maximoSabores(tamanho)) {
            throw new IllegalArgumentException("A quantidade de sabores excede o limite do tamanho escolhido.");
        }
        if (sabores.stream().anyMatch(sabor -> sabor.getPreco() == null || sabor.getPreco() < 0)) {
            throw new IllegalArgumentException("Um ou mais sabores possuem preço inválido.");
        }
        if (sabores.stream().anyMatch(sabor -> !sabor.isDisponivel())) {
            throw new IllegalArgumentException("Um ou mais sabores estão indisponíveis no momento.");
        }

        Borda borda = buscarBordaDisponivel(item.getBordaId());
        double mediaSabores = sabores.stream().mapToDouble(Sabor::getPreco).average().orElseThrow();
        double preco = mediaSabores * multiplicador(tamanho) + (borda == null ? 0.0 : borda.getPreco());

        Pizza pizza = new Pizza();
        pizza.setPedido(pedido);
        pizza.setTamanho(tamanho);
        pizza.setPreco(arredondar(preco));
        pizza.setBorda(borda);
        pizza.setCodigo(montarDescricao(sabores, borda, tamanho));
        return pizza;
    }

    private String montarDescricao(List<Sabor> sabores, Borda borda, Tamanho tamanho) {
        String descricao = sabores.isEmpty()
                ? "Pizza " + tamanho.name()
                : String.join(" / ", sabores.stream().map(Sabor::getNome).toList());

        if (borda != null) {
            descricao += " - borda " + borda.getNome();
        }

        return descricao.length() > 255 ? descricao.substring(0, 255) : descricao;
    }

    private Borda buscarBordaDisponivel(Long bordaId) {
        if (bordaId == null) {
            return null;
        }
        Borda borda = bordaRepository.findById(bordaId)
                .orElseThrow(() -> new IllegalArgumentException("Borda não encontrada."));
        if (borda.getPreco() == null || borda.getPreco() < 0) {
            throw new IllegalArgumentException("A borda escolhida possui preço inválido.");
        }
        if (!borda.isDisponivel()) {
            throw new IllegalArgumentException("A borda escolhida está indisponível no momento.");
        }
        return borda;
    }

    private int maximoSabores(Tamanho tamanho) {
        return switch (tamanho) {
            case PEQUENO -> 1;
            case MEDIO -> 2;
            case GRANDE -> 3;
        };
    }

    private double multiplicador(Tamanho tamanho) {
        return switch (tamanho) {
            case PEQUENO -> 1.0;
            case MEDIO -> 1.5;
            case GRANDE -> 2.0;
        };
    }

    private double arredondar(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    private Tamanho converterTamanho(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O tamanho da pizza é obrigatório.");
        }

        String normalizado = Normalizer.normalize(valor, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toUpperCase(Locale.ROOT);

        return switch (normalizado) {
            case "PEQUENA", "PEQUENO" -> Tamanho.PEQUENO;
            case "MEDIA", "MEDIO" -> Tamanho.MEDIO;
            case "GRANDE" -> Tamanho.GRANDE;
            default -> throw new IllegalArgumentException("Tamanho de pizza inválido: " + valor);
        };
    }

    private void validarPedido(PedidoRequestDTO dados) {
        if (dados == null) {
            throw new IllegalArgumentException("Os dados do pedido são obrigatórios.");
        }
        if (dados.getClienteCpf() == null || dados.getClienteCpf().isBlank()) {
            throw new IllegalArgumentException("O cliente do pedido é obrigatório.");
        }
        if (dados.getFormaPagamento() == null) {
            throw new IllegalArgumentException("A forma de pagamento é obrigatória.");
        }
        if (dados.getPizzas() == null || dados.getPizzas().isEmpty()) {
            throw new IllegalArgumentException("O pedido deve possuir ao menos uma pizza.");
        }
    }

    private List<PedidoResponseDTO> converter(List<Pedido> pedidos) {
        return pedidos.stream().map(PedidoResponseDTO::fromEntity).toList();
    }
}
