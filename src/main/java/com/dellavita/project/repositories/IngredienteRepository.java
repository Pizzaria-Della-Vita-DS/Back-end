
package com.dellavita.project.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dellavita.project.entities.Ingrediente;

public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {

	boolean existsByNomeIgnoreCase(String nome);

	boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

	Optional<Ingrediente> findByNomeIgnoreCase(String nome);

}
