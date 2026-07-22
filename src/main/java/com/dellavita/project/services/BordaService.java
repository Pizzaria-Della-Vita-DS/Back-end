
package com.dellavita.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.entities.Borda;
import com.dellavita.project.repositories.BordaRepository;
import com.dellavita.project.services.exceptions.RegistroDuplicadoException;

@Service
public class BordaService {

    @Autowired
    private BordaRepository bordaRepository;

    @Transactional
    public List<Borda> listar(){
        return bordaRepository.findAll();
    }

    @Transactional
    public Borda criar(Borda borda) {
        String nomeNormalizado = borda.getNome().trim();
        if (bordaRepository.existsByNomeIgnoreCase(nomeNormalizado)) {
            throw new RegistroDuplicadoException("Já existe uma borda cadastrada com este nome.");
        }
        borda.setNome(nomeNormalizado);
        return bordaRepository.save(borda);
    }

    @Transactional
    public Borda atualizar(Long id, Borda  borda) {
        String nomeNormalizado = borda.getNome().trim();
        if (bordaRepository.existsByNomeIgnoreCaseAndIdNot(nomeNormalizado, id)) {
            throw new RegistroDuplicadoException("Já existe uma borda cadastrada com este nome.");
        }
        Borda bordaNovo = bordaRepository.findById(id).orElseThrow();
        bordaNovo.setNome(nomeNormalizado);
        bordaNovo.setPreco(borda.getPreco());
        return bordaRepository.save(bordaNovo);
    }

    @Transactional
    public void excluir(Long id) {
        bordaRepository.deleteById(id);
    }
}