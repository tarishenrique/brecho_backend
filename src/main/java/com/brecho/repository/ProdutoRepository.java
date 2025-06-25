package com.brecho.repository;

import com.brecho.model.Produto;
import com.brecho.model.ProdutoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Page<Produto> findByDisponivelTrue(Pageable pageable);

    Page<Produto> findByDisponivelTrueAndProdutoCategoria_ProdutoCategoriaId(Long produtoCategoriaId, Pageable pageable);

    @Query("SELECT p FROM Produto p WHERE p.disponivel = true AND " +
            "(:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
            "(:categoria IS NULL OR p.produtoCategoria.produtoCategoriaId = :categoriaId) AND " +
            "(:precoMin IS NULL OR p.preco >= :precoMin) AND " +
            "(:precoMax IS NULL OR p.preco <= :precoMax)")
    Page<Produto> findProdutosComFiltros(@Param("nome") String nome,
                                         @Param("categoriaId") Long categoriaId,
                                         @Param("precoMin") BigDecimal precoMin,
                                         @Param("precoMax") BigDecimal precoMax,
                                         Pageable pageable);

    List<Produto> findByVendedor_UsuarioIdAndDisponivelTrue(Long vendedorId);

    @Query("SELECT p FROM Produto p WHERE p.disponivel = true ORDER BY p.dataCriacao DESC")
    Page<Produto> findProdutosMaisRecentes(Pageable pageable);

}
