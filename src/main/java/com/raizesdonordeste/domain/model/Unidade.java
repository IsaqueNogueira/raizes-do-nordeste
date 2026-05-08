package com.raizesdonordeste.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "unidades")
public class Unidade {

    @Id
    private String id;

    private String nome;

    private String endereco;

    private String cidade;

    private String estado;

    private String telefone;

    private boolean ativa;
}