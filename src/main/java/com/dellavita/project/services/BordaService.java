package com.dellavita.project.services;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.dto.BordaRequestDTO;
import com.dellavita.project.entities.Borda;
import com.dellavita.project.entities.Ingrediente;
import com.dellavita.project.repositories.BordaRepository;
import com.dellavita.project.repositories.IngredienteRepository;
import com.dellavita.project.services.exceptions.RecursoNaoEncontradoException;
import com.dellavita.project.services.exceptions.RegistroDuplicadoException;

@Service
public class BordaService {

    private final BordaRepository bordaRepository;
    private final IngredienteRepository ingredienteRepository;

    public BordaService(
            BordaRepository bordaRepository,
            IngredienteRepository ingredienteRepository) {
        this.bordaRepository = bordaRepository;
        this.ingredienteRepository = ingredienteRepository;
    }

    @Transactional(readOnly = true)
    public List<Borda> listar() {
        return bordaRepository.findAll();
    }

    @Transactional
    public Borda criar(BordaRequestDTO dados) {
        validar(dados);
        String nomeNormalizado = dados.getNome().trim();
        if (bordaRepository.existsByNomeIgnoreCase(nomeNormalizado)) {
            throw new RegistroDuplicadoException("Já existe uma borda cadastrada com este nome.");
        }

        Borda borda = new Borda();
        preencher(borda, dados, nomeNormalizado);
        return bordaRepository.save(borda);
    }

    @Transactional
    public Borda atualizar(Long id, BordaRequestDTO dados) {
        validar(dados);
        String nomeNormalizado = dados.getNome().trim();
        if (bordaRepository.existsByNomeIgnoreCaseAndIdNot(nomeNormalizado, id)) {
            throw new RegistroDuplicadoException("Já existe uma borda cadastrada com este nome.");
        }

        Borda borda = bordaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Borda não encontrada."));
        preencher(borda, dados, nomeNormalizado);
        return bordaRepository.save(borda);
    }

    @Transactional
    public void excluir(Long id) {
        if (!bordaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Borda não encontrada.");
        }
        bordaRepository.deleteById(id);
    }

    private void preencher(Borda borda, BordaRequestDTO dados, String nomeNormalizado) {
        borda.setNome(nomeNormalizado);
        borda.setPreco(dados.getPreco());
        borda.setIngredientes(buscarIngredientes(dados.getIngredientesIds()));
    }

    private void validar(BordaRequestDTO dados) {
        if (dados == null || dados.getNome() == null || dados.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome da borda é obrigatório.");
        }
        if (dados.getPreco() == null || !Double.isFinite(dados.getPreco()) || dados.getPreco() < 0) {
            throw new IllegalArgumentException("O preço da borda deve ser maior ou igual a zero.");
        }
    }

    private List<Ingrediente> buscarIngredientes(List<Long> ids) {
        List<Long> idsDistintos = ids == null
                ? List.of()
                : ids.stream().filter(Objects::nonNull).distinct().toList();
        List<Ingrediente> ingredientes = ingredienteRepository.findAllById(idsDistintos);
        if (ingredientes.size() != idsDistintos.size()) {
            throw new IllegalArgumentException("Um ou mais ingredientes da borda não foram encontrados.");
        }
        return ingredientes;
    }
}
