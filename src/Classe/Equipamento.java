package Classe;
import  Exception.InvalidArgumentException;

public class Equipamento {
	private int id = 0;
	private String nome;
	private String modelo;

	public Equipamento(int id,String nome,String modelo) {
		
		try {
			setId(id);
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
		setNome(nome);
		try {
			setModelo(modelo);
		} catch (InvalidArgumentException e) {1
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
		else throw new InvalidArgumentException("Modelo vazio");
	}

	public int getId() {return id; }
	public String getNome() {return nome;}
	public String getModelo() {return modelo;}
	
	
}
