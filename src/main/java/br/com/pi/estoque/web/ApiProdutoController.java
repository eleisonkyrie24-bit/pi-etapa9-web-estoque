package br.com.pi.estoque.web;

import br.com.pi.estoque.exception.RegraNegocioException;
import br.com.pi.estoque.model.Produto;
import br.com.pi.estoque.model.TipoMovimentacao;
import br.com.pi.estoque.service.EstoqueService;
import br.com.pi.estoque.service.ProdutoService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiProdutoController {
    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;

    public ApiProdutoController(ProdutoService produtoService, EstoqueService estoqueService) {
        this.produtoService = produtoService;
        this.estoqueService = estoqueService;
    }

    @GetMapping("/produtos")
    public List<Produto> listar() {
        return produtoService.listarTodos();
    }

    @GetMapping("/produtos/{sku}")
    public ResponseEntity<?> buscar(@PathVariable String sku) {
        try {
            return ResponseEntity.ok(produtoService.buscarPorSku(sku));
        } catch (RegraNegocioException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", ex.getMessage()));
        }
    }

    @PostMapping("/movimentacoes")
    public ResponseEntity<?> movimentar(@RequestBody MovimentacaoRequest request) {
        try {
            var produto = produtoService.buscarPorSku(request.sku());
            if (request.tipo() == TipoMovimentacao.ENTRADA) {
                estoqueService.registrarEntrada(produto, request.quantidade());
            } else if (request.tipo() == TipoMovimentacao.SAIDA) {
                estoqueService.registrarSaida(produto, request.quantidade());
            } else {
                return ResponseEntity.badRequest().body(Map.of("erro", "Tipo de movimentação é obrigatório."));
            }
            return ResponseEntity.ok(produto);
        } catch (RegraNegocioException ex) {
            return ResponseEntity.badRequest().body(Map.of("erro", ex.getMessage()));
        }
    }

    public record MovimentacaoRequest(String sku, TipoMovimentacao tipo, int quantidade) {
    }
}
