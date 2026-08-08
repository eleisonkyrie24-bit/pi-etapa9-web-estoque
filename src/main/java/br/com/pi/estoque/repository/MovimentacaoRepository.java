package br.com.pi.estoque.repository;

import br.com.pi.estoque.model.MovimentacaoEstoque;
import java.util.List;

public interface MovimentacaoRepository {
    void salvar(MovimentacaoEstoque movimentacao);
    List<MovimentacaoEstoque> listarTodas();
    List<MovimentacaoEstoque> listarPorSku(String sku);
}
