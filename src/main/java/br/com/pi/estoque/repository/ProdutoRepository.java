package br.com.pi.estoque.repository;

import br.com.pi.estoque.model.Produto;
import java.util.List;
import java.util.Optional;

public interface ProdutoRepository {
    void salvar(Produto produto);
    Optional<Produto> buscarPorSku(String sku);
    List<Produto> listarTodos();
    boolean existePorSku(String sku);
    void removerPorSku(String sku);
}
