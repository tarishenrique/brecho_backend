package com.brecho.dto;

import com.brecho.model.PedidoStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoDTO {
    private Long id;
    private LocalDateTime dataPedido;
    private BigDecimal valorTotal;
    private PedidoStatus pedidoStatus;
    private Long compradorId;
    private String nomeComprador;
    private List<ItemPedidoDTO> itens;
}
