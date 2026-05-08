package com.raizesdonordeste.domain.repository;

import com.raizesdonordeste.domain.model.Pagamento;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PagamentoRepository extends MongoRepository<Pagamento, String> {
    Optional<Pagamento> findByPedidoId(String pedidoId);
}