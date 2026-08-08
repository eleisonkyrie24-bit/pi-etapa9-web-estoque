package br.com.pi.estoque.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacoes_estoque", indexes = {
        @Index(name = "idx_movimentacao_sku", columnList = "sku")
})
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String sku;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoMovimentacao tipo;

    @Column(nullable = false)
    private int quantidade;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    protected MovimentacaoEstoque() {
        // Construtor exigido pelo JPA.
    }

    public MovimentacaoEstoque(String sku, TipoMovimentacao tipo, int quantidade, LocalDateTime dataHora) {
        this.sku = sku;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.dataHora = dataHora;
    }

    public Long getId() { return id; }
    public String getSku() { return sku; }
    public TipoMovimentacao getTipo() { return tipo; }
    public int getQuantidade() { return quantidade; }
    public LocalDateTime getDataHora() { return dataHora; }

    // Mantêm a forma de acesso usada na Etapa 7, quando a classe era record.
    public String sku() { return sku; }
    public TipoMovimentacao tipo() { return tipo; }
    public int quantidade() { return quantidade; }
    public LocalDateTime dataHora() { return dataHora; }
}
