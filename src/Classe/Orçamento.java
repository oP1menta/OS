package Classe;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Orçamento {

    public enum Status {
        PENDENTE,
        APROVADO,
        REPROVADO,
        EXPIRADO
    }

    private int id;
    private List<ItemOrcamento> itens;
    private String tipoPagamento;
    private Tecnico tecnicoResponsavel;
    private BigDecimal Mão_de_obra;
    private Status status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataDecisao;
    private String observacoes;

    public Orçamento(List<ItemOrcamento> itens,
                     String tipoPagamento,
                     Tecnico tecnicoResponsavel,
                     BigDecimal Mão_de_obra) {

        if (itens == null || itens.isEmpty())
            throw new IllegalArgumentException("O orçamento deve ter ao menos uma peça");
        this.itens = new ArrayList<>(itens);
        this.setTipoPagamento(tipoPagamento.trim());
        this.setTecnicoResponsavel(tecnicoResponsavel);
        this.setMão_de_obra(Mão_de_obra);
        this.setStatus(Status.PENDENTE);
        this.setDataCriacao(LocalDateTime.now());
    }

    public void aprovar() {
        garantirEstadoPendente();
        this.setStatus(Status.APROVADO);
        this.setDataDecisao(LocalDateTime.now());
    }

    public void reprovar() {
        garantirEstadoPendente();
        this.setStatus(Status.REPROVADO);
        this.setDataDecisao(LocalDateTime.now());
    }

    public BigDecimal getValorTotalPecas() {
        return itens.stream()
                    .map(ItemOrcamento::getValor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void garantirEstadoPendente() {
        if (getStatus() != Status.PENDENTE) {
            throw new IllegalStateException(
                "Orçamento já foi decidido. Status atual: " + status
            );
        }
    }

    public boolean estaPendente()  { return status == Status.PENDENTE;  }
    public boolean estaAprovado()  { return status == Status.APROVADO;  }
    public boolean estaReprovado() { return status == Status.REPROVADO; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public List<ItemOrcamento> getItens() { return itens; }
    public void setItens(List<ItemOrcamento> itens) {
        if (itens == null || itens.isEmpty())
            throw new IllegalArgumentException("O orçamento deve ter ao menos uma peça");
        this.itens = new ArrayList<>(itens);
    }

    public String getTipoPagamento() { return tipoPagamento; }
    public void setTipoPagamento(String tipoPagamento) {
        if (tipoPagamento == null || tipoPagamento.isBlank())
            throw new IllegalArgumentException("Tipo de pagamento é obrigatório");
        this.tipoPagamento = tipoPagamento;
    }

    public Tecnico getTecnicoResponsavel() { return tecnicoResponsavel; }
    public void setTecnicoResponsavel(Tecnico tecnicoResponsavel) {
        if (tecnicoResponsavel == null)
            throw new IllegalArgumentException("Técnico responsável é obrigatório");
        this.tecnicoResponsavel = tecnicoResponsavel;
    }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataDecisao() { return dataDecisao; }
    public void setDataDecisao(LocalDateTime dataDecisao) { this.dataDecisao = dataDecisao; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public BigDecimal getMão_de_obra() { return Mão_de_obra; }
    public void setMão_de_obra(BigDecimal mão_de_obra) { Mão_de_obra = mão_de_obra; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Orçamento)) return false;
        Orçamento orcamento = (Orçamento) o;
        return Objects.equals(id, orcamento.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Técnico: " + getTecnicoResponsavel()
            + " | Peças: " + itens.size()
            + " | Total peças: R$" + getValorTotalPecas()
            + " | " + getStatus();
    }
}