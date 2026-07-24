package com.dellavita.project.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dellavita.project.dto.PedidoRequestDTO;
import com.dellavita.project.dto.PedidoRequestDTO.PizzaRequestDTO;
import com.dellavita.project.entities.Borda;
import com.dellavita.project.entities.Cliente;
import com.dellavita.project.entities.Ingrediente;
import com.dellavita.project.entities.Pedido;
import com.dellavita.project.entities.Sabor;
import com.dellavita.project.enums.Estado;
import com.dellavita.project.enums.FormaPagamento;
import com.dellavita.project.enums.Tamanho;
import com.dellavita.project.repositories.BordaRepository;
import com.dellavita.project.repositories.ClienteRepository;
import com.dellavita.project.repositories.PedidoRepository;
import com.dellavita.project.repositories.SaborRepository;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTests {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private SaborRepository saborRepository;

    @Mock
    private BordaRepository bordaRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void deveCriarPedidoComClienteValoresPadraoEPizzasVinculadas() {
        Cliente cliente = new Cliente();
        cliente.setCpf("12345678909");
        cliente.setNome("Cliente Teste");

        Sabor sabor = saborDisponivel();
        Borda borda = new Borda(2L, "Catupiry", 5.0, List.of(
                new Ingrediente(3L, "Catupiry", true)));

        when(clienteRepository.findById(cliente.getCpf())).thenReturn(Optional.of(cliente));
        when(saborRepository.findAllById(List.of(1L))).thenReturn(List.of(sabor));
        when(bordaRepository.findById(2L)).thenReturn(Optional.of(borda));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        PizzaRequestDTO pizza = new PizzaRequestDTO();
        pizza.setTamanho("Média");
        pizza.setPreco(50.0);
        pizza.setSaboresIds(List.of(1L));
        pizza.setBordaId(2L);

        PedidoRequestDTO dados = new PedidoRequestDTO();
        dados.setClienteCpf(cliente.getCpf());
        dados.setFormaPagamento(FormaPagamento.PIX);
        dados.setEnderecoEntrega("Rua de Teste, 123");
        dados.setPrecoTotal(57.0);
        dados.setPizzas(List.of(pizza));

        pedidoService.criar(dados);

        ArgumentCaptor<Pedido> captor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository).save(captor.capture());
        Pedido salvo = captor.getValue();

        assertSame(cliente, salvo.getCliente());
        assertEquals(Estado.CONFIRMANDO_PEDIDO, salvo.getEstado());
        assertEquals(FormaPagamento.PIX, salvo.getForma_pagamento());
        assertEquals(57.0, salvo.getPreco_total());
        assertNotNull(salvo.getData_hora());
        assertEquals(1, salvo.getPizzas().size());
        assertSame(salvo, salvo.getPizzas().get(0).getPedido());
        assertEquals(Tamanho.MEDIO, salvo.getPizzas().get(0).getTamanho());
        assertEquals(50.0, salvo.getPizzas().get(0).getPreco());
        assertSame(borda, salvo.getPizzas().get(0).getBorda());
        assertEquals("Calabresa - borda Catupiry", salvo.getPizzas().get(0).getCodigo());
    }

    @Test
    void deveBuscarHistoricoSomenteDoClienteInformado() {
        when(pedidoRepository.listarPorCliente("12345678909")).thenReturn(List.of());

        pedidoService.findHistoricoCliente("12345678909");

        verify(pedidoRepository).listarPorCliente("12345678909");
    }

    @Test
    void deveBuscarHistoricoGeralSemExcluirPedidosFinalizados() {
        Pedido finalizado = new Pedido();
        finalizado.setEstado(Estado.FINALIZADO);
        when(pedidoRepository.listarMaisRecentes()).thenReturn(List.of(finalizado));

        List<com.dellavita.project.dto.PedidoResponseDTO> historico =
                pedidoService.findHistoricoGeral();

        assertEquals(1, historico.size());
        assertEquals(Estado.FINALIZADO, historico.get(0).getEstado());
        verify(pedidoRepository).listarMaisRecentes();
    }

    @Test
    void deveIgnorarPrecoEnviadoPeloCliente() {
        Cliente cliente = new Cliente();
        cliente.setCpf("12345678909");
        Sabor sabor = saborDisponivel();

        when(clienteRepository.findById(cliente.getCpf())).thenReturn(Optional.of(cliente));
        when(saborRepository.findAllById(List.of(1L))).thenReturn(List.of(sabor));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        PizzaRequestDTO pizza = new PizzaRequestDTO();
        pizza.setTamanho("Pequena");
        pizza.setPreco(0.01);
        pizza.setSaboresIds(List.of(1L));

        PedidoRequestDTO dados = new PedidoRequestDTO();
        dados.setClienteCpf(cliente.getCpf());
        dados.setFormaPagamento(FormaPagamento.DINHEIRO);
        dados.setPrecoTotal(0.01);
        dados.setPizzas(List.of(pizza));

        pedidoService.criar(dados);

        ArgumentCaptor<Pedido> captor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository).save(captor.capture());
        assertEquals(30.0, captor.getValue().getPizzas().get(0).getPreco());
        assertEquals(30.0, captor.getValue().getPreco_total());
    }

    @Test
    void deveRejeitarBordaComIngredienteIndisponivel() {
        Cliente cliente = new Cliente();
        cliente.setCpf("12345678909");
        Sabor sabor = saborDisponivel();
        Borda borda = new Borda(2L, "Catupiry", 5.0, List.of(
                new Ingrediente(3L, "Catupiry", false)));

        when(clienteRepository.findById(cliente.getCpf())).thenReturn(Optional.of(cliente));
        when(saborRepository.findAllById(List.of(1L))).thenReturn(List.of(sabor));
        when(bordaRepository.findById(2L)).thenReturn(Optional.of(borda));

        PizzaRequestDTO pizza = new PizzaRequestDTO();
        pizza.setTamanho("Pequena");
        pizza.setSaboresIds(List.of(1L));
        pizza.setBordaId(2L);

        PedidoRequestDTO dados = new PedidoRequestDTO();
        dados.setClienteCpf(cliente.getCpf());
        dados.setFormaPagamento(FormaPagamento.PIX);
        dados.setPizzas(List.of(pizza));

        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> pedidoService.criar(dados));
        assertEquals("A borda escolhida está indisponível no momento.", erro.getMessage());
    }

    @Test
    void deveRejeitarSaborComIngredienteIndisponivel() {
        Cliente cliente = new Cliente();
        cliente.setCpf("12345678909");
        Sabor sabor = new Sabor(1L, "Camarão", List.of(
                new Ingrediente(4L, "Camarão", false)), 45.0);

        when(clienteRepository.findById(cliente.getCpf())).thenReturn(Optional.of(cliente));
        when(saborRepository.findAllById(List.of(1L))).thenReturn(List.of(sabor));

        PizzaRequestDTO pizza = new PizzaRequestDTO();
        pizza.setTamanho("Pequena");
        pizza.setSaboresIds(List.of(1L));

        PedidoRequestDTO dados = new PedidoRequestDTO();
        dados.setClienteCpf(cliente.getCpf());
        dados.setFormaPagamento(FormaPagamento.PIX);
        dados.setPizzas(List.of(pizza));

        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> pedidoService.criar(dados));
        assertEquals("Um ou mais sabores estão indisponíveis no momento.", erro.getMessage());
    }

    private Sabor saborDisponivel() {
        return new Sabor(1L, "Calabresa", List.of(
                new Ingrediente(1L, "Calabresa", true)), 30.0);
    }
}
