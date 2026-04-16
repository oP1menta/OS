package Classe;

import Exception.InvalidArgumentException;

public class ClienteJuridica extends Cliente {
    private String cnpj;
    private String CEP;

    public ClienteJuridica(String nome, String telefone, String email,String Cidade, String CEP ,String cnpj) throws InvalidArgumentException {
        super(nome, telefone, email, Cidade, CEP);
        this.setCnpj(cnpj);
    }

    @Override
    public String getDocumento() {
        return this.cnpj; 
    }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

	
	

	
}
