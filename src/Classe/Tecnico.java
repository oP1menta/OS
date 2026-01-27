package Classe;

import java.time.LocalDate;

public class Tecnico {

    private int id;
    private String nome;
    private String documento;
    private String equipamento;
    private LocalDate dataAssociacao;

    public Tecnico(String nome, String documento, String equipamento, LocalDate dataAssociacao) {
        this.setNome(nome);
        this.setDocumento (documento);
        this.setEquipamento (equipamento);
        this.setDataAssociacao(dataAssociacao);
    }

    // Getters
    public int getId()  {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDocumento() {
        return documento;
    }

    public String getEquipamento() {
        return equipamento;
    }

    public LocalDate getDataAssociacao() {
        return dataAssociacao;
    }

    // Setter controlado
    public void setId(int id) {
        this.id = id;
    }

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public void setEquipamento(String equipamento) {
		this.equipamento = equipamento;
	}

	public void setDataAssociacao(LocalDate dataAssociacao) {
		this.dataAssociacao = dataAssociacao;
	}
}