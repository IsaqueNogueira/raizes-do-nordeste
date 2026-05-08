package com.raizesdonordeste.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "produtos")
public class Produto {

    @Id
    private String id;

    private String nome;

    private String descricao;

    private BigDecimal preco;

    private String categoria;

    private String unidadeId;

    private boolean disponivel;
}