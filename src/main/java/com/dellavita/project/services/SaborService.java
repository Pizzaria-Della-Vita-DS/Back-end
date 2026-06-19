package com.dellavita.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.entities.Sabor;
import com.dellavita.project.repositories.SaborRepository;

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
        return saborRepository.save(sabor);
    }

    @Transactional
    public Sabor atualizar(Long id, Sabor sabor) {
        Sabor saborNovo = saborRepository.findById(id).orElseThrow();
        saborNovo.setNome(sabor.getNome());
        saborNovo.setIngredientes(sabor.getIngredientes());
        saborNovo.setPreco(sabor.getPreco());
        return saborRepository.save(saborNovo);
    }

    @Transactional
    public void excluir(Long id) {
        saborRepository.deleteById(id);
    }
}