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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dellavita.project.dto.BordaRequestDTO;
import com.dellavita.project.entities.Borda;
import com.dellavita.project.entities.Ingrediente;
import com.dellavita.project.repositories.BordaRepository;
import com.dellavita.project.repositories.IngredienteRepository;

@ExtendWith(MockitoExtension.class)
class BordaServiceTests {

    @Mock
    private BordaRepository bordaRepository;

    @Mock
    private IngredienteRepository ingredienteRepository;

    @InjectMocks
    private BordaService bordaService;

    @Test
    void deveCriarBordaComIngredientesEDisponibilidadeCalculada() {
        Ingrediente catupiry = new Ingrediente(1L, "Catupiry", true);
        Ingrediente mussarela = new Ingrediente(2L, "Mussarela", false);
        when(ingredienteRepository.findAllById(List.of(1L, 2L)))
                .thenReturn(List.of(catupiry, mussarela));
        when(bordaRepository.save(any(Borda.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));

        BordaRequestDTO dados = novaBorda("  Especial  ", 8.5, List.of(1L, 2L));
        Borda criada = bordaService.criar(dados);

        assertEquals("Especial", criada.getNome());
        assertEquals(8.5, criada.getPreco());
        assertEquals(List.of(catupiry, mussarela), criada.getIngredientes());
        assertFalse(criada.isDisponivel());
    }

    @Test
    void deveRejeitarNomeVazioEPrecoInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> bordaService.criar(novaBorda("   ", 5.0, List.of())));
        assertThrows(IllegalArgumentException.class,
                () -> bordaService.criar(novaBorda("Catupiry", -1.0, List.of())));
        assertThrows(IllegalArgumentException.class,
                () -> bordaService.criar(novaBorda("Catupiry", null, List.of())));
    }

    @Test
    void devePermitirBordaSemIngredientes() {
        when(bordaRepository.save(any(Borda.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));

        Borda criada = bordaService.criar(novaBorda("Catupiry", 5.0, List.of()));

        assertTrue(criada.getIngredientes().isEmpty());
        assertTrue(criada.isDisponivel());
    }

    @Test
    void deveRejeitarIngredienteInexistente() {
        when(ingredienteRepository.findAllById(List.of(99L))).thenReturn(List.of());

        assertThrows(IllegalArgumentException.class,
                () -> bordaService.criar(novaBorda("Especial", 5.0, List.of(99L))));
    }

    private BordaRequestDTO novaBorda(String nome, Double preco, List<Long> ingredientesIds) {
        BordaRequestDTO dados = new BordaRequestDTO();
        dados.setNome(nome);
        dados.setPreco(preco);
        dados.setIngredientesIds(ingredientesIds);
        return dados;
    }
}
