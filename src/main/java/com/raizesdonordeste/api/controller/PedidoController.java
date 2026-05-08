package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.application.dto.CriarPedidoRequest;
import com.raizesdonordeste.application.service.PedidoService;
import com.raizesdonordeste.domain.model.Pedido;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Endpoints de pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    @Operation(summary = "Criar novo pedido")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ATENDENTE', 'ADMIN')")
    public ResponseEntity<Pedido> criar(@Valid @RequestBody CriarPedidoRequest request,
                                        Authentication auth) {
        return ResponseEntity.status(201).body(pedidoService.criarPedido(request, auth.getName()));
    }

    @GetMapping
    @Operation(summary = "Listar pedidos com filtros")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COZINHA', 'ATENDENTE')")
    public ResponseEntity<List<Pedido>> listar(
            @RequestParam(required = false) Pedido.CanalPedido canalPedido,
            @RequestParam(required = false) Pedido.StatusPedido status) {
        return ResponseEntity.ok(pedidoService.listarPedidos(canalPedido, status));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do pedido")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COZINHA', 'ATENDENTE')")
    public ResponseEntity<Pedido> atualizarStatus(@PathVariable String id,
                                                   @RequestParam Pedido.StatusPedido status) {
        return ResponseEntity.ok(pedidoService.atualizarStatus(id, status));
    }
}