package br.com.pi.estoque.web;

import br.com.pi.estoque.exception.RegraNegocioException;
import br.com.pi.estoque.model.Produto;
import br.com.pi.estoque.service.EstoqueService;
import br.com.pi.estoque.service.ProdutoService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProdutoController {
    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;

    public ProdutoController(ProdutoService produtoService, EstoqueService estoqueService) {
        this.produtoService = produtoService;
        this.estoqueService = estoqueService;
    }

    @GetMapping("/produtos")
    public String listar(@RequestParam(required = false) String q, Model model) {
        List<Produto> produtos = produtoService.listarTodos();
        if (q != null && !q.isBlank()) {
            String termo = q.trim().toUpperCase();
            produtos = produtos.stream()
                    .filter(p -> p.getSku().contains(termo)
                            || p.getNome().toUpperCase().contains(termo))
                    .toList();
        }
        model.addAttribute("produtos", produtos);
        model.addAttribute("q", q == null ? "" : q);
        return "produtos";
    }

    @PostMapping("/produtos")
    public String cadastrar(@RequestParam String sku,
                            @RequestParam String nome,
                            @RequestParam BigDecimal preco,
                            RedirectAttributes redirect) {
        try {
            Produto produto = produtoService.cadastrar(sku, nome, preco);
            redirect.addFlashAttribute("sucesso", "Produto " + produto.getSku() + " cadastrado com sucesso.");
        } catch (RegraNegocioException ex) {
            redirect.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/produtos";
    }

    @GetMapping("/produto/{sku}")
    public String detalhe(@PathVariable String sku, Model model, RedirectAttributes redirect) {
        try {
            Produto produto = produtoService.buscarPorSku(sku);
            model.addAttribute("produto", produto);
            model.addAttribute("historico", estoqueService.listarHistoricoPorSku(sku));
            return "produto";
        } catch (RegraNegocioException ex) {
            redirect.addFlashAttribute("erro", ex.getMessage());
            return "redirect:/produtos";
        }
    }

    @PostMapping("/produto/{sku}/preco")
    public String alterarPreco(@PathVariable String sku,
                               @RequestParam BigDecimal preco,
                               RedirectAttributes redirect) {
        try {
            produtoService.alterarPreco(sku, preco);
            redirect.addFlashAttribute("sucesso", "Preço alterado com sucesso.");
        } catch (RegraNegocioException ex) {
            redirect.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/produto/" + sku;
    }

    @PostMapping("/produto/{sku}/excluir")
    public String excluir(@PathVariable String sku, RedirectAttributes redirect) {
        try {
            produtoService.remover(sku);
            redirect.addFlashAttribute("sucesso", "Produto removido com sucesso.");
            return "redirect:/produtos";
        } catch (RegraNegocioException ex) {
            redirect.addFlashAttribute("erro", ex.getMessage());
            return "redirect:/produto/" + sku;
        }
    }
}
