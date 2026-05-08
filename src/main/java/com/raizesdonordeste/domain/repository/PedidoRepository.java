package com.raizesdonordeste.domain.repository;

import com.raizesdonordeste.domain.model.Pedido;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PedidoRepository extends MongoRepository<Pedido, String> {
    List<Pedido> findByClienteId(String clienteId);
    List<Pedido> findByCanalPedido(Pedido.CanalPedido canalPedido);
    List<Pedido> findByStatus(Pedido.StatusPedido status);
    List<Pedido> findByCanalPedidoAndStatus(Pedido.CanalPedido canalPedido, Pedido.StatusPedido status);
    List<Pedido> findByUnidadeId(String unidadeId);
}