package Classe;

import Exception.InvalidArgumentException;

public class ClienteFisica extends Cliente {
    private String cpf;

    public ClienteFisica(String nome, String telefone, String email, String cpf) throws InvalidArgumentException {
        super(nome, telefone, email);
        this.cpf = cpf;
    }

    @Override
    public String getDocumento() {
        return this.cpf; // Para o sistema, o documento da Pessoa Física é o CPF
    }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
}
