package Classe;

public class ClienteFisica extends Cliente {
	public ClienteFisica (
			String nome,
			String telefone,
			String email,
			String cpf) {
		
		super(nome,telefone,email,cpf);
	}
	
	@Override
	public String toString() {
		return  "Pessoa Fisica-------------------"+super.toString()+"--------------------------------";
	}
	

}
