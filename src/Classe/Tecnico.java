package Classe;

import java.time.LocalDate;

import Exception.InvalidArgumentException;

public class Tecnico {

    private int id;
    private String nome;
    private String documento;
    
    private LocalDate dataAssociacao;

    public Tecnico(String nome, String documento, LocalDate dataAssociacao) {
        try {
			this.setNome(nome);
		} catch (InvalidArgumentException e) {
			
			e.printStackTrace();
		}
        try {
			this.setDocumento (documento);
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
       
        this.setDataAssociacao(dataAssociacao);
    }

    // Getters
    public int getId()  {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDocumento()  {
        return documento;
    }

    public LocalDate getDataAssociacao() {
        return dataAssociacao;
    }

    // Setter controlado
    public void setId(int id) {
        this.id = id;
    }

	public void setNome(String nome) throws InvalidArgumentException {
	if(nome == null) {
		throw new InvalidArgumentException("O tecnico Precisa ter um nome");}
		else
			this.nome = nome;
	}

	public void setDocumento(String documento) throws InvalidArgumentException {
		if(documento == null) {
			throw new InvalidArgumentException("O tecnico Precisa documento");}
			else
		this.documento = documento;
	}


	public void setDataAssociacao(LocalDate dataAssociacao) {
		this.dataAssociacao = dataAssociacao;
	}
	
	public String toString() {
		return "Nome: " + getNome() + "\n Documento:" + getDocumento() + " \nData em entrada" + getDataAssociacao();
	}
	

}