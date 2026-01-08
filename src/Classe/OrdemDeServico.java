package Classe;
import java.util.Date;
import Classe.*;
import Exception.InvalidArgumentException;

public class OrdemDeServico {
	private Cliente cliente;
	private Equipamento equipamento;
	private int Id_Os;
	private Date Data_Abertura,Data_Fechamento;
	private String Descricao;
	private int FK_Equipamento;
	private int FK_Usuario;

	public OrdemDeServico(
			Cliente cliente,
			Equipamento equipamento,
			int Id_Os,
			Date Data_Abertura,
			String Descricao,
			int FK_Equipamento,
			int FK_Usuario) throws InvalidArgumentException {
		if (cliente == null) {
			throw new InvalidArgumentException("Ordem de Serviço Sem Cliente");
		}
		else if (equipamento == null){
			throw new InvalidArgumentException("Ordem de Serviço Sem Maquina");
			}
		else {
			this.cliente=cliente;
			this.equipamento=equipamento;
			setDescricao(Descricao);
			try {
				setId_Os(Id_Os);
			} catch (InvalidArgumentException e) {
				e.printStackTrace();
			}
			try {
				setData(Data_Abertura);
			} catch (InvalidArgumentException e) {
				e.printStackTrace();
			}
		}
	
	}

	
	public void setDescricao(String Descricao) {
		this.Descricao = Descricao;
	}
	
	public void setId_Os(int Id_Os) throws InvalidArgumentException {
		if(Id_Os < 0) {
			throw new InvalidArgumentException("Id da OS Inválido");
		}
		else {
		this.Id_Os = Id_Os;
		}
	}
	
	public void setData(Date Data_Abertura) throws InvalidArgumentException {
		if (Data_Abertura == null) {
			throw new InvalidArgumentException("Data de abertura de OS invalida ou nula");
		}
		else {
			this.Data_Abertura = Data_Abertura;			
		}
	}

	public String getDescricao() {return Descricao;}
	public int getId_Os() {return Id_Os;}
	public Date getData_Abertura() {return Data_Abertura;}
	
}

