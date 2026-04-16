package Classe;

import Exception.InvalidArgumentException;

public class Equipamento {

    private int id;
    private String nome;
    private String modelo;
    private String documentoCliente;
    private Boolean ativo;
    private Cliente cliente;

    
    public Equipamento(String nome, String modelo, String documentoCliente) {
    	
    
        setNome(nome);
        try {
			setModelo(modelo);
		} catch (InvalidArgumentException e) {
			
			e.printStackTrace();
		}
        this.setDocumentoCliente(documentoCliente); 
        this.setAtivo(true); 
    }

  
    public Equipamento(int id, String nome, String modelo, String documentoCliente, Boolean ativo)
    {

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

this.setDocumentoCliente(documentoCliente);

this.setAtivo(ativo);
}


 

    public void setId(int id) throws InvalidArgumentException {
        if (id <= 0)
            throw new InvalidArgumentException("Id inválido");
        this.id = id;
    }

    public void setNome(String nome) {
    	
        this.nome = nome;
    }

    public void setModelo(String modelo) throws InvalidArgumentException {
        if (modelo == null || modelo.isBlank())
            throw new InvalidArgumentException("Modelo inválido");
        this.modelo = modelo;
    }

    public void setDocumentoCliente(String documentoCliente) {
        this.documentoCliente = documentoCliente;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    
       //GETTERS
    

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getModelo() { return modelo; }
    public String getDocumentoCliente() { return documentoCliente; }
    public Boolean getAtivo() { return ativo; }

    @Override
    public String toString() {
        return nome + " - " + modelo;
    }
}
