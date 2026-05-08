package com.raizesdonordeste.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "fidelidades")
public class Fidelidade {

    @Id
    private String id;

    private String clienteId;

    private int pontos;

    private List<HistoricoPontos> historico;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HistoricoPontos {
        private String descricao;
        private int pontos;
        private TipoMovimentacao tipo;
        private LocalDateTime data;
    }

    public enum TipoMovimentacao {
        CREDITO, DEBITO
    }
}