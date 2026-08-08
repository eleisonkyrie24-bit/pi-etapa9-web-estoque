package br.com.pi.estoque.model;

import br.com.pi.estoque.exception.RegraNegocioException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @Column(length = 50, nullable = false, updatable = false)
    private String sku;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private int quantidadeEmEstoque;

    protected Produto() {
        // Construtor exigido pelo JPA.
    }

    public Produto(String sku, String nome, BigDecimal preco) {
        this.sku = Objects.requireNonNull(sku);
        this.nome = Objects.requireNonNull(nome);
        this.preco = Objects.requireNonNull(preco).setScale(2, RoundingMode.HALF_UP);
        this.quantidadeEmEstoque = 0;
    }

    public String getSku() {
        return sku;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public int getQuantidadeEmEstoque() {
        return quantidadeEmEstoque;
    }

    public void alterarPreco(BigDecimal novoPreco) {
        if (novoPreco == null || novoPreco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraNegocioException("O preço deve ser maior que zero.");
        }
        this.preco = novoPreco.setScale(2, RoundingMode.HALF_UP);
    }

    public void adicionarEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new RegraNegocioException("A quantidade de entrada deve ser positiva.");
        }
        quantidadeEmEstoque += quantidade;
    }

    public void removerEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new RegraNegocioException("A quantidade de saída deve ser positiva.");
        }
        if (quantidade > quantidadeEmEstoque) {
            throw new RegraNegocioException("Estoque insuficiente para a saída solicitada.");
        }
        quantidadeEmEstoque -= quantidade;
    }

    public BigDecimal calcularValorEmEstoque() {
        return preco.multiply(BigDecimal.valueOf(quantidadeEmEstoque))
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return "Produto{" +
                "sku='" + sku + '\'' +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", quantidadeEmEstoque=" + quantidadeEmEstoque +
                '}';
    }
}
