package br.com.pi.estoque.web;

import br.com.pi.estoque.exception.RegraNegocioException;
import br.com.pi.estoque.model.TipoMovimentacao;
import br.com.pi.estoque.service.EstoqueService;
import br.com.pi.estoque.service.ProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MovimentacaoController {
    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;

    public MovimentacaoController(ProdutoService produtoService, EstoqueService estoqueService) {
        this.produtoService = produtoService;
        this.estoqueService = estoqueService;
    }

    @GetMapping("/movimentacoes")
    public String pagina(Model model) {
        model.addAttribute("produtos", produtoService.listarTodos());
        return "movimentacoes";
    }

    @PostMapping("/movimentacoes")
    public String registrar(@RequestParam String sku,
                            @RequestParam TipoMovimentacao tipo,
                            @RequestParam int quantidade,
                            RedirectAttributes redirect) {
        try {
            var produto = produtoService.buscarPorSku(sku);
            if (tipo == TipoMovimentacao.ENTRADA) {
                estoqueService.registrarEntrada(produto, quantidade);
            } else {
                estoqueService.registrarSaida(produto, quantidade);
            }
            redirect.addFlashAttribute("sucesso",
                    tipo + " de " + quantidade + " unidade(s) registrada para " + produto.getSku() + ".");
        } catch (RegraNegocioException ex) {
            redirect.addFlashAttribute("erro", ex.getMessage());
        }
        return "redirect:/movimentacoes";
    }
}
