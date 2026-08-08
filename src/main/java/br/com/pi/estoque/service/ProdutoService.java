package br.com.pi.estoque.service;

import br.com.pi.estoque.exception.ProdutoNaoEncontradoException;
import br.com.pi.estoque.exception.RegraNegocioException;
import br.com.pi.estoque.model.Produto;
import br.com.pi.estoque.repository.ProdutoRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public Produto cadastrar(String sku, String nome, BigDecimal preco) {
        String skuNormalizado = normalizarSku(sku);
        String nomeNormalizado = validarNome(nome);
        validarPreco(preco);

        if (produtoRepository.existePorSku(skuNormalizado)) {
            throw new RegraNegocioException("Já existe produto cadastrado com o SKU " + skuNormalizado + ".");
        }

        Produto produto = new Produto(skuNormalizado, nomeNormalizado, preco);
        produtoRepository.salvar(produto);
        return produto;
    }

    @Transactional(readOnly = true)
    public Produto buscarPorSku(String sku) {
        String skuNormalizado = normalizarSku(sku);
        return produtoRepository.buscarPorSku(skuNormalizado)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(skuNormalizado));
    }

    @Transactional(readOnly = true)
    public List<Produto> listarTodos() {
        return produtoRepository.listarTodos();
    }

    @Transactional
    public void alterarPreco(String sku, BigDecimal novoPreco) {
        validarPreco(novoPreco);
        Produto produto = buscarPorSku(sku);
        produto.alterarPreco(novoPreco);
        produtoRepository.salvar(produto);
    }

    @Transactional
    public void remover(String sku) {
        Produto produto = buscarPorSku(sku);
        if (produto.getQuantidadeEmEstoque() > 0) {
            throw new RegraNegocioException("Não é permitido remover produto com saldo em estoque.");
        }
        produtoRepository.removerPorSku(produto.getSku());
    }

    private String normalizarSku(String sku) {
        if (sku == null || sku.isBlank()) {
            throw new RegraNegocioException("O SKU é obrigatório.");
        }
        return sku.trim().toUpperCase();
    }

    private String validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new RegraNegocioException("O nome do produto é obrigatório.");
        }
        return nome.trim();
    }

    private void validarPreco(BigDecimal preco) {
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraNegocioException("O preço deve ser maior que zero.");
        }
    }
}
