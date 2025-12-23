package Classe;

public class PessoaFisica extends Pessoa {
	public PessoaFisica (
			String nome,
			String telefone,
			String email,
			String cpf) {
		
		try {
			setNome(nome);
		}catch();
		
		setTelefone(telefone);
		
		setEmail(email);
		
		setCpf(cpf);
	}
	
	public void setNome (String nome) {
		this.nome=nome;
	}
	public void setTelefone(String telefone) {
		this.telefone=telefone;
	}
	public void setEmail(String email) {
		this.email=email;
	}
	

}
