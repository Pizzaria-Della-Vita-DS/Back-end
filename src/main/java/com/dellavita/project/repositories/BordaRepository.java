
package com.dellavita.project.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dellavita.project.entities.Borda;

public interface BordaRepository extends JpaRepository<Borda, Long> {

	boolean existsByNomeIgnoreCase(String nome);

	boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

	Optional<Borda> findByNomeIgnoreCase(String nome);

}
