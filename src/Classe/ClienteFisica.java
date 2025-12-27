package Classe;

import Exception.InvalidArgumentException;

public class ClienteFisica extends Cliente {
	public ClienteFisica (
			String nome,
			String telefone,
			String email,
			String cpf) {
		
		super(nome,telefone,email,cpf);
	}
	
	public void toText () {
		System.out.println(nome+telefone+email+documento);
		System.out.println("Fisica");
		
	}

}
