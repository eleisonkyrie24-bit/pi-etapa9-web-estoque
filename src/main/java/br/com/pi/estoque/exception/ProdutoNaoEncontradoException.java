package br.com.pi.estoque.exception;

public class ProdutoNaoEncontradoException extends RegraNegocioException {
    public ProdutoNaoEncontradoException(String sku) {
        super("Produto não encontrado: " + sku + ".");
    }
}
