
package Aplicação;

import java.sql.Connection;

import BancoDeDados.ClienteDao;
import BancoDeDados.EquipamentoDAO;
import BancoDeDados.conect;
import Classe.Cliente;
import Classe.Equipamento;
import Factory.Fac; 

public class Programa {
	//Pedro Burrinho deu commit sem net comentario para mudar o commit
	
	private static conect conectar = null;

	public static void main(String[] args) {

		
		System.out.println("pedro buuro");
		try{

			
			conectar = new conect();
			 Fac factory = Fac.getInstancia();
			


			System.out.println("Usuario da Conexao: " + conectar.getConexao().getMetaData().getUserName());
			System.out.println("URL da Conexao: " + conectar.getConexao().getMetaData().getURL());
			//buscandoDadosParaTeste();
			
			    Cliente novoCliente = factory.criarCliente("Biel  ", "01111112", "bielaaa@email.com", "12345678902");

	           
			   ClienteDao clienteDao = new ClienteDao();
	            clienteDao.salvarcliente(novoCliente, conectar.getConexao());

	           
	            Equipamento novoEquipamento = new Equipamento(0, "vibrador grande", "intelamd", novoCliente.getDocumento());

	            EquipamentoDAO equipamentoDao = new EquipamentoDAO();
	            equipamentoDao.inserir(novoEquipamento, conectar.getConexao());

	            System.out.println("\n--- TESTE CONCLUÍDO COM SUCESSO ---");
	            
	            Cliente busca = clienteDao.buscarPorDocumento(novoCliente.getDocumento(), conectar.getConexao());
	            if (busca != null) {
	                System.out.println("Dono: " + busca.getNome());
	                
	                // Buscamos o equipamento pelo ID que o banco gerou para o objeto 'e'
	                Equipamento eqBusca = equipamentoDao.buscarPorId(novoEquipamento.getId(), conectar.getConexao());
	                System.out.println("Equipamento vinculado: " + eqBusca.getNome() + " - " + eqBusca.getModelo());
	                System.out.println("Documento na FK: " + eqBusca.getDocumentoCliente());
	            }

		}catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			if(conectar != null)
				conectar.fecharConexao();
		}
		
		

	    
	         
	  

	}
	}
