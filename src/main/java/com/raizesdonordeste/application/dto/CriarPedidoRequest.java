package com.raizesdonordeste.application.dto;

import com.raizesdonordeste.domain.model.Pedido;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CriarPedidoRequest {

    @NotBlank(message = "UnidadeId é obrigatório")
    private String unidadeId;

    @NotNull(message = "Canal do pedido é obrigatório")
    private Pedido.CanalPedido canalPedido;

    @NotEmpty(message = "Itens são obrigatórios")
    private List<ItemRequest> itens;

    @NotBlank(message = "Forma de pagamento é obrigatória")
    private String formaPagamento;

    @Data
    public static class ItemRequest {
        @NotBlank(message = "ProdutoId é obrigatório")
        private String produtoId;

        @NotNull(message = "Quantidade é obrigatória")
        private int quantidade;
    }
}