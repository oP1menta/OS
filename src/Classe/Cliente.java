package Classe;

import java.util.ArrayList;
import java.util.List;
import Exception.InvalidArgumentException;

public abstract class Cliente {
    protected String nome;
    protected String telefone;
    protected String email;
    protected boolean ativo = true; 
    protected List<Equipamento> equipamentos = new ArrayList<>();

   
    public Cliente(String nome, String telefone, String email) throws InvalidArgumentException {
        this.setNome(nome);
        this.setTelefone(telefone);
        this.setEmail(email);
    }

  
   

   
    public void setNome(String nome) throws InvalidArgumentException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new InvalidArgumentException("O nome não pode estar vazio.");
        }
        this.nome = nome;
    }

    public void addEquipamento(Equipamento e) {
        this.equipamentos.add(e);
    }
    public void setTelefone(String telefone) {
		this.telefone = telefone;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	public abstract String getDocumento();
    public String getNome() { return nome; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public List<Equipamento> getEquipamentos() { return equipamentos; }

    @Override
    public String toString() {
        return String.format("\nNome: %s\nDocumento: %s\nTelefone: %s\nAtivo: %s", 
                             nome, getDocumento(), telefone, ativo ? "Sim" : "Não");
    }


	
}
