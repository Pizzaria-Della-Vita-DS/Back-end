package com.dellavita.project.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.entities.Ingrediente;
import com.dellavita.project.repositories.IngredienteRepository;
import com.dellavita.project.services.exceptions.RecursoNaoEncontradoException;
import com.dellavita.project.services.exceptions.RegistroDuplicadoException;

@Service
public class IngredienteService {

    private final IngredienteRepository ingredienteRepository;

    public IngredienteService(IngredienteRepository ingredienteRepository) {
        this.ingredienteRepository = ingredienteRepository;
    }

    @Transactional(readOnly = true)
    public List<Ingrediente> listar() {
        return ingredienteRepository.findAll();
    }

    @Transactional
    public Ingrediente criar(Ingrediente ingrediente) {
        validar(ingrediente);
        String nomeNormalizado = ingrediente.getNome().trim();
        if (ingredienteRepository.existsByNomeIgnoreCase(nomeNormalizado)) {
            throw new RegistroDuplicadoException("Já existe um ingrediente cadastrado com este nome.");
        }
        ingrediente.setNome(nomeNormalizado);
        return ingredienteRepository.save(ingrediente);
    }

    @Transactional
    public Ingrediente atualizar(Long id, Ingrediente ingrediente) {
        validar(ingrediente);
        String nomeNormalizado = ingrediente.getNome().trim();
        if (ingredienteRepository.existsByNomeIgnoreCaseAndIdNot(nomeNormalizado, id)) {
            throw new RegistroDuplicadoException("Já existe um ingrediente cadastrado com este nome.");
        }
        Ingrediente ingredienteExistente = buscarPorId(id);
        ingredienteExistente.setNome(nomeNormalizado);
        ingredienteExistente.setDisponivel(ingrediente.isDisponivel());
        return ingredienteRepository.save(ingredienteExistente);
    }

    @Transactional
    public Ingrediente alternar(Long id, boolean disponivel) {
        Ingrediente ingrediente = buscarPorId(id);
        ingrediente.setDisponivel(disponivel);
        return ingredienteRepository.save(ingrediente);
    }

    @Transactional
    public void excluir(Long id) {
        Ingrediente ingrediente = buscarPorId(id);
        boolean vinculadoASabor = ingrediente.getSabores() != null && !ingrediente.getSabores().isEmpty();
        boolean vinculadoABorda = ingrediente.getBordas() != null && !ingrediente.getBordas().isEmpty();
        if (vinculadoASabor || vinculadoABorda) {
            throw new IllegalArgumentException(
                    "O ingrediente está vinculado a sabores ou bordas. Remova esses vínculos antes de excluí-lo.");
        }
        ingredienteRepository.deleteById(id);
    }

    private Ingrediente buscarPorId(Long id) {
        return ingredienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ingrediente não encontrado."));
    }

    private void validar(Ingrediente ingrediente) {
        if (ingrediente == null || ingrediente.getNome() == null || ingrediente.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do ingrediente é obrigatório.");
        }
    }
}
