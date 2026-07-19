package com.dellavita.project.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dellavita.project.entities.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, String> {

    Optional<Cliente> findByLogin(String login);

    boolean existsByLogin(String login);

}