package Classe;
import java.util.ArrayList;
import Exception.InvalidArgumentException;

public class Cliente{
	protected String nome,telefone,email;
	protected String documento;
	protected int length;
	protected ArrayList<Equipamento> equipamentos;
	
	public Cliente(
			String nome,
			String telefone,
			String email,
			String documento) {
		try{
			setNome(nome);
		}catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
		this.equipamentos = new ArrayList<>();
		setTelefone(telefone);
		setEmail(email);
		
		try{
			setCpf(documento);
		}catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
	}
		
	public void addEquipamento(Equipamento e) {
		equipamentos.add(e);
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
	
	
	
	public void getEquipamentos() throws InvalidArgumentException {
		if (equipamentos.isEmpty()) {
			throw new InvalidArgumentException("Sem equipamentos");
		}
		else {
			for(Equipamento e : equipamentos) {
				System.out.println("- "+e.getNome());
			}
		}
	}
	
	public String getNome() {return nome;}
	public String getTelefone() {return telefone;}
	public String getEmail() {return email;}
	public int getLength() {return length;}
	
	//Override
	public String toString() {
		return "\nNome: "+ nome +"\n"
				+ "Telefone: " + telefone
				+ "\nEmail: " + email 
				+"\nDocumento: "+ documento+"\n";
	
	}
	
	public String getDocumento() {return documento;}
	
}
	

