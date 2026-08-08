package br.com.pi.estoque.repository.jpa;

import br.com.pi.estoque.model.MovimentacaoEstoque;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataMovimentacaoRepository extends JpaRepository<MovimentacaoEstoque, Long> {
    List<MovimentacaoEstoque> findBySkuOrderByDataHoraDesc(String sku);
    List<MovimentacaoEstoque> findAllByOrderByDataHoraDesc();
}
