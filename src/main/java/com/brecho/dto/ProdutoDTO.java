package com.brecho.dto;

import com.brecho.model.ProdutoCategoria;
import com.brecho.model.ProdutoCondicao;
import com.brecho.model.ProdutoTamanho;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoDTO {
    private Long produtoId;

    @NotBlank(message = "Nome do produto é obrigatório")
    @Size(min = 3, max = 150, message = "Nome deve ter entre 3 e 150 caracteres")
    private String nome;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    private BigDecimal preco;

    private ProdutoCategoria produtoCategoria;
    private ProdutoTamanho produtoTamanho;
    private ProdutoCondicao produtoCondicao;
    private String marca;
    private String cor;
    private LocalDateTime dataCriacao;
    private Boolean disponivel;
    private Long vendedorId;
    private String nomeVendedor;
    private List<String> urlsImagens;
}
