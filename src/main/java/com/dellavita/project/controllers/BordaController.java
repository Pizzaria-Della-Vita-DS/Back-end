package com.dellavita.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dellavita.project.entities.Borda;
import com.dellavita.project.services.BordaService;

@RestController
@RequestMapping(value="/bordas")
public class BordaController {

	@Autowired
	private BordaService bordaService;
	
	@GetMapping
	public List<Borda> listar(){
		return bordaService.listar();
	}
	
	@PostMapping
    public Borda criar(@RequestBody Borda borda) {
        return bordaService.criar(borda);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Borda> atualizar(@PathVariable Long id, @RequestBody Borda borda) {
        return ResponseEntity.ok(bordaService.atualizar(id, borda));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        bordaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
    
}