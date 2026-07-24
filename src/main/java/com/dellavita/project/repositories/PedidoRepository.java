package com.dellavita.project.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dellavita.project.entities.Pedido;
import com.dellavita.project.enums.Estado;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @EntityGraph(attributePaths = {"cliente", "pizzas"})
    @Query("select distinct p from Pedido p order by p.data_hora desc")
    List<Pedido> listarMaisRecentes();

    @EntityGraph(attributePaths = {"cliente", "pizzas"})
    @Query("select distinct p from Pedido p where p.estado not in :estados order by p.data_hora desc")
    List<Pedido> listarAtivos(@Param("estados") Collection<Estado> estados);

    @EntityGraph(attributePaths = {"cliente", "pizzas"})
    @Query("select distinct p from Pedido p where p.cliente.cpf = :cpf order by p.data_hora desc")
    List<Pedido> listarPorCliente(@Param("cpf") String cpf);
}
