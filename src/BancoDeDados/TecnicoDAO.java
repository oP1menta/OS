package BancoDeDados;

import Classe.Tecnico;
import Exception.InvalidArgumentException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TecnicoDAO {

    public void salvar(Tecnico tecnico) {

        String sql = """
            INSERT INTO tecnico (nome, documento, data_associacao)
            VALUES (?, ?, ?)
        """;
        conect conexaoObj = new conect();
        try (Connection con = conexaoObj.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	        	ps.setString(1, tecnico.getNome());
	        			
	            ps.setString(2, tecnico.getDocumento());
	            ps.setDate(3, Date.valueOf(tecnico.getDataAssociacao()));
	
	            ps.executeUpdate();
	
	            ResultSet rs = ps.getGeneratedKeys();
	            if (rs.next()) {
	                tecnico.setId(rs.getInt(1));
	            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar técnico", e);
        }
    }

    public List<Tecnico> listar() {

        List<Tecnico> tecnicos = new ArrayList<>();

        String sql = "SELECT * FROM tecnico";
        conect conexaoObj = new conect();
        try (Connection con = conexaoObj.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Tecnico tecnico = new Tecnico(
                        rs.getString("nome"),
                        rs.getString("documento"),  
                        rs.getDate("data_associacao").toLocalDate()
                );
                tecnico.setId(rs.getInt("id"));
                tecnicos.add(tecnico);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar técnicos", e);
        }

        return tecnicos;
    }

    public Tecnico buscarPorId(int id) {

        String sql = "SELECT * FROM tecnico WHERE id = ?";
        conect conexaoObj = new conect();
        try (Connection con = conexaoObj.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Tecnico tecnico = new Tecnico(
                        rs.getString("nome"),
                        rs.getString("documento"),
                        rs.getDate("data_associacao").toLocalDate()
                );
                tecnico.setId(rs.getInt("id"));
                return tecnico;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar técnico", e);
        }

        return null;
    }
    public void deletar(int id) {

        String sql = "DELETE FROM tecnico WHERE id = ? ";

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int linhas = stmt.executeUpdate();

            if (linhas == 0) {
                throw new RuntimeException("Nenhum técnico encontrado com esse ID.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar técnico", e);
        } finally {
            conexaoObj.fecharConexao();
        }
    }
}