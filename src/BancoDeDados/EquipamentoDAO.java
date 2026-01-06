package BancoDeDados;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Classe.Equipamento;
import Exception.InvalidArgumentException;

public class EquipamentoDAO {

   
    private Equipamento criarEquipamentoDoResultSet(ResultSet rs) throws SQLException, InvalidArgumentException {
       
        return new Equipamento(
            rs.getInt("id"), 
            rs.getString("nome"), 
            rs.getString("modelo"),
            rs.getString("documento_cliente") 
        );
    }

   
    public void inserir(Equipamento equipamento, Connection conn) {
        String sql = "INSERT INTO equipamento (nome, modelo, documento_cliente) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, equipamento.getNome());
            stmt.setString(2, equipamento.getModelo());
            stmt.setString(3, equipamento.getDocumentoCliente()); 
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    equipamento.setId(generatedKeys.getInt(1));
                }
            }
            System.out.println(" Sucesso: Equipamento " + equipamento.getNome() + "do cliente " + equipamento.getDocumentoCliente());

        } catch (SQLException | InvalidArgumentException e) {
            System.err.println(" Erro ao Inserir Equipamento: " + e.getMessage());
        }
    }

    
    public Equipamento buscarPorId(int id, Connection conn) {
        String sql = "SELECT * FROM equipamento WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarEquipamentoDoResultSet(rs);
                }
            }
        } catch (SQLException | InvalidArgumentException e) {
            System.err.println(" Erro na Busca por ID: " + e.getMessage());
        }
        return null; 
    }

    
    public List<Equipamento> buscarTodos(Connection conn) {
        List<Equipamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM equipamento";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                lista.add(criarEquipamentoDoResultSet(rs));
            }
        } catch (SQLException | InvalidArgumentException e) {
            System.err.println(" Erro ao buscar todos os equipamentos: " + e.getMessage());
        }
        return lista;
    }
}
