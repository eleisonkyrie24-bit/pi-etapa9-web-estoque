package br.com.pi.estoque.repository.memory;

import br.com.pi.estoque.model.MovimentacaoEstoque;
import br.com.pi.estoque.repository.MovimentacaoRepository;
import java.util.ArrayList;
import java.util.List;

public class InMemoryMovimentacaoRepository implements MovimentacaoRepository {
    private final List<MovimentacaoEstoque> movimentacoes = new ArrayList<>();

    @Override
    public void salvar(MovimentacaoEstoque movimentacao) {
        movimentacoes.add(movimentacao);
    }

    @Override
    public List<MovimentacaoEstoque> listarTodas() {
        return List.copyOf(movimentacoes);
    }

    @Override
    public List<MovimentacaoEstoque> listarPorSku(String sku) {
        return movimentacoes.stream()
                .filter(m -> m.sku().equals(sku))
                .toList();
    }
}
