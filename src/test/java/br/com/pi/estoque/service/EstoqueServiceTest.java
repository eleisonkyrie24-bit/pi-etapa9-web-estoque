package br.com.pi.estoque.service;

import br.com.pi.estoque.model.Produto;
import br.com.pi.estoque.repository.memory.InMemoryMovimentacaoRepository;
import br.com.pi.estoque.repository.memory.InMemoryProdutoRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EstoqueServiceTest {
    private InMemoryProdutoRepository produtoRepository;
    private InMemoryMovimentacaoRepository movimentacaoRepository;
    private ProdutoService produtoService;
    private EstoqueService estoqueService;

    @BeforeEach
    void configurar() {
        produtoRepository = new InMemoryProdutoRepository();
        movimentacaoRepository = new InMemoryMovimentacaoRepository();
        produtoService = new ProdutoService(produtoRepository);
        estoqueService = new EstoqueService(produtoRepository, movimentacaoRepository);
    }

    @Test
    @DisplayName("Deve registrar entrada e saída e manter o saldo correto")
    void deveRegistrarEntradaESaida() {
        Produto produto = produtoService.cadastrar("TEC-001", "Teclado", new BigDecimal("250.00"));
        estoqueService.registrarEntrada(produto, 10);
        estoqueService.registrarSaida(produto, 3);
        assertEquals(7, produto.getQuantidadeEmEstoque());
        assertEquals(2, estoqueService.listarHistorico().size());
    }

    @Test
    @DisplayName("Deve calcular o valor financeiro total do estoque")
    void deveCalcularValorTotalDoEstoque() {
        Produto teclado = produtoService.cadastrar("TEC-001", "Teclado", new BigDecimal("250.00"));
        Produto mouse = produtoService.cadastrar("MOU-001", "Mouse", new BigDecimal("100.00"));
        estoqueService.registrarEntrada(teclado, 7);
        estoqueService.registrarEntrada(mouse, 2);
        BigDecimal total = estoqueService.calcularValorTotalEstoque();
        assertEquals(new BigDecimal("1950.00"), total);
    }
}
