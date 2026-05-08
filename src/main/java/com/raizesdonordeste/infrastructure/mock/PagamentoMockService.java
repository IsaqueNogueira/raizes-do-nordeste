package com.raizesdonordeste.infrastructure.mock;

import com.raizesdonordeste.domain.model.Pagamento;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PagamentoMockService {

    public Pagamento processarPagamento(String pedidoId, BigDecimal valor, String formaPagamento) {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(UUID.randomUUID().toString());
        pagamento.setPedidoId(pedidoId);
        pagamento.setValor(valor);
        pagamento.setFormaPagamento(formaPagamento);
        pagamento.setDataSolicitacao(LocalDateTime.now());
        pagamento.setDataRetorno(LocalDateTime.now());

        // Simula aprovacao ou recusa baseado no valor
        // Valores acima de 1000 sao recusados para simular falha
        if (valor.compareTo(new BigDecimal("1000")) > 0) {
            pagamento.setStatus(Pagamento.StatusPagamento.RECUSADO);
            pagamento.setMensagemRetorno("Pagamento recusado: valor acima do limite permitido");
        } else {
            pagamento.setStatus(Pagamento.StatusPagamento.APROVADO);
            pagamento.setMensagemRetorno("Pagamento aprovado com sucesso");
        }

        return pagamento;
    }
}