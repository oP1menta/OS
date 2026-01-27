package BancoDeDados;

import Classe.Tecnico;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TecnicoDAO {

    public void salvar(Tecnico tecnico) {

        String sql = """
            INSERT INTO tecnico (nome, documento, equipamento, data_associacao)
            VALUES (?, ?, ?, ?)
        """;
        conect conexaoObj = new conect();
        try (Connection con = conexaoObj.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, tecnico.getNome());
            ps.setString(2, tecnico.getDocumento());
            ps.setString(3, tecnico.getEquipamento());
            ps.setDate(4, Date.valueOf(tecnico.getDataAssociacao()));

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
                        rs.getString("equipamento"),
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
                        rs.getString("equipamento"),
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
}