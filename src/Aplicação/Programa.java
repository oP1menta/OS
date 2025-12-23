
package Aplicação;

import BancoDeDados.conect; 

public class Programa {
	//Pedro Burrinho deu commit sem net comentario para mudar o commit
	
	private static conect conectar = null;

	public static void main(String[] args) {

		
		System.out.println("pedro buuro");
		try{

			//servidor, banco de dados, usuario, senha
			conectar = new conect();

			//Aqui pode vir o uso da conex�o para executar comando DDL ou DML


			System.out.println("Usuario da Conexao: " + conectar.getConexao().getMetaData().getUserName());
			System.out.println("URL da Conexao: " + conectar.getConexao().getMetaData().getURL());
			//buscandoDadosParaTeste();

		}catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			if(conectar != null)
				conectar.fecharConexao();
		}

	}
	}
