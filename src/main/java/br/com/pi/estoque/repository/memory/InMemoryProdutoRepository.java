package br.com.pi.estoque.repository.memory;

import br.com.pi.estoque.model.Produto;
import br.com.pi.estoque.repository.ProdutoRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryProdutoRepository implements ProdutoRepository {
    private final Map<String, Produto> produtos = new LinkedHashMap<>();

    @Override
    public void salvar(Produto produto) {
        produtos.put(produto.getSku(), produto);
    }

    @Override
    public Optional<Produto> buscarPorSku(String sku) {
        return Optional.ofNullable(produtos.get(sku));
    }

    @Override
    public List<Produto> listarTodos() {
        return new ArrayList<>(produtos.values());
    }

    @Override
    public boolean existePorSku(String sku) {
        return produtos.containsKey(sku);
    }

    @Override
    public void removerPorSku(String sku) {
        produtos.remove(sku);
    }
}
