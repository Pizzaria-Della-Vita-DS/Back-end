package com.dellavita.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dellavita.project.entities.Ingrediente;
import com.dellavita.project.services.IngredienteService;

@RestController
@RequestMapping(value="/ingredientes")
public class IngredienteController {

	@Autowired
	private IngredienteService ingredienteService;
	
	@GetMapping
	public List<Ingrediente> listar(){
		return ingredienteService.listar();
	}
	
	@PostMapping
    public Ingrediente criar(@RequestBody Ingrediente ingrediente) {
        return ingredienteService.criar(ingrediente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ingrediente> atualizar(@PathVariable Long id, @RequestBody Ingrediente ingrediente) {
        return ResponseEntity.ok(ingredienteService.atualizar(id, ingrediente));
    }
    
    @PatchMapping("/{id}/disponibilidade")
    public ResponseEntity<Ingrediente> alternar(@PathVariable Long id, @RequestBody boolean disponivel){
    	return ResponseEntity.ok(ingredienteService.alternar(id, disponivel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
    	ingredienteService.excluir(id);
        return ResponseEntity.noContent().build();
    }
	
}