package com.dellavita.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dellavita.project.entities.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, String>{

}
