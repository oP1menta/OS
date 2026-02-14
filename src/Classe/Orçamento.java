package Classe;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Orçamento {

   
    public enum Status {
        PENDENTE,
        APROVADO,
        REPROVADO,
        EXPIRADO
    }

    private int id;
    private String peca;
    private BigDecimal valor;
    private String tipoPagamento;
    private Tecnico tecnicoResponsavel;

    private Status status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataDecisao;
    private String observacoes;

    
    public Orçamento(String peca,
                     BigDecimal valor,
                     String tipoPagamento, Tecnico tecnicoResponsavel) {

      

        this.setPeca (peca.trim());
        this.setValor (valor);
        this.setTipoPagamento ( tipoPagamento.trim());
        this.setTecnicoResponsavel (tecnicoResponsavel);

        this.setStatus(Status.PENDENTE);
        this.setDataCriacao (LocalDateTime.now());
    }

    

    public void aprovar() {
        garantirEstadoPendente();
        this.setStatus (Status.APROVADO);
        this.setDataDecisao(LocalDateTime.now());
    }

    public void reprovar() {
        garantirEstadoPendente();
        this.setStatus (Status.REPROVADO);
        this.setDataDecisao (LocalDateTime.now());
    }

    public void expirar() {
        if (getStatus() == Status.APROVADO)
            throw new IllegalStateException("Orçamento aprovado não pode expirar");

        this.setStatus ( Status.EXPIRADO);
        this.setDataDecisao(LocalDateTime.now());
    }

   

    private void garantirEstadoPendente() {
        if (getStatus() != Status.PENDENTE) {
            throw new IllegalStateException(
                "Orçamento já foi decidido. Status atual: " + status
            );
        }
    }

    
       

        
    

    

    public boolean estaPendente() {
        return status == Status.PENDENTE;
    }

    public boolean estaAprovado() {
        return status == Status.APROVADO;
    }

    public boolean estaReprovado() {
        return status == Status.REPROVADO;
    }

  
    public int getId() {
        return id;
    }

    public String getPeca() {
        return peca;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public Tecnico getTecnicoResponsavel() {
        return tecnicoResponsavel;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataDecisao() {
        return dataDecisao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Orçamento)) return false;
        Orçamento orcamento = (Orçamento) o;
        return Objects.equals(id, orcamento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }



	public void setId(int id) {
		this.id = id;
	}



	public void setPeca(String peca) {
		 if (peca == null || peca.isBlank())
	            throw new IllegalArgumentException("Peça é obrigatória");
		 else
			 this.peca = peca;
	}



	public void setValor(BigDecimal valor) {
		 if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0)
	            throw new IllegalArgumentException("Valor deve ser maior que zero");
		 else
		this.valor = valor;
	}



	public void setTipoPagamento(String tipoPagamento) {
		if (tipoPagamento == null || tipoPagamento.isBlank())
            throw new IllegalArgumentException("Tipo de pagamento é obrigatório");
		else	
		this.tipoPagamento = tipoPagamento;
	}
	


	public void setTecnicoResponsavel(Tecnico tecnicoResponsavel) {
		if (tecnicoResponsavel == null)
	        throw new IllegalArgumentException("Técnico responsável é obrigatório");	
		else
		this.tecnicoResponsavel = tecnicoResponsavel;
	}



	public void setStatus(Status status) {
		this.status = status;
	}



	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}



	public void setDataDecisao(LocalDateTime dataDecisao) {
		this.dataDecisao = dataDecisao;
	}



	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}
	
	
	public String toString() {
		return "Tecnico Responsavel: " + getTecnicoResponsavel() + "\n Data Criação " + getDataCriacao() + 
				"\n Peça: " + getPeca() + " \nValor: $" + getValor();
	 } 
	
}