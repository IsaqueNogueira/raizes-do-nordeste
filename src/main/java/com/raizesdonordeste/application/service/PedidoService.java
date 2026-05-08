package com.raizesdonordeste.application.service;

import com.raizesdonordeste.application.dto.CriarPedidoRequest;
import com.raizesdonordeste.domain.model.*;
import com.raizesdonordeste.domain.repository.*;
import com.raizesdonordeste.infrastructure.mock.PagamentoMockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FidelidadeRepository fidelidadeRepository;

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Autowired
    private PagamentoMockService pagamentoMockService;

    public Pedido criarPedido(CriarPedidoRequest request, String emailCliente) {
        unidadeRepository.findById(request.getUnidadeId())
                .orElseThrow(() -> new RuntimeException("Unidade não encontrada"));

        Usuario cliente = usuarioRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        String clienteId = cliente.getId();

        List<Pedido.ItemPedido> itens = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CriarPedidoRequest.ItemRequest itemRequest : request.getItens()) {
            Produto produto = produtoRepository.findById(itemRequest.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + itemRequest.getProdutoId()));

            if (!produto.isDisponivel()) {
                throw new RuntimeException("Produto indisponível: " + produto.getNome());
            }

            Estoque estoque = estoqueRepository
                    .findByProdutoIdAndUnidadeId(itemRequest.getProdutoId(), request.getUnidadeId())
                    .orElseThrow(() -> new RuntimeException("Estoque não encontrado para o produto: " + produto.getNome()));

            if (estoque.getQuantidade() < itemRequest.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome()
                        + ". Disponível: " + estoque.getQuantidade());
            }

            BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(itemRequest.getQuantidade()));
            total = total.add(subtotal);

            Pedido.ItemPedido item = new Pedido.ItemPedido(
                    produto.getId(),
                    produto.getNome(),
                    itemRequest.getQuantidade(),
                    produto.getPreco(),
                    subtotal
            );
            itens.add(item);
        }

        Pedido pedido = new Pedido();
        pedido.setClienteId(clienteId);
        pedido.setUnidadeId(request.getUnidadeId());
        pedido.setCanalPedido(request.getCanalPedido());
        pedido.setItens(itens);
        pedido.setTotal(total);
        pedido.setFormaPagamento(request.getFormaPagamento());
        pedido.setStatus(Pedido.StatusPedido.AGUARDANDO_PAGAMENTO);
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setDataAtualizacao(LocalDateTime.now());

        Pedido salvo = pedidoRepository.save(pedido);

        for (CriarPedidoRequest.ItemRequest itemRequest : request.getItens()) {
            Estoque estoqueAtual = estoqueRepository
                    .findByProdutoIdAndUnidadeId(itemRequest.getProdutoId(), request.getUnidadeId())
                    .orElse(null);

            if (estoqueAtual != null) {
                estoqueAtual.setQuantidade(estoqueAtual.getQuantidade() - itemRequest.getQuantidade());
                estoqueAtual.setUltimaAtualizacao(LocalDateTime.now());
                estoqueRepository.save(estoqueAtual);
            }
        }

        Pagamento pagamento = pagamentoMockService.processarPagamento(
                salvo.getId(), total, request.getFormaPagamento());
        pagamentoRepository.save(pagamento);

        if (pagamento.getStatus() == Pagamento.StatusPagamento.APROVADO) {
            salvo.setStatus(Pedido.StatusPedido.PAGAMENTO_APROVADO);
        } else {
            salvo.setStatus(Pedido.StatusPedido.CANCELADO);
        }

        salvo.setDataAtualizacao(LocalDateTime.now());
        pedidoRepository.save(salvo);

       if (pagamento.getStatus() == Pagamento.StatusPagamento.APROVADO) {
            final BigDecimal totalFinal = total;
            fidelidadeRepository.findByClienteId(clienteId).ifPresent(fidelidade -> {
                int pontos = totalFinal.intValue();
                fidelidade.setPontos(fidelidade.getPontos() + pontos);
                Fidelidade.HistoricoPontos historico = new Fidelidade.HistoricoPontos(
                        "Pedido " + salvo.getId(),
                        pontos,
                        Fidelidade.TipoMovimentacao.CREDITO,
                        LocalDateTime.now()
                );
                fidelidade.getHistorico().add(historico);
                fidelidadeRepository.save(fidelidade);
            });
        }

        return salvo;
    }

    public Pedido atualizarStatus(String pedidoId, Pedido.StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pedido.setStatus(novoStatus);
        pedido.setDataAtualizacao(LocalDateTime.now());

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarPedidos(Pedido.CanalPedido canal, Pedido.StatusPedido status) {
        if (canal != null && status != null) {
            return pedidoRepository.findByCanalPedidoAndStatus(canal, status);
        } else if (canal != null) {
            return pedidoRepository.findByCanalPedido(canal);
        } else if (status != null) {
            return pedidoRepository.findByStatus(status);
        }
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(String id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }
}