package Factory;
import Classe.*;
import Exception.InvalidArgumentException;

public class Fac {
	private static Fac InstanciaUnica;
	private Fac() {}
	public static Fac getInstancia() {
		if (InstanciaUnica == null) {
			InstanciaUnica = new Fac();
		}
		return InstanciaUnica;
	}

	
	
	public Cliente getCliente(String nome,String telefone,String email,String documento) throws InvalidArgumentException {
		
		documento = documento.replaceAll("\\D", "");
		 
		 
		if(documento.length() == 11) {
			System.out.println("eu sou fisica");
			return new ClienteFisica(nome, telefone, email, documento);
		}
		else if(documento.length()==14) {
			System.out.println("eu sou juridica");
			return new ClienteJuridica(nome, telefone, email, documento);
		}
		throw new InvalidArgumentException("Documento invalido");
	}	
}
