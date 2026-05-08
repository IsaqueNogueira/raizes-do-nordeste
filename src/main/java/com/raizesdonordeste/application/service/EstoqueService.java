package com.raizesdonordeste.application.service;

import com.raizesdonordeste.application.dto.MovimentacaoEstoqueRequest;
import com.raizesdonordeste.domain.model.Estoque;
import com.raizesdonordeste.domain.repository.EstoqueRepository;
import com.raizesdonordeste.domain.repository.ProdutoRepository;
import com.raizesdonordeste.domain.repository.UnidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UnidadeRepository unidadeRepository;

    public Estoque movimentar(MovimentacaoEstoqueRequest request) {
        produtoRepository.findById(request.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        unidadeRepository.findById(request.getUnidadeId())
                .orElseThrow(() -> new RuntimeException("Unidade não encontrada"));

        Estoque estoque = estoqueRepository
                .findByProdutoIdAndUnidadeId(request.getProdutoId(), request.getUnidadeId())
                .orElseGet(() -> {
                    Estoque novo = new Estoque();
                    novo.setProdutoId(request.getProdutoId());
                    novo.setUnidadeId(request.getUnidadeId());
                    novo.setQuantidade(0);
                    return novo;
                });

        if (request.getTipo() == Estoque.TipoMovimentacao.ENTRADA) {
            estoque.setQuantidade(estoque.getQuantidade() + request.getQuantidade());
        } else {
            if (estoque.getQuantidade() < request.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente");
            }
            estoque.setQuantidade(estoque.getQuantidade() - request.getQuantidade());
        }

        estoque.setUltimaAtualizacao(LocalDateTime.now());
        return estoqueRepository.save(estoque);
    }

    public List<Estoque> listarPorUnidade(String unidadeId) {
        return estoqueRepository.findByUnidadeId(unidadeId);
    }

    public Estoque buscarPorProdutoEUnidade(String produtoId, String unidadeId) {
        return estoqueRepository.findByProdutoIdAndUnidadeId(produtoId, unidadeId)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));
    }
}