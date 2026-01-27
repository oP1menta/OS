package Classe;

import dominio.enums.StatusOrdemServico;
import java.time.LocalDateTime;

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

    

    public OrdemDeServico(Equipamento equipamento,
                          String descricaoProblema,
                          LocalDateTime dataFechamentoPrevisto) {

        if (equipamento == null)
            throw new IllegalArgumentException("Equipamento obrigatório");

        if (dataFechamentoPrevisto == null)
            throw new IllegalArgumentException("Data prevista obrigatória");

        this.equipamento = equipamento;
        this.descricaoProblema = descricaoProblema;
        this.dataFechamentoPrevisto = dataFechamentoPrevisto;

        this.dataAbertura = LocalDateTime.now();
        this.status = StatusOrdemServico.PENDENTE;
    }

    

    public void iniciar(Orçamento orcamento) {

        if (orcamento == null)
            throw new IllegalArgumentException("Orçamento obrigatório");

        if (!orcamento.estaAprovado())
            throw new IllegalStateException(
                "Não é possível iniciar OS sem orçamento aprovado"
            );

        if (orcamento.getOrdemDeServico() == null ||
            !orcamento.getOrdemDeServico().equals(this))
            throw new IllegalStateException(
                "Orçamento não pertence a esta Ordem de Serviço"
            );

        if (status != StatusOrdemServico.PENDENTE)
            throw new IllegalStateException("OS já iniciada");

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

    //  GETTERS E SETTERS 

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

    public void setId(Long id) {
        this.id = id;
    }
}