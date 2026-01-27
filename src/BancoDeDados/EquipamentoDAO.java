package BancoDeDados;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Classe.Equipamento;
import Exception.InvalidArgumentException;
import Factory.Fac;

public class EquipamentoDAO {


    private Equipamento montar(ResultSet rs)
            throws SQLException, InvalidArgumentException {

        Equipamento eq = Fac.getInstancia().criarEquipamento(
                rs.getString("nome"),
                rs.getString("modelo"),
                rs.getString("documento_cliente")
        );

        eq.setId(rs.getInt("id"));
        eq.setAtivo(rs.getBoolean("ativo"));

        return eq;
    }


    public void salvar(Equipamento eq) throws InvalidArgumentException {

        String sql = """
            INSERT INTO equipamento (nome, modelo, documento_cliente, ativo)
            VALUES (?, ?, ?, ?)
        """;

        conect conexaoObj = new conect();

        try (Connection con = conexaoObj.getConexao();
             PreparedStatement ps = con.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, eq.getNome());
            ps.setString(2, eq.getModelo());
            ps.setString(3, eq.getDocumentoCliente());
            ps.setBoolean(4, eq.getAtivo());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    eq.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar equipamento", e);
        }
    }


    public List<Equipamento> buscarPorDocumentoCliente(String documento) {

        List<Equipamento> lista = new ArrayList<>();

        String sql = """
            SELECT * FROM equipamento
            WHERE documento_cliente = ? AND ativo = TRUE
        """;

        conect conexaoObj = new conect();

        try (Connection con = conexaoObj.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, documento);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }

        } catch (SQLException | InvalidArgumentException e) {
            throw new RuntimeException("Erro ao buscar equipamentos do cliente", e);
        }

        return lista;
    }


    public Equipamento buscarPorId(Long equipamentoId) {

        String sql = "SELECT * FROM equipamento WHERE id = ?";

        conect conexaoObj = new conect();

        try (Connection con = conexaoObj.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, equipamentoId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }

        } catch (SQLException | InvalidArgumentException e) {
            throw new RuntimeException("Erro ao buscar equipamento por ID", e);
        }

        return null;
    }

    /* ===================== LIST ALL ===================== */

    public List<Equipamento> listarTodos() {

        List<Equipamento> lista = new ArrayList<>();

        String sql = "SELECT * FROM equipamento WHERE ativo = TRUE";

        conect conexaoObj = new conect();

        try (Connection con = conexaoObj.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(montar(rs));
            }

        } catch (SQLException | InvalidArgumentException e) {
            throw new RuntimeException("Erro ao listar equipamentos", e);
        }

        return lista;
    }

    /* ===================== DELETE FÍSICO ===================== */

    public void deletar(int id) {

        String sql = "DELETE FROM equipamento WHERE id = ?";

        conect conexaoObj = new conect();

        try (Connection con = conexaoObj.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar equipamento", e);
        }
    }

    /* ===================== DELETE LÓGICO ===================== */

    public void desativar(int id) {

        String sql = "UPDATE equipamento SET ativo = FALSE WHERE id = ?";

        conect conexaoObj = new conect();

        try (Connection con = conexaoObj.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao desativar equipamento", e);
        }
    }
}
