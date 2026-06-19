package com.dellavita.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dellavita.project.entities.Funcionario;
import com.dellavita.project.entities.Solicitacao;
import com.dellavita.project.services.SolicitacaoService;

@RestController
@RequestMapping(value="/solicitacoes-acesso")
public class SolicitacaoController {

	@Autowired
	private SolicitacaoService solicitacaoService;
	
	@GetMapping
	public List<Solicitacao> listar(){
		return solicitacaoService.listar();
	}
	
	@PostMapping
    public Solicitacao criar(@RequestBody Solicitacao solicitacao) {
        return solicitacaoService.criar(solicitacao);
    }

    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<Funcionario> aprovar(@PathVariable Long id, @RequestBody boolean disponivel){
    	return ResponseEntity.ok(solicitacaoService.aprovar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
    	solicitacaoService.recusar(id);
        return ResponseEntity.noContent().build();
    }
	
}