package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.application.dto.AuthResponse;
import com.raizesdonordeste.application.dto.LoginRequest;
import com.raizesdonordeste.application.dto.RegisterRequest;
import com.raizesdonordeste.application.service.AuthService;
import com.raizesdonordeste.domain.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints de autenticação")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login do usuário")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    @Operation(summary = "Cadastro de novo usuário")
    public ResponseEntity<Usuario> register(@Valid @RequestBody RegisterRequest request) {
        Usuario usuario = authService.registrar(request);
        usuario.setSenha(null);
        return ResponseEntity.status(201).body(usuario);
    }
}