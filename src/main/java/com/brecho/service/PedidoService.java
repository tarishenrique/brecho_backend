package com.brecho.service;

import com.brecho.dto.ItemPedidoDTO;
import com.brecho.dto.PedidoDTO;
import com.brecho.exception.BusinessException;
import com.brecho.exception.ResourceNotFoundException;
import com.brecho.mapper.PedidoMapper;
import com.brecho.model.*;
import com.brecho.repository.PedidoRepository;
import com.brecho.repository.ProdutoRepository;
import com.brecho.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoMapper pedidoMapper;

    @Transactional(readOnly = true)
    public Page<PedidoDTO> findAll(Pageable pageable) {
        return pedidoRepository.findAll(pageable)
                .map(pedidoMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public PedidoDTO findById(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com ID: " + id));
        return pedidoMapper.toDTO(pedido);
    }

    @Transactional(readOnly = true)
    public Page<PedidoDTO> findByComprador(Long compradorId, Pageable pageable) {
        return pedidoRepository.findByComprador_UsuarioId(compradorId, pageable)
                .map(pedidoMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<PedidoDTO> findPedidosDoVendedor(Long vendedorId, Pageable pageable) {
        return pedidoRepository.findPedidosDoVendedor(vendedorId, pageable)
                .map(pedidoMapper::toDTO);
    }

    public PedidoDTO create(Long compradorId, List<ItemPedidoDTO> itensDTO) {
        Usuario comprador = usuarioRepository.findById(compradorId)
                .orElseThrow(() -> new ResourceNotFoundException("Comprador não encontrado com ID: " + compradorId));

        Pedido pedido = new Pedido();
        pedido.setComprador(comprador);

        List<ItemPedido> itens = new ArrayList<>();
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemPedidoDTO itemDTO : itensDTO) {
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + itemDTO.getProdutoId()));

            if (!produto.getDisponivel()) {
                throw new BusinessException("Produto não está disponível: " + produto.getNome());
            }

            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoUnitario(produto.getPreco());
            item.setPedido(pedido);

            itens.add(item);
            valorTotal = valorTotal.add(produto.getPreco().multiply(BigDecimal.valueOf(itemDTO.getQuantidade())));

            // Marcar produto como vendido
            produto.setDisponivel(false);
        }

        pedido.setItens(itens);
        pedido.setValorTotal(valorTotal);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return pedidoMapper.toDTO(pedidoSalvo);
    }

    public PedidoDTO updateStatus(Long id, PedidoStatus novoStatus) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com ID: " + id));

        pedido.setPedidoStatus(novoStatus);

        Pedido pedidoAtualizado = pedidoRepository.save(pedido);
        return pedidoMapper.toDTO(pedidoAtualizado);
    }

    public void cancelar(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com ID: " + id));

        if (pedido.getPedidoStatus().getNome().equals("ENTREGUE")) {
            throw new BusinessException("Não é possível cancelar um pedido já entregue");
        }

        PedidoStatus pedidoStatusCancelado = new PedidoStatus();
        pedidoStatusCancelado.setPedidoStatusId(5L);
        pedido.setPedidoStatus(pedidoStatusCancelado);

        // Retornar produtos para disponível
        for (ItemPedido item : pedido.getItens()) {
            item.getProduto().setDisponivel(true);
        }

        pedidoRepository.save(pedido);
    }

}
