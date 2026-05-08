package com.raizesdonordeste.domain.repository;

import com.raizesdonordeste.domain.model.Fidelidade;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FidelidadeRepository extends MongoRepository<Fidelidade, String> {
    Optional<Fidelidade> findByClienteId(String clienteId);
}