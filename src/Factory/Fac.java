package Factory;
import java.util.ArrayList;
import Classe.*;
import Exception.InvalidArgumentException;

public class Fac {
	private ArrayList<Cliente> clientes = new ArrayList<>();
	private static Fac InstanciaUnica;
	private Fac() {}
	public static Fac getInstancia() {
		if (InstanciaUnica == null) {
			InstanciaUnica = new Fac();
		}
		return InstanciaUnica;
	}

	public static Usuario criarUsuario(String login, String senha) {
		return new Usuario(login,senha);
	}
	

	
	
	public Cliente criarCliente(
		    String nome,
		    String telefone,
		    String email,
		    String documento
		) throws InvalidArgumentException {

		    documento = documento.replaceAll("\\D", "");

		    Cliente c;

		    if (documento.length() == 11) {
		        c = new ClienteFisica(nome, telefone, email, documento);
		    } 
		    else if (documento.length() == 14) {
		        c = new ClienteJuridica(nome, telefone, email, documento);
		    } 
		    else {
		        throw new InvalidArgumentException("Documento invalido");
		    }

		    clientes.add(c);
		    return c;
		}
	public ArrayList<Cliente> getCliente() {
    return clientes;
	}
	
	
	public Equipamento criarEquipamento (int id, String nome,String modelo,String FK_Cliente) {
		return new Equipamento(id,nome,modelo,FK_Cliente);
	}
	
	
}
