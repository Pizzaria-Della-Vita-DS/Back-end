package com.dellavita.project.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dellavita.project.entities.Ingrediente;
import com.dellavita.project.entities.Sabor;
import com.dellavita.project.repositories.IngredienteRepository;

@ExtendWith(MockitoExtension.class)
class IngredienteServiceTests {

    @Mock
    private IngredienteRepository ingredienteRepository;

    @InjectMocks
    private IngredienteService ingredienteService;

    @Test
    void deveNormalizarNomeAoCriar() {
        when(ingredienteRepository.save(any(Ingrediente.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));

        Ingrediente criado = ingredienteService.criar(
                new Ingrediente(null, "  Queijo mussarela  ", true));

        assertEquals("Queijo mussarela", criado.getNome());
    }

    @Test
    void deveAlternarDisponibilidade() {
        Ingrediente ingrediente = new Ingrediente(1L, "Queijo", true);
        when(ingredienteRepository.findById(1L)).thenReturn(Optional.of(ingrediente));
        when(ingredienteRepository.save(any(Ingrediente.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));

        Ingrediente alterado = ingredienteService.alternar(1L, false);

        assertFalse(alterado.isDisponivel());
    }

    @Test
    void deveImpedirExclusaoQuandoIngredienteEstaEmUso() {
        Ingrediente ingrediente = new Ingrediente(1L, "Queijo", true);
        ingrediente.setSabores(List.of(new Sabor()));
        when(ingredienteRepository.findById(1L)).thenReturn(Optional.of(ingrediente));

        assertThrows(IllegalArgumentException.class, () -> ingredienteService.excluir(1L));
    }
}
