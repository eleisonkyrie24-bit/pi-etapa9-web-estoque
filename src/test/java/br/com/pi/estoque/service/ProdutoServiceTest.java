package br.com.pi.estoque.service;

import br.com.pi.estoque.exception.RegraNegocioException;
import br.com.pi.estoque.model.Produto;
import br.com.pi.estoque.repository.memory.InMemoryProdutoRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProdutoServiceTest {
    private ProdutoService produtoService;

    @BeforeEach
    void configurar() {
        produtoService = new ProdutoService(new InMemoryProdutoRepository());
    }

    @Test
    @DisplayName("Deve normalizar o SKU ao cadastrar produto")
    void deveNormalizarSkuNoCadastro() {
        Produto produto = produtoService.cadastrar("  tec-001  ", "Teclado Mecânico", new BigDecimal("250.00"));
        assertEquals("TEC-001", produto.getSku());
    }

    @Test
    @DisplayName("Deve rejeitar cadastro de SKU duplicado")
    void deveRejeitarSkuDuplicado() {
        produtoService.cadastrar("TEC-001", "Teclado", new BigDecimal("250.00"));
        assertThrows(RegraNegocioException.class,
                () -> produtoService.cadastrar("tec-001", "Outro teclado", new BigDecimal("300.00")));
    }

    @Test
    @DisplayName("Deve rejeitar preço igual a zero")
    void deveRejeitarPrecoZero() {
        assertThrows(RegraNegocioException.class,
                () -> produtoService.cadastrar("TEC-001", "Teclado", BigDecimal.ZERO));
    }
}
