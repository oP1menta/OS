package Classe;

import dominio.enums.StatusOrdemServico;
import java.time.LocalDateTime;
import java.util.Objects;

public class OrdemDeServico {

    
    private Long id;
    private Equipamento equipamento;

    private LocalDateTime dataAbertura;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFechamentoPrevisto;
    private LocalDateTime dataFechamentoReal;

    private String descricaoProblema;
    private String observacoesTecnicas;

    private StatusOrdemServico status;
    private Orçamento orcamentoAprovado;

   
    public OrdemDeServico(Long id,Equipamento equipamento,
    		String descricaoProblema,LocalDateTime dataAbertura,
    		LocalDateTime dataFechamentoPrevisto,
    		LocalDateTime dataFechamentoReal,StatusOrdemServico status
    ) {

        this.id = id;
        this.equipamento = equipamento;
        this.descricaoProblema = descricaoProblema;
        this.dataAbertura = dataAbertura;
        this.dataFechamentoPrevisto = dataFechamentoPrevisto;
        this.dataFechamentoReal = dataFechamentoReal;
        this.status = status;
    }
    
    
    

   

    public void iniciar(Orçamento orcamento) {

        if (orcamento == null)
            throw new IllegalArgumentException("Orçamento obrigatório");

        if (!orcamento.estaAprovado())
            throw new IllegalStateException(
                "Não é possível iniciar OS sem orçamento aprovado"
            );

        if (status != StatusOrdemServico.PENDENTE)
            throw new IllegalStateException("A OS não está pendente");

        this.orcamentoAprovado = orcamento;
        this.status = StatusOrdemServico.EM_ANDAMENTO;
        this.dataInicio = LocalDateTime.now();
    }

    public void concluir(String observacoesTecnicas) {

        if (status != StatusOrdemServico.EM_ANDAMENTO)
            throw new IllegalStateException(
                "Apenas OS em andamento podem ser concluídas"
            );

        this.observacoesTecnicas = observacoesTecnicas;
        this.dataFechamentoReal = LocalDateTime.now();
        this.status = StatusOrdemServico.CONCLUIDA;
    }

    public boolean estaAtrasada() {
        return status == StatusOrdemServico.EM_ANDAMENTO &&
               LocalDateTime.now().isAfter(dataFechamentoPrevisto);
    }

    /* =========================
       VALIDAÇÕES INTERNAS
       ========================= */
    private void validarCriacao(Equipamento equipamento,
                                String descricaoProblema,
                                LocalDateTime dataFechamentoPrevisto) {

        if (equipamento == null)
            throw new IllegalArgumentException("Equipamento obrigatório");

        if (descricaoProblema == null || descricaoProblema.isBlank())
            throw new IllegalArgumentException("Descrição do problema obrigatória");

        if (dataFechamentoPrevisto == null)
            throw new IllegalArgumentException("Data prevista obrigatória");
    }

    
    public boolean estaPendente() {
        return status == StatusOrdemServico.PENDENTE;
    }

    public boolean estaEmAndamento() {
        return status == StatusOrdemServico.EM_ANDAMENTO;
    }

    public boolean estaConcluida() {
        return status == StatusOrdemServico.CONCLUIDA;
    }

   
    public Long getId() {
        return id;
    }

    public Equipamento getEquipamento() {
        return equipamento;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public LocalDateTime getDataFechamentoPrevisto() {
        return dataFechamentoPrevisto;
    }

    public LocalDateTime getDataFechamentoReal() {
        return dataFechamentoReal;
    }

    public String getDescricaoProblema() {
        return descricaoProblema;
    }

    public String getObservacoesTecnicas() {
        return observacoesTecnicas;
    }

    public StatusOrdemServico getStatus() {
        return status;
    }

    public Orçamento getOrcamentoAprovado() {
        return orcamentoAprovado;
    }

 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrdemDeServico)) return false;
        OrdemDeServico that = (OrdemDeServico) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

	public void setId(Long id) {
		this.id = id;
	}

	public void setEquipamento(Equipamento equipamento) {
		this.equipamento = equipamento;
	}

	public void setDataAbertura(LocalDateTime dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public void setDataInicio(LocalDateTime dataInicio) {
		this.dataInicio = dataInicio;
	}

	public void setDataFechamentoPrevisto(LocalDateTime dataFechamentoPrevisto) {
		this.dataFechamentoPrevisto = dataFechamentoPrevisto;
	}

	public void setDataFechamcentoReal(LocalDateTime dataFechamentoReal) {
		this.dataFechamentoReal = dataFechamentoReal;
	}

	public void setDescricaoProblema(String descricaoProblema) {
		this.descricaoProblema = descricaoProblema;
	}

	public void setObservacoesTecnicas(String observacoesTecnicas) {
		this.observacoesTecnicas = observacoesTecnicas;
	}

	public void setStatus(StatusOrdemServico status) {
		this.status = status;
	}

	public void setOrcamentoAprovado(Orçamento orcamentoAprovado) {
		this.orcamentoAprovado = orcamentoAprovado;
	}
}