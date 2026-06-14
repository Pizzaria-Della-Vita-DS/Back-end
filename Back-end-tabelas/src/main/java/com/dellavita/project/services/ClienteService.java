package com.dellavita.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.entities.Cliente;
import com.dellavita.project.repositories.ClienteRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Transactional
	public Cliente create(Cliente cliente) {
		return clienteRepository.save(cliente);
	}
	
	@Transactional
	public List<Cliente> findAll(){
		return clienteRepository.findAll();
	}
	
	@Transactional
	public Cliente update(Long id, Cliente cliente) {
		Cliente clienteNovo = clienteRepository.findById(id).orElseThrow();
		clienteNovo.setNome(cliente.getNome());
		clienteNovo.setCpf(cliente.getCpf());
		clienteNovo.setLogin(cliente.getLogin());
		clienteNovo.setSenha(cliente.getSenha());
		clienteNovo.setTelefone(cliente.getTelefone());
		clienteNovo.setEndereco(cliente.getEndereco());
		return clienteRepository.save(clienteNovo);
	}
	
	@Transactional
	public void delete(Long id) {
		clienteRepository.deleteById(id);
	}
	
}
