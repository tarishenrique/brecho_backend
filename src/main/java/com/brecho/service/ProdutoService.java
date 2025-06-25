package com.brecho.service;

import com.brecho.dto.ProdutoDTO;
import com.brecho.exception.BusinessException;
import com.brecho.exception.ResourceNotFoundException;
import com.brecho.mapper.ProdutoMapper;
import com.brecho.model.Produto;
import com.brecho.model.ProdutoCategoria;
import com.brecho.model.Usuario;
import com.brecho.repository.ProdutoRepository;
import com.brecho.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProdutoMapper produtoMapper;

    @Transactional(readOnly = true)
    public Page<ProdutoDTO> findAll(Pageable pageable) {
        return produtoRepository.findByDisponivelTrue(pageable)
                .map(produtoMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public ProdutoDTO findById(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
        return produtoMapper.toDTO(produto);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoDTO> findByCategoria(ProdutoCategoria categoria, Pageable pageable) {
        return produtoRepository.findByDisponivelTrueAndProdutoCategoria_ProdutoCategoriaId(categoria.getProdutoCategoriaId(), pageable)
                .map(produtoMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoDTO> findWithFilters(String nome, ProdutoCategoria categoria,
                                            BigDecimal precoMin, BigDecimal precoMax,
                                            Pageable pageable) {
        return produtoRepository.findProdutosComFiltros(nome, categoria.getProdutoCategoriaId(), precoMin, precoMax, pageable)
                .map(produtoMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> findByVendedor(Long vendedorId) {
        return produtoMapper.toDTOList(produtoRepository.findByVendedor_UsuarioIdAndDisponivelTrue(vendedorId));
    }

    @Transactional(readOnly = true)
    public Page<ProdutoDTO> findMaisRecentes(Pageable pageable) {
        return produtoRepository.findProdutosMaisRecentes(pageable)
                .map(produtoMapper::toDTO);
    }

    public ProdutoDTO create(ProdutoDTO produtoDTO) {
        Usuario vendedor = usuarioRepository.findById(produtoDTO.getVendedorId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor não encontrado com ID: " + produtoDTO.getVendedorId()));

        Produto produto = produtoMapper.toEntity(produtoDTO);
        produto.setVendedor(vendedor);

        Produto produtoSalvo = produtoRepository.save(produto);
        return produtoMapper.toDTO(produtoSalvo);
    }

    public ProdutoDTO update(Long id, ProdutoDTO produtoDTO) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));

        // Verificar se o usuário é o dono do produto
        if (!produto.getVendedor().getUsuarioId().equals(produtoDTO.getVendedorId())) {
            throw new BusinessException("Usuário não tem permissão para editar este produto");
        }

        produtoMapper.updateEntityFromDTO(produtoDTO, produto);

        Produto produtoAtualizado = produtoRepository.save(produto);
        return produtoMapper.toDTO(produtoAtualizado);
    }

    public void delete(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));

        produto.setDisponivel(false);
        produtoRepository.save(produto);
    }

    public ProdutoDTO marcarComoVendido(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));

        produto.setDisponivel(false);
        Produto produtoAtualizado = produtoRepository.save(produto);
        return produtoMapper.toDTO(produtoAtualizado);
    }

}
