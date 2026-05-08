package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.domain.model.Fidelidade;
import com.raizesdonordeste.domain.repository.FidelidadeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fidelidade")
@Tag(name = "Fidelidade", description = "Endpoints do programa de fidelidade")
public class FidelidadeController {

    @Autowired
    private FidelidadeRepository fidelidadeRepository;

    @GetMapping("/{clienteId}")
    @Operation(summary = "Consultar pontos de fidelidade do cliente")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'CLIENTE')")
    public ResponseEntity<Fidelidade> consultar(@PathVariable String clienteId) {
        return fidelidadeRepository.findByClienteId(clienteId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}