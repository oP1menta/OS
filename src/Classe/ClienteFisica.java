package Classe;

import Exception.InvalidArgumentException;

public class ClienteFisica extends Cliente {
    private String cpf;

    public ClienteFisica(String nome, String telefone, String email, String cpf) throws InvalidArgumentException {
        super(nome, telefone, email);
        this.setCpf(cpf);
    }

    @Override
    public String getDocumento() {
        return this.cpf; 
    }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
}
