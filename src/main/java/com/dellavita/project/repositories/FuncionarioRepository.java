package com.dellavita.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dellavita.project.entities.Funcionario;
import com.dellavita.project.enums.Status;

public interface FuncionarioRepository extends JpaRepository<Funcionario, String> {

	List<Funcionario> findByStatus(Status status);

}