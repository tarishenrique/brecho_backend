package com.brecho.mapper;

import com.brecho.dto.ItemPedidoDTO;
import com.brecho.dto.PedidoDTO;
import com.brecho.model.ItemPedido;
import com.brecho.model.Pedido;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PedidoMapper {
    @Mapping(target = "compradorId", source = "comprador.usuarioId")
    @Mapping(target = "nomeComprador", source = "comprador.nome")
    PedidoDTO toDTO(Pedido pedido);

    List<PedidoDTO> toPedidoDTOList(List<Pedido> pedidos);

    @Mapping(target = "produtoId", source = "produto.produtoId")
    @Mapping(target = "nomeProduto", source = "produto.nome")
    ItemPedidoDTO toDTO(ItemPedido itemPedido);

    List<ItemPedidoDTO> toItemPedidoDTOList(List<ItemPedido> itens);
}
