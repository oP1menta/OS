package Classe;

public class ClienteJuridica extends Cliente {
	
	public ClienteJuridica(
			String nome,
			String telefone,
			String email,
			String cnpj) {
		
		super(nome,telefone,email,cnpj);
		
	}
	
	public void toText () {
		System.out.println(nome+telefone+email+documento);
		System.out.println("Juridico");
		
		
	}

}
