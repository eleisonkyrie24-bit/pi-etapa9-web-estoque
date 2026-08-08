package br.com.pi.estoque.repository.jpa;

import br.com.pi.estoque.model.MovimentacaoEstoque;
import br.com.pi.estoque.repository.MovimentacaoRepository;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class JpaMovimentacaoRepositoryAdapter implements MovimentacaoRepository {
    private final SpringDataMovimentacaoRepository repository;

    public JpaMovimentacaoRepositoryAdapter(SpringDataMovimentacaoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void salvar(MovimentacaoEstoque movimentacao) {
        repository.save(movimentacao);
    }

    @Override
    public List<MovimentacaoEstoque> listarTodas() {
        return repository.findAllByOrderByDataHoraDesc();
    }

    @Override
    public List<MovimentacaoEstoque> listarPorSku(String sku) {
        return repository.findBySkuOrderByDataHoraDesc(sku);
    }
}
