package com.brecho.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "produto")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long produtoId;

    @NotBlank(message = "Nome do produto é obrigatório")
    @Size(min = 3, max = 150, message = "Nome deve ter entre 3 e 150 caracteres")
    @Column(nullable = false, length = 150)
    private String nome;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    @Column(length = 500)
    private String descricao;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(length = 50)
    private String marca;

    @Column(length = 30)
    private String cor;

    @Column
    private LocalDateTime dataCriacao;

    @Column(name = "disponivel")
    private Boolean disponivel;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "categoria")
//    private CategoriaProduto categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produtoCategoriaId")
    private ProdutoCategoria produtoCategoria;

//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "tamanho")
//    private Tamanho tamanho;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produtoTamanhoId")
    private ProdutoTamanho produtoTamanho;

//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "condicao")
//    private CondicaoProduto condicao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produtoCondicaoId")
    private ProdutoCondicao produtoCondicao;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedorId", nullable = false)
    private Usuario vendedor;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProdutoImagem> imagens;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemPedido> itensPedido;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        if (disponivel == null) {
            disponivel = true;
        }
    }
}