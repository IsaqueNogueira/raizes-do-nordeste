package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.domain.model.Unidade;
import com.raizesdonordeste.domain.repository.UnidadeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/unidades")
@Tag(name = "Unidades", description = "Endpoints de unidades da rede")
public class UnidadeController {

    @Autowired
    private UnidadeRepository unidadeRepository;

    @GetMapping
    @Operation(summary = "Listar todas as unidades ativas")
    public ResponseEntity<List<Unidade>> listar() {
        return ResponseEntity.ok(unidadeRepository.findByAtivaTrue());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar unidade por ID")
    public ResponseEntity<Unidade> buscarPorId(@PathVariable String id) {
        return unidadeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar nova unidade")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Unidade> criar(@Valid @RequestBody Unidade unidade) {
        unidade.setAtiva(true);
        return ResponseEntity.status(201).body(unidadeRepository.save(unidade));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar unidade")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Unidade> atualizar(@PathVariable String id, @RequestBody Unidade unidade) {
        return unidadeRepository.findById(id).map(u -> {
            unidade.setId(id);
            return ResponseEntity.ok(unidadeRepository.save(unidade));
        }).orElse(ResponseEntity.notFound().build());
    }
}