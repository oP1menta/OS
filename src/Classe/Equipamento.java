package Classe;

import Exception.InvalidArgumentException;


public class Equipamento {
    private int id = 0;
    private String nome;
    private String modelo;
    private String documentoCliente; 
    private Boolean ativo = true;

  
    public Equipamento(int id, String nome, String modelo, String documentoCliente) {
        try {
            setId(id);
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
        
        setNome(nome);
        
        try {
            setModelo(modelo);
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
        
        this.documentoCliente = documentoCliente;
        
    }
    


    
    public Equipamento() {
    }
    
    public void setId(int id) throws InvalidArgumentException {
        if (id < 0) {            
            throw new InvalidArgumentException("Id inválido");
        } else {
            this.id = id;
        }
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setModelo(String modelo) throws InvalidArgumentException {
        if (modelo != null && !modelo.isEmpty()) {            
            this.modelo = modelo;
        } else {
            throw new InvalidArgumentException("Modelo vazio");
        }
    }

    public void setDocumentoCliente(String documentoCliente) {
        this.documentoCliente = documentoCliente;
    }
    

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getModelo() { return modelo; }
    public String getDocumentoCliente() { return documentoCliente; }
    public Boolean getAtivo() {return ativo;}
   
    @Override
    public String toString() {
        return "\n[Equipamento]" +
               "\nID: " + id +
               "\nNome: " + nome +
               "\nModelo: " + modelo +
               "\nCliente (Doc): " + documentoCliente + "\n";
    }
}
