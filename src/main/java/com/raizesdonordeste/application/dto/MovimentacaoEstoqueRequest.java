package com.raizesdonordeste.application.dto;

import com.raizesdonordeste.domain.model.Estoque;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MovimentacaoEstoqueRequest {

    @NotBlank(message = "ProdutoId é obrigatório")
    private String produtoId;

    @NotBlank(message = "UnidadeId é obrigatório")
    private String unidadeId;

    @NotNull(message = "Tipo é obrigatório")
    private Estoque.TipoMovimentacao tipo;

    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    private int quantidade;
}