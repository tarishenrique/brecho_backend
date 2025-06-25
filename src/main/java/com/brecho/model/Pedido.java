package com.brecho.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedido")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pedidoId;

    @Column
    private LocalDateTime dataPedido;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorTotal;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "status_pedido")
//    private StatusPedido statusPedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedidoStatusId", nullable = false)
    private PedidoStatus pedidoStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compradorId", nullable = false)
    private Usuario comprador;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemPedido> itens;


    @PrePersist
    protected void onCreate() {
        dataPedido = LocalDateTime.now();
//        if (statusPedido == null) {
//            statusPedido = StatusPedido.PENDENTE;
//        }
    }

}
