package BancoDeDados;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Classe.*;
import Factory.Fac;
import Exception.InvalidArgumentException;

public class ClienteDao {
 
	//Esse metodo é responsavel por pegar as informações do banco de dados.: 
	//Ele pega o ResultSet (a tabela de resultados) que já está com o cursor apontando para a linha  quem esta o cliente.
   
    private Cliente criarClienteDoResultSet(ResultSet rs) throws SQLException, InvalidArgumentException {
        Fac factory = Fac.getInstancia();
        
        //Buscas as coisa no BD
        // Ele vai na coluna nome e pega esse nome e o transforma em uma string e assim por diante
        String nome = rs.getString("nome");
        String telefone = rs.getString("telefone");
        String email = rs.getString("email");
        String documento = rs.getString("documento");
        boolean ativo = rs.getBoolean("ativo");

       //E depois passa para a criação do objeto aq dentro do programa com essa informações do BD, e assim é possiveo pegar de maneira real o objeto
        Cliente cliente = factory.criarCliente(nome, telefone, email, documento);
        cliente.setAtivo(ativo);
        
        return cliente;
    }

  
    public void salvarcliente(Cliente cliente, Connection conn) {
      
        String sql = "INSERT INTO cliente (documento, nome, telefone, email, ativo) VALUES (?, ?, ?, ?, TRUE)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getDocumento());
            stmt.setString(2, cliente.getNome());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            
            stmt.executeUpdate();
            System.out.println(" Sucesso: Cliente " + cliente.getNome() + " cadastrado.");

        } catch (SQLException e) {
            String estadoSQL = e.getSQLState();
            if ("23505".equals(estadoSQL)) {
                System.err.println(" Erro: O documento " + cliente.getDocumento() + " já existe.");
            } else {
                System.err.println(" Erro SQL [" + estadoSQL + "]: " + e.getMessage());
            }
        }
    }

    
    public void atualizar(Cliente cliente, Connection conn) {
        String sql = "UPDATE cliente SET nome = ?, telefone = ?, email = ? WHERE documento = ?";
        // o PrapedStatement nada mais é como o nome ja diz , e a preparação da pergunta para o BD
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
          //Depois associa a cada local o seu devido valor 
        	stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getDocumento()); 
            
            //Aqui ele de fato realiza o update e conta quantas linhas foram afetadas
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas == 0) {
                System.err.println(" Erro de Localização: Cliente não encontrado com o documento " + cliente.getDocumento());
            } else {
                System.out.println(" Sucesso: Dados atualizados para " + cliente.getNome());
            }

        } catch (SQLException e) {
            String estadoSQL = e.getSQLState();
            if ("22001".equals(estadoSQL)) {
                System.err.println(" Erro de Tamanho: Texto longo demais para o campo.");
            } else {
                System.err.println("Erro ao Atualizar [" + estadoSQL + "]: " + e.getMessage());
            }
        }
    }

   
    public Cliente buscarPorDocumento(String documento, Connection conn) {
        String sql = "SELECT * FROM cliente WHERE documento = ?";
        // antes ele pega a pergunta pronta e a monta atravez do preparedStatement
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, documento);
            // resultSer armazena a tabela de resultados que o BD geral 
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                	// Passa essa tabela para o metodo que cria o objeto cliente
                    return criarClienteDoResultSet(rs);
                }
            }
        } catch (SQLException | InvalidArgumentException e) {
            System.err.println(" Erro na Busca: " + e.getMessage());
        }
        return null; 
    }

    
    public List<Cliente> listarPorNome(String nome, Connection conn) {
        List<Cliente> clientes = new ArrayList<>();// Cria o espaço temporario para armazenar a lista de clientes
        
        String sql = "SELECT * FROM cliente WHERE nome ILIKE ? AND ativo = TRUE";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(criarClienteDoResultSet(rs)); 
                }
            }
        } catch (SQLException | InvalidArgumentException e) {
            System.err.println(" Erro ao listar: " + e.getMessage());
        }
        return clientes;
    }

   
    public void desativar(String documento, Connection conn) {
        String sql = "UPDATE cliente SET ativo = FALSE WHERE documento = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, documento);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                System.err.println(" Erro: Documento não encontrado.");
            } else {
                System.out.println(" Cliente desativado com sucesso.");
            }
        } catch (SQLException e) {
            System.err.println(" Erro ao Desativar: " + e.getMessage());
        }
    }
}
