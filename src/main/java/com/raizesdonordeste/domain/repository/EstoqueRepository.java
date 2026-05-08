package com.raizesdonordeste.domain.repository;

import com.raizesdonordeste.domain.model.Estoque;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.List;

public interface EstoqueRepository extends MongoRepository<Estoque, String> {
    Optional<Estoque> findByProdutoIdAndUnidadeId(String produtoId, String unidadeId);
    List<Estoque> findByUnidadeId(String unidadeId);
}