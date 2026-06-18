package com.dellavita.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dellavita.project.entities.Sabor;
import com.dellavita.project.services.SaborService;

@RestController
@RequestMapping(value="/sabores")
public class SaborController {

	@Autowired
	private SaborService saborService;
	
	@GetMapping
	public List<Sabor> listar(){
		return saborService.listar();
	}
	
	@PostMapping
    public Sabor criar(@RequestBody Sabor sabor) {
        return saborService.criar(sabor);
    }

    @PutMapping("/{id}")
    public Sabor atualizar(@PathVariable Long id, @RequestBody Sabor sabor) {
        return saborService.atualizar(id, sabor);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        saborService.excluir(id);
    }
	
}
