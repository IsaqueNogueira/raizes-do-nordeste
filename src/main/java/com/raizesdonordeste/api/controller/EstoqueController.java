package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.application.dto.MovimentacaoEstoqueRequest;
import com.raizesdonordeste.application.service.EstoqueService;
import com.raizesdonordeste.domain.model.Estoque;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estoque")
@Tag(name = "Estoque", description = "Endpoints de controle de estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @PostMapping("/movimentacao")
    @Operation(summary = "Registrar movimentação de estoque")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<Estoque> movimentar(@Valid @RequestBody MovimentacaoEstoqueRequest request) {
        return ResponseEntity.ok(estoqueService.movimentar(request));
    }

    @GetMapping("/unidade/{unidadeId}")
    @Operation(summary = "Consultar estoque por unidade")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<List<Estoque>> listarPorUnidade(@PathVariable String unidadeId) {
        return ResponseEntity.ok(estoqueService.listarPorUnidade(unidadeId));
    }

    @GetMapping
    @Operation(summary = "Consultar estoque por produto e unidade")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<Estoque> buscar(@RequestParam String produtoId,
                                          @RequestParam String unidadeId) {
        return ResponseEntity.ok(estoqueService.buscarPorProdutoEUnidade(produtoId, unidadeId));
    }
}