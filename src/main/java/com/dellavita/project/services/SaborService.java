package com.dellavita.project.services;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.dto.SaborRequestDTO;
import com.dellavita.project.entities.Ingrediente;
import com.dellavita.project.entities.Sabor;
import com.dellavita.project.repositories.IngredienteRepository;
import com.dellavita.project.repositories.SaborRepository;
import com.dellavita.project.services.exceptions.RecursoNaoEncontradoException;
import com.dellavita.project.services.exceptions.RegistroDuplicadoException;

@Service
public class SaborService {

    private final SaborRepository saborRepository;
    private final IngredienteRepository ingredienteRepository;

    public SaborService(
            SaborRepository saborRepository,
            IngredienteRepository ingredienteRepository) {
        this.saborRepository = saborRepository;
        this.ingredienteRepository = ingredienteRepository;
    }

    @Transactional(readOnly = true)
    public List<Sabor> listar() {
        return saborRepository.findAll();
    }

    @Transactional
    public Sabor criar(SaborRequestDTO dados) {
        validar(dados);
        String nomeNormalizado = dados.getNome().trim();
        if (saborRepository.existsByNomeIgnoreCase(nomeNormalizado)) {
            throw new RegistroDuplicadoException("Já existe um sabor cadastrado com este nome.");
        }

        Sabor sabor = new Sabor();
        preencher(sabor, dados, nomeNormalizado);
        return saborRepository.save(sabor);
    }

    @Transactional
    public Sabor atualizar(Long id, SaborRequestDTO dados) {
        validar(dados);
        String nomeNormalizado = dados.getNome().trim();
        if (saborRepository.existsByNomeIgnoreCaseAndIdNot(nomeNormalizado, id)) {
            throw new RegistroDuplicadoException("Já existe um sabor cadastrado com este nome.");
        }

        Sabor sabor = saborRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sabor não encontrado."));
        preencher(sabor, dados, nomeNormalizado);
        return saborRepository.save(sabor);
    }

    @Transactional
    public void excluir(Long id) {
        if (!saborRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Sabor não encontrado.");
        }
        saborRepository.deleteById(id);
    }

    private void preencher(Sabor sabor, SaborRequestDTO dados, String nomeNormalizado) {
        sabor.setNome(nomeNormalizado);
        sabor.setPreco(dados.getPreco());
        sabor.setIngredientes(buscarIngredientes(dados.getIngredientesIds()));
    }

    private void validar(SaborRequestDTO dados) {
        if (dados == null || dados.getNome() == null || dados.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do sabor é obrigatório.");
        }
        if (dados.getPreco() == null || !Double.isFinite(dados.getPreco()) || dados.getPreco() < 0) {
            throw new IllegalArgumentException("O preço do sabor deve ser maior ou igual a zero.");
        }
    }

    private List<Ingrediente> buscarIngredientes(List<Long> ids) {
        List<Long> idsDistintos = ids == null
                ? List.of()
                : ids.stream().filter(Objects::nonNull).distinct().toList();
        List<Ingrediente> ingredientes = ingredienteRepository.findAllById(idsDistintos);
        if (ingredientes.size() != idsDistintos.size()) {
            throw new IllegalArgumentException("Um ou mais ingredientes do sabor não foram encontrados.");
        }
        return ingredientes;
    }
}
