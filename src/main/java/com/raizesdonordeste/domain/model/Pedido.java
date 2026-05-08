package com.raizesdonordeste.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "pedidos")
public class Pedido {

    @Id
    private String id;

    private String clienteId;

    private String unidadeId;

    private CanalPedido canalPedido;

    private List<ItemPedido> itens;

    private BigDecimal total;

    private StatusPedido status;

    private String formaPagamento;

    private LocalDateTime dataCriacao;

    private LocalDateTime dataAtualizacao;

    public enum CanalPedido {
        APP, TOTEM, BALCAO, PICKUP, WEB
    }

    public enum StatusPedido {
        AGUARDANDO_PAGAMENTO,
        PAGAMENTO_APROVADO,
        EM_PREPARO,
        PRONTO,
        ENTREGUE,
        CANCELADO
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemPedido {
        private String produtoId;
        private String nomeProduto;
        private int quantidade;
        private BigDecimal precoUnitario;
        private BigDecimal subtotal;
    }
}