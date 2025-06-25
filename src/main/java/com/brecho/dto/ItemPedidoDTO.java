package com.brecho.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedidoDTO {
    private Long itemPedidoDTO;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private Long produtoId;
    private String nomeProduto;
}
