package com.raizesdonordeste.domain.repository;

import com.raizesdonordeste.domain.model.Unidade;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UnidadeRepository extends MongoRepository<Unidade, String> {
    List<Unidade> findByAtivaTrue();
}