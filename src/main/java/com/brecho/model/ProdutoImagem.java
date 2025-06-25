package com.brecho.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "produto_imagem")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoImagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long produtoImagemId;

    @Column(name = "url_imagem", nullable = false, length = 500)
    private String urlImagem;

    @Column(name = "imagem_principal")
    private Boolean imagemPrincipal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
}
