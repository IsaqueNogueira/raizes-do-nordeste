package com.raizesdonordeste.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "pagamentos")
public class Pagamento {

    @Id
    private String id;

    private String pedidoId;

    private BigDecimal valor;

    private String formaPagamento;

    private StatusPagamento status;

    private String mensagemRetorno;

    private LocalDateTime dataSolicitacao;

    private LocalDateTime dataRetorno;

    public enum StatusPagamento {
        PENDENTE,
        APROVADO,
        RECUSADO
    }
}