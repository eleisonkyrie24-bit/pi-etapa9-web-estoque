package br.com.pi.estoque.web;

import br.com.pi.estoque.service.EstoqueService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HistoricoController {
    private final EstoqueService estoqueService;

    public HistoricoController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @GetMapping("/historico")
    public String historico(@RequestParam(required = false) String sku, Model model) {
        model.addAttribute("movimentacoes", estoqueService.listarHistoricoPorSku(sku));
        model.addAttribute("sku", sku == null ? "" : sku);
        return "historico";
    }
}
