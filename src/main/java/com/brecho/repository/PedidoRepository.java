package com.brecho.repository;

import com.brecho.model.Pedido;
import com.brecho.model.PedidoStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Page<Pedido> findByComprador_UsuarioId(Long compradorId, Pageable pageable);

    List<Pedido> findByComprador_UsuarioIdAndPedidoStatus_PedidoStatusId(Long compradorId, Long pedidoStatusId);

    @Query("SELECT p FROM Pedido p JOIN p.itens i WHERE i.produto.vendedor.id = :vendedorId")
    Page<Pedido> findPedidosDoVendedor(@Param("vendedorId") Long vendedorId, Pageable pageable);

}
