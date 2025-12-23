package Factory;
import Classe.*;

public class Fac {
	private static Fac InstanciaUnica;
	private Fac() {}
	public Fac getInstancia() {
		if (InstanciaUnica == null) {
			InstanciaUnica = new Fac();
		}
		return InstanciaUnica;
	}

	
	//FAZER TRY CATCH para impossibilitar coisas diferentes de 11 e 14
	public Pessoa getPessoa(String nome,String telefone,String email,String cpf) {
		
		if(length == 11) {
			return new PessoaFisica(String nome,String telefone,String email,String cpf);
		}
		
		return new PessoaJuridica(String nome,String telefone,String email,String cpf);
	}	
}
