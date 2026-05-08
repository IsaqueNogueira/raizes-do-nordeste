package com.raizesdonordeste.domain.repository;

import com.raizesdonordeste.domain.model.Produto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProdutoRepository extends MongoRepository<Produto, String> {
    List<Produto> findByUnidadeId(String unidadeId);
    List<Produto> findByUnidadeIdAndDisponivelTrue(String unidadeId);
}