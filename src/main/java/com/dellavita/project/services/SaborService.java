
package com.dellavita.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.entities.Sabor;
import com.dellavita.project.repositories.SaborRepository;
import com.dellavita.project.services.exceptions.RegistroDuplicadoException;

@Service
public class SaborService {

    @Autowired
    private SaborRepository saborRepository;

    @Transactional
    public List<Sabor> listar(){
        return saborRepository.findAll();
    }

    @Transactional
    public Sabor criar(Sabor sabor) {
        String nomeNormalizado = sabor.getNome().trim();
        if (saborRepository.existsByNomeIgnoreCase(nomeNormalizado)) {
            throw new RegistroDuplicadoException("Já existe um sabor cadastrado com este nome.");
        }
        sabor.setNome(nomeNormalizado);
        return saborRepository.save(sabor);
    }

    @Transactional
    public Sabor atualizar(Long id, Sabor sabor) {
        String nomeNormalizado = sabor.getNome().trim();
        if (saborRepository.existsByNomeIgnoreCaseAndIdNot(nomeNormalizado, id)) {
            throw new RegistroDuplicadoException("Já existe um sabor cadastrado com este nome.");
        }
        Sabor saborNovo = saborRepository.findById(id).orElseThrow();
        saborNovo.setNome(nomeNormalizado);
        saborNovo.setIngredientes(sabor.getIngredientes());
        saborNovo.setPreco(sabor.getPreco());
        return saborRepository.save(saborNovo);
    }

    @Transactional
    public void excluir(Long id) {
        saborRepository.deleteById(id);
    }
}