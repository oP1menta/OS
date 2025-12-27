package Classe;
import java.util.Scanner;
import Exception.InvalidArgumentException;

public class Cliente{
	protected String nome,telefone,email;
	protected String documento;
	protected int length;
	Scanner sc=new Scanner(System.in);
	
	public Cliente(String nome,String telefone,String email,String documento) {
		try{
			setNome(nome);
		}catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
		
		setTelefone(telefone);
		setEmail(email);
		
		try{
			setCpf(documento);
		}catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
	}
		
	
	
	public void setNome (String nome) throws InvalidArgumentException{
		if (nome!=null && !nome.isEmpty()) {
			this.nome=nome;
		}else {
			throw new InvalidArgumentException("Campo nome vazio");
		}
	}
	public void setTelefone(String telefone) {
		this.telefone=telefone;
	}
	public void setEmail(String email) {
		this.email=email;
	}
	public void setCpf(String documento) throws InvalidArgumentException {
		if(documento!=null && !documento.isEmpty()) {
			this.documento = documento;
			length = documento.length();	
		}
		else {
			throw new InvalidArgumentException("Campo Documentos vazio");
		}
	}
	
	
	
	public String getNome() {return nome;}
	public String getTelefone() {return telefone;}
	public String getEmail() {return email;}
	public String getDocumento() {return documento;}
	public int getLength() {return length;}
	
}
