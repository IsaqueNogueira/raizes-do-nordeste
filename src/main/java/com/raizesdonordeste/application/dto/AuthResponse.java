package com.raizesdonordeste.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String tokenType;
    private long expiresIn;
    private UsuarioInfo user;

    @Data
    @AllArgsConstructor
    public static class UsuarioInfo {
        private String id;
        private String nome;
        private String perfil;
    }
}