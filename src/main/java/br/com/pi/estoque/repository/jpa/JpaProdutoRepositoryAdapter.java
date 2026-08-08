package br.com.pi.estoque.repository.jpa;

import br.com.pi.estoque.model.Produto;
import br.com.pi.estoque.repository.ProdutoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class JpaProdutoRepositoryAdapter implements ProdutoRepository {
    private final SpringDataProdutoRepository repository;

    public JpaProdutoRepositoryAdapter(SpringDataProdutoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void salvar(Produto produto) {
        repository.save(produto);
    }

    @Override
    public Optional<Produto> buscarPorSku(String sku) {
        return repository.findById(sku);
    }

    @Override
    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    @Override
    public boolean existePorSku(String sku) {
        return repository.existsById(sku);
    }

    @Override
    public void removerPorSku(String sku) {
        repository.deleteById(sku);
    }
}
