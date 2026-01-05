package Classe;
import  Exception.InvalidArgumentException;

public class Equipamento {
	private int id = 0;
	private String nome;
	private String modelo;
	private String FK_Cliente;

	public Equipamento(int id,String nome,String modelo,String FK_Cliente) {
		
		try {
			setFK(FK_Cliente);
		}catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
		
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
		
	}
	
	public void setId(int id) throws InvalidArgumentException {
		if (id == 0) {			
			throw new InvalidArgumentException("Id vazio");
		}
		else {
			this.id = id;
		}
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setModelo(String modelo) throws InvalidArgumentException  {
		if (modelo != null && !modelo.isEmpty()) {			
			this.modelo = modelo;
		}
		else {
			throw new InvalidArgumentException("Modelo vazio");
		}
	}
	
	public void setFK(String FK_Cliente) throws InvalidArgumentException{
		if (FK_Cliente != null && !FK_Cliente.isEmpty()) {
			this.FK_Cliente = FK_Cliente;
		}
		else {
			throw new InvalidArgumentException("Foreing Key vazia");
		}
	}

	public int getId() {return id; }
	public String getNome() {return nome;}
	public String getModelo() {return modelo;}
	public String getFK() {return FK_Cliente;}
	
}
