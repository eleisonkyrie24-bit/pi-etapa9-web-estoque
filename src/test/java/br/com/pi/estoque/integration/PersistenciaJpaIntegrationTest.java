package br.com.pi.estoque.integration;

import br.com.pi.estoque.service.EstoqueService;
import br.com.pi.estoque.service.ProdutoService;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class PersistenciaJpaIntegrationTest {
    @Autowired ProdutoService produtoService;
    @Autowired EstoqueService estoqueService;

    @Test
    @DisplayName("JPA deve persistir produto e recuperar pelo SKU")
    void devePersistirProduto() {
        produtoService.cadastrar("jpa-001", "Produto JPA", new BigDecimal("99.90"));
        var recuperado = produtoService.buscarPorSku("JPA-001");
        assertEquals("Produto JPA", recuperado.getNome());
        assertEquals(new BigDecimal("99.90"), recuperado.getPreco());
    }

    @Test
    @DisplayName("JPA deve persistir movimentação e saldo")
    void devePersistirMovimentacaoESaldo() {
        var produto = produtoService.cadastrar("jpa-002", "Produto movimento", new BigDecimal("10.00"));
        estoqueService.registrarEntrada(produto, 12);
        var recuperado = produtoService.buscarPorSku("JPA-002");
        assertEquals(12, recuperado.getQuantidadeEmEstoque());
        assertEquals(1, estoqueService.listarHistoricoPorSku("JPA-002").size());
    }
}
