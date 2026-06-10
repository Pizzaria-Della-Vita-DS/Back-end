package com.dellavita.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dellavita.project.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

}
