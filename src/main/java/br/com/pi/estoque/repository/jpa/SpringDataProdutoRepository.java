package br.com.pi.estoque.repository.jpa;

import br.com.pi.estoque.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataProdutoRepository extends JpaRepository<Produto, String> {
}
