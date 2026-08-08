package br.com.pi.estoque.integration;

import br.com.pi.estoque.service.EstoqueService;
import br.com.pi.estoque.service.ProdutoService;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WebMvcIntegrationTest {
    @Autowired MockMvc mockMvc;
    @Autowired ProdutoService produtoService;
    @Autowired EstoqueService estoqueService;

    @BeforeEach
    void preparar() {
        if (produtoService.listarTodos().stream().noneMatch(p -> p.getSku().equals("WEB-001"))) {
            produtoService.cadastrar("WEB-001", "Produto Web", new BigDecimal("25.00"));
        }
    }

    @Test
    @DisplayName("Página de produtos deve exibir item persistido")
    void deveExibirProdutoNaPagina() throws Exception {
        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("WEB-001")))
                .andExpect(content().string(containsString("Produto Web")));
    }

    @Test
    @DisplayName("Formulário web deve registrar entrada e redirecionar")
    void deveRegistrarEntradaPelaWeb() throws Exception {
        mockMvc.perform(post("/movimentacoes")
                        .param("sku", "WEB-001")
                        .param("tipo", "ENTRADA")
                        .param("quantidade", "4"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/movimentacoes"));

        mockMvc.perform(get("/produto/WEB-001"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(">4</strong>")));
    }

    @Test
    @DisplayName("API deve retornar produto existente")
    void deveRetornarProdutoNaApi() throws Exception {
        mockMvc.perform(get("/api/produtos/WEB-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("WEB-001"))
                .andExpect(jsonPath("$.nome").value("Produto Web"));
    }

    @Test
    @DisplayName("API deve preservar regra de estoque insuficiente")
    void deveRejeitarSaidaAcimaDoSaldoNaApi() throws Exception {
        mockMvc.perform(post("/api/movimentacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sku\":\"WEB-001\",\"tipo\":\"SAIDA\",\"quantidade\":100}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value(containsString("Estoque insuficiente")));
    }

    @Test
    @DisplayName("Filtro vazio do histórico deve listar todas as movimentações")
    void deveListarHistoricoQuandoFiltroSkuVazio() throws Exception {
        var produto = produtoService.buscarPorSku("WEB-001");
        estoqueService.registrarEntrada(produto, 2);
        mockMvc.perform(get("/historico").param("sku", ""))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("WEB-001")));
    }
}
