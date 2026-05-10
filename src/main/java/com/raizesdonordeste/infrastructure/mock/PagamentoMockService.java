package com.raizesdonordeste.infrastructure.mock;

import com.raizesdonordeste.domain.model.Pagamento;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class PagamentoMockService {

    private static final BigDecimal LIMITE_PIX = new BigDecimal("2000");
    private static final BigDecimal LIMITE_CARTAO = new BigDecimal("1000");
    private static final BigDecimal LIMITE_DINHEIRO = new BigDecimal("500");

    public Pagamento processarPagamento(String pedidoId, BigDecimal valor, String formaPagamento) {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(UUID.randomUUID().toString());
        pagamento.setPedidoId(pedidoId);
        pagamento.setValor(valor);
        pagamento.setFormaPagamento(formaPagamento);
        pagamento.setDataSolicitacao(LocalDateTime.now());
        pagamento.setDataRetorno(LocalDateTime.now());

        String forma = formaPagamento != null ? formaPagamento.toUpperCase() : "MOCK";

        switch (forma) {
            case "PIX":
                processarPix(pagamento, valor);
                break;
            case "CARTAO":
            case "CARTAO_CREDITO":
            case "CARTAO_DEBITO":
                processarCartao(pagamento, valor);
                break;
            case "DINHEIRO":
                processarDinheiro(pagamento, valor);
                break;
            default:
                processarMock(pagamento, valor);
                break;
        }

        return pagamento;
    }

    private void processarPix(Pagamento pagamento, BigDecimal valor) {
        if (valor.compareTo(LIMITE_PIX) > 0) {
            pagamento.setStatus(Pagamento.StatusPagamento.RECUSADO);
            pagamento.setMensagemRetorno("PIX recusado: valor R$ " + valor + " acima do limite de R$ " + LIMITE_PIX);
        } else {
            pagamento.setStatus(Pagamento.StatusPagamento.APROVADO);
            pagamento.setMensagemRetorno("PIX aprovado com sucesso");
        }
    }

    private void processarCartao(Pagamento pagamento, BigDecimal valor) {
        if (valor.compareTo(LIMITE_CARTAO) > 0) {
            pagamento.setStatus(Pagamento.StatusPagamento.RECUSADO);
            pagamento.setMensagemRetorno("Cartão recusado: valor R$ " + valor + " acima do limite de R$ " + LIMITE_CARTAO);
        } else {
            boolean recusadoPorSaldo = new Random().nextInt(10) == 0;
            if (recusadoPorSaldo) {
                pagamento.setStatus(Pagamento.StatusPagamento.RECUSADO);
                pagamento.setMensagemRetorno("Cartão recusado: saldo insuficiente");
            } else {
                pagamento.setStatus(Pagamento.StatusPagamento.APROVADO);
                pagamento.setMensagemRetorno("Cartão aprovado com sucesso");
            }
        }
    }

    private void processarDinheiro(Pagamento pagamento, BigDecimal valor) {
        if (valor.compareTo(LIMITE_DINHEIRO) > 0) {
            pagamento.setStatus(Pagamento.StatusPagamento.RECUSADO);
            pagamento.setMensagemRetorno("Pagamento em dinheiro recusado: valor acima de R$ " + LIMITE_DINHEIRO);
        } else {
            pagamento.setStatus(Pagamento.StatusPagamento.APROVADO);
            pagamento.setMensagemRetorno("Pagamento em dinheiro registrado com sucesso");
        }
    }

    private void processarMock(Pagamento pagamento, BigDecimal valor) {
        if (valor.compareTo(new BigDecimal("1000")) > 0) {
            pagamento.setStatus(Pagamento.StatusPagamento.RECUSADO);
            pagamento.setMensagemRetorno("Pagamento recusado: valor acima do limite permitido");
        } else {
            pagamento.setStatus(Pagamento.StatusPagamento.APROVADO);
            pagamento.setMensagemRetorno("Pagamento aprovado com sucesso");
        }
    }
}