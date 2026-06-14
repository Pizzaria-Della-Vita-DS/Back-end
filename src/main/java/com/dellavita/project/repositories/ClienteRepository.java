package com.dellavita.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dellavita.project.entities.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{

}
