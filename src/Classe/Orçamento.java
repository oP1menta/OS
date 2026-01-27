package Classe;

import java.math.BigDecimal;

public class Orçamento {

    public enum StatusOrcamento {
        PENDENTE,
        APROVADO,
        REPROVADO
    }

    private Long idOrcamento;
    private String peca;
    private BigDecimal valor;
    private String tipoPagamento;
    private StatusOrcamento status;
    private OrdemDeServico ordemDeServico;

   
    public Orçamento(String peca, BigDecimal valor,
                     String tipoPagamento, OrdemDeServico ordemDeServico) {

        this.setPeca (peca);
        this.setValor(valor);
        this.setTipoPagamento ( tipoPagamento);
        this.setOrdemDeServico(ordemDeServico);
        this.setStatus ( StatusOrcamento.PENDENTE);
    }

    
    public Orçamento(Long idOrcamento, String peca, BigDecimal valor,
                     String tipoPagamento, StatusOrcamento status,
                     OrdemDeServico ordemDeServico) {

        this.idOrcamento = idOrcamento;
        this.peca = peca;
        this.valor = valor;
        this.tipoPagamento = tipoPagamento;
        this.status = status;
        this.ordemDeServico = ordemDeServico;
    }

    public boolean estaAprovado() {
        return status == StatusOrcamento.APROVADO;
    }

    public void aprovar() {
        this.status = StatusOrcamento.APROVADO;
    }

    public void reprovar() {
        this.status = StatusOrcamento.REPROVADO;
    }

  
    // Getters
    public Long getIdOrcamento() { return idOrcamento; }
    public String getPeca() { return peca; }
    public BigDecimal getValor() { return valor; }
    public String getTipoPagamento() { return tipoPagamento; }
    public StatusOrcamento getStatus() { return status; }
    public OrdemDeServico getOrdemDeServico() { return ordemDeServico; }

	public void setIdOrcamento(Long idOrcamento) {
		this.idOrcamento = idOrcamento;
	}

	public void setPeca(String peca) {
		this.peca = peca;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public void setTipoPagamento(String tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public void setStatus(StatusOrcamento status) {
		this.status = status;
	}

	public void setOrdemDeServico(OrdemDeServico ordemDeServico) {
		this.ordemDeServico = ordemDeServico;
	}
}