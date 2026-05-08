package com.raizesdonordeste.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "usuarios")
public class Usuario {

    @Id
    private String id;

    private String nome;

    @Indexed(unique = true)
    private String email;

    private String senha;

    private PerfilUsuario perfil;

    private boolean consentimentoLGPD;

    private LocalDateTime dataCadastro;

    private boolean ativo;

    public enum PerfilUsuario {
        ADMIN, GERENTE, CLIENTE, COZINHA, ATENDENTE
    }
}