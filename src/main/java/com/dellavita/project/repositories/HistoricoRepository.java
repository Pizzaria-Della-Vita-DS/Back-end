package com.dellavita.project.repositories;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dellavita.project.entities.Historico;

public interface HistoricoRepository extends JpaRepository<Historico, LocalDate>{

}
