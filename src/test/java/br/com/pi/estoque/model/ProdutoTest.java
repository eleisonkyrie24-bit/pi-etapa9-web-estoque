package br.com.pi.estoque.model;

import br.com.pi.estoque.exception.RegraNegocioException;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProdutoTest {

    @Test
    @DisplayName("Deve calcular o valor do produto em estoque")
    void deveCalcularValorEmEstoque() {
        Produto produto = new Produto("TEC-001", "Teclado", new BigDecimal("250.00"));
        produto.adicionarEstoque(7);
        BigDecimal valor = produto.calcularValorEmEstoque();
        assertEquals(new BigDecimal("1750.00"), valor);
    }

    @Test
    @DisplayName("Deve somar uma entrada válida ao saldo")
    void deveAdicionarEstoque() {
        Produto produto = new Produto("MOU-001", "Mouse", new BigDecimal("80.00"));
        produto.adicionarEstoque(10);
        assertEquals(10, produto.getQuantidadeEmEstoque());
    }

    @Test
    @DisplayName("Deve rejeitar entrada com quantidade zero")
    void deveRejeitarEntradaComQuantidadeZero() {
        Produto produto = new Produto("MOU-001", "Mouse", new BigDecimal("80.00"));
        assertThrows(RegraNegocioException.class, () -> produto.adicionarEstoque(0));
    }

    @Test
    @DisplayName("Deve impedir saída superior ao estoque disponível")
    void deveImpedirSaidaSuperiorAoEstoque() {
        Produto produto = new Produto("TEC-001", "Teclado", new BigDecimal("250.00"));
        produto.adicionarEstoque(7);
        assertThrows(RegraNegocioException.class, () -> produto.removerEstoque(8));
        assertEquals(7, produto.getQuantidadeEmEstoque());
    }
}
