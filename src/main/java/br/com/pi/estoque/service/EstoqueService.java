package br.com.pi.estoque.service;

import br.com.pi.estoque.model.MovimentacaoEstoque;
import br.com.pi.estoque.model.Produto;
import br.com.pi.estoque.model.TipoMovimentacao;
import br.com.pi.estoque.repository.MovimentacaoRepository;
import br.com.pi.estoque.repository.ProdutoRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstoqueService {
    private final ProdutoRepository produtoRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    public EstoqueService(ProdutoRepository produtoRepository,
                          MovimentacaoRepository movimentacaoRepository) {
        this.produtoRepository = produtoRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    @Transactional
    public void registrarEntrada(Produto produto, int quantidade) {
        produto.adicionarEstoque(quantidade);
        produtoRepository.salvar(produto);
        registrarMovimentacao(produto.getSku(), TipoMovimentacao.ENTRADA, quantidade);
    }

    @Transactional
    public void registrarSaida(Produto produto, int quantidade) {
        produto.removerEstoque(quantidade);
        produtoRepository.salvar(produto);
        registrarMovimentacao(produto.getSku(), TipoMovimentacao.SAIDA, quantidade);
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoEstoque> listarHistorico() {
        return movimentacaoRepository.listarTodas();
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoEstoque> listarHistoricoPorSku(String sku) {
        if (sku == null || sku.isBlank()) {
            return listarHistorico();
        }
        return movimentacaoRepository.listarPorSku(sku.trim().toUpperCase());
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularValorTotalEstoque() {
        return produtoRepository.listarTodos().stream()
                .map(Produto::calcularValorEmEstoque)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void registrarMovimentacao(String sku, TipoMovimentacao tipo, int quantidade) {
        movimentacaoRepository.salvar(
                new MovimentacaoEstoque(sku, tipo, quantidade, LocalDateTime.now())
        );
    }
}
