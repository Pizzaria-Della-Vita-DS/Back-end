package com.dellavita.project.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dellavita.project.dto.SaborRequestDTO;
import com.dellavita.project.entities.Ingrediente;
import com.dellavita.project.entities.Sabor;
import com.dellavita.project.repositories.IngredienteRepository;
import com.dellavita.project.repositories.SaborRepository;

@ExtendWith(MockitoExtension.class)
class SaborServiceTests {

    @Mock
    private SaborRepository saborRepository;

    @Mock
    private IngredienteRepository ingredienteRepository;

    @InjectMocks
    private SaborService saborService;

    @Test
    void deveCriarSaborComIngredientesEDisponibilidadeCalculada() {
        Ingrediente queijo = new Ingrediente(1L, "Queijo", true);
        Ingrediente camarao = new Ingrediente(2L, "Camarão", false);
        when(ingredienteRepository.findAllById(List.of(1L, 2L)))
                .thenReturn(List.of(queijo, camarao));
        when(saborRepository.save(any(Sabor.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));

        Sabor criado = saborService.criar(novoSabor("  Camarão  ", 49.9, List.of(1L, 2L)));

        assertEquals("Camarão", criado.getNome());
        assertEquals(List.of(queijo, camarao), criado.getIngredientes());
        assertFalse(criado.isDisponivel());
    }

    @Test
    void deveExigirNomeEPreco() {
        assertThrows(IllegalArgumentException.class,
                () -> saborService.criar(novoSabor(" ", 30.0, List.of(1L))));
        assertThrows(IllegalArgumentException.class,
                () -> saborService.criar(novoSabor("Calabresa", -1.0, List.of(1L))));
    }

    @Test
    void devePermitirSaborSemIngredientes() {
        when(saborRepository.save(any(Sabor.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));

        Sabor criado = saborService.criar(novoSabor("Calabresa", 30.0, List.of()));

        assertTrue(criado.getIngredientes().isEmpty());
        assertTrue(criado.isDisponivel());
    }

    @Test
    void deveRejeitarIngredienteInexistente() {
        when(ingredienteRepository.findAllById(List.of(99L))).thenReturn(List.of());

        assertThrows(IllegalArgumentException.class,
                () -> saborService.criar(novoSabor("Especial", 35.0, List.of(99L))));
    }

    private SaborRequestDTO novoSabor(String nome, Double preco, List<Long> ingredientesIds) {
        SaborRequestDTO dados = new SaborRequestDTO();
        dados.setNome(nome);
        dados.setPreco(preco);
        dados.setIngredientesIds(ingredientesIds);
        return dados;
    }
}
