
package com.dellavita.project.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dellavita.project.entities.Funcionario;
import com.dellavita.project.enums.Status;

public interface FuncionarioRepository extends JpaRepository<Funcionario, String> {

    Optional<Funcionario> findByLogin(String login);

    Optional<Funcionario> findByLoginIgnoreCase(String login);

    boolean existsByLogin(String login);

    boolean existsByLoginIgnoreCase(String login);

    List<Funcionario> findByStatus(Status status);

}