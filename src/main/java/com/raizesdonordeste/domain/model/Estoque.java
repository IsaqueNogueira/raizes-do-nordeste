package com.raizesdonordeste.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "estoques")
public class Estoque {

    @Id
    private String id;

    private String produtoId;

    private String unidadeId;

    private int quantidade;

    private LocalDateTime ultimaAtualizacao;

    public enum TipoMovimentacao {
        ENTRADA, SAIDA
    }
}