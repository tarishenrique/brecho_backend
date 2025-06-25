package com.brecho.controller;

import com.brecho.dto.ProdutoDTO;
import com.brecho.model.ProdutoCategoria;
import com.brecho.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<Page<ProdutoDTO>> listarTodos(@PageableDefault(size = 20) Pageable pageable) {
        Page<ProdutoDTO> produtos = produtoService.findAll(pageable);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.findById(id));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<Page<ProdutoDTO>> buscarPorCategoria(@PathVariable ProdutoCategoria categoria, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(produtoService.findByCategoria(categoria, pageable));
    }

    @PostMapping
    public ResponseEntity<ProdutoDTO> criar(@RequestBody ProdutoDTO produtoDTO) {
        ProdutoDTO produtoSalvo = produtoService.create(produtoDTO);
        return ResponseEntity.created(URI.create("/api/produtos/" + produtoSalvo.getProdutoId()))
                .body(produtoSalvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
