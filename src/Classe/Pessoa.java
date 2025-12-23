package Classe;
import java.util.Scanner;
import Exception.InvalidArgumentException;

public abstract class Pessoa extends InvalidArgumentException{
	protected String nome,telefone,email;
	protected String cpf;
	protected int length;
	Scanner sc=new Scanner(System.in);
	
	public Pessoa(String nome,String telefone,String email,String cpf) {
		try{
			setNome(nome);
		}catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
		setTelefone(telefone);
		setEmail(email);
		try{
			setCpf(cpf);
		}catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void setNome (String nome) throws InvalidArgumentException{
		if (nome!="") {
			this.nome=nome;
		}
		throw new InvalidArgumentException("Campo nome vazio");
	}
	public void setTelefone(String telefone) {
		this.telefone=telefone;
	}
	public void setEmail(String email) {
		this.email=email;
	}
	public void setCpf(String cpf) throws InvalidArgumentException {
		if(cpf!="") {
			this.cpf = cpf;
			length = cpf.length();	
		}throw new InvalidArgumentException("Campo Cpf vazio");
	}
	
	
	
	public String getNome() {return nome;}
	public String getTelefone() {return telefone;}
	public String getEmail() {return email;}
	public String getCpf() {return cpf;}
	public int getLength() {return length;}
	
}
