package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.domain.model.Produto;
import com.raizesdonordeste.domain.repository.ProdutoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@Tag(name = "Produtos", description = "Endpoints de produtos e cardápio")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    @Operation(summary = "Listar produtos por unidade")
    public ResponseEntity<List<Produto>> listar(@RequestParam(required = false) String unidadeId) {
        if (unidadeId != null) {
            return ResponseEntity.ok(produtoRepository.findByUnidadeIdAndDisponivelTrue(unidadeId));
        }
        return ResponseEntity.ok(produtoRepository.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    public ResponseEntity<Produto> buscarPorId(@PathVariable String id) {
        return produtoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar novo produto")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<Produto> criar(@RequestBody Produto produto) {
        produto.setDisponivel(true);
        return ResponseEntity.status(201).body(produtoRepository.save(produto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<Produto> atualizar(@PathVariable String id, @RequestBody Produto produto) {
        return produtoRepository.findById(id).map(p -> {
            produto.setId(id);
            return ResponseEntity.ok(produtoRepository.save(produto));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover produto")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        return produtoRepository.findById(id).map(p -> {
            produtoRepository.delete(p);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}