package Classe;

import java.math.BigDecimal;

public class ItemOrcamento {

    private int id;
    private String descricao;
    private BigDecimal valor;

    public ItemOrcamento(String descricao, BigDecimal valor) {
        if (descricao == null || descricao.isBlank())
            throw new IllegalArgumentException("Descrição da peça é obrigatória");
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Valor da peça deve ser maior que zero");
        this.descricao = descricao.trim();
        this.valor = valor;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    @Override
    public String toString() {
        return descricao + " — R$ " + valor;
    }
}