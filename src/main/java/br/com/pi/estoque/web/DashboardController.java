package br.com.pi.estoque.web;

import br.com.pi.estoque.service.EstoqueService;
import br.com.pi.estoque.service.ProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;

    public DashboardController(ProdutoService produtoService, EstoqueService estoqueService) {
        this.produtoService = produtoService;
        this.estoqueService = estoqueService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        var produtos = produtoService.listarTodos();
        var historico = estoqueService.listarHistorico();
        model.addAttribute("produtos", produtos);
        model.addAttribute("quantidadeProdutos", produtos.size());
        model.addAttribute("unidadesEmEstoque", produtos.stream()
                .mapToInt(p -> p.getQuantidadeEmEstoque()).sum());
        model.addAttribute("valorTotal", estoqueService.calcularValorTotalEstoque());
        model.addAttribute("movimentacoesRecentes", historico.stream().limit(5).toList());
        return "index";
    }
}
