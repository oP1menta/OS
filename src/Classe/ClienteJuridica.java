package Classe;

import Exception.InvalidArgumentException;

public class ClienteJuridica extends Cliente {
    private String cnpj;

    public ClienteJuridica(String nome, String telefone, String email, String cnpj) throws InvalidArgumentException {
        super(nome, telefone, email);
        this.cnpj = cnpj;
    }

    @Override
    public String getDocumento() {
        return this.cnpj; 
    }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
}
