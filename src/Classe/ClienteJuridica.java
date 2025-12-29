package Classe;


public class ClienteJuridica extends Cliente {
	
	public ClienteJuridica(
			String nome,
			String telefone,
			String email,
			String cnpj) {
		
		super(nome,telefone,email,cnpj);
		
	}
	@Override
	public String toString () {
			return"\nPessoa Juridica-----------------"+super.toString()+"--------------------------------";		
	}

}
