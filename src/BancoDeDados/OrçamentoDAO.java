package BancoDeDados;

import Classe.Orçamento;
import Classe.OrdemDeServico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrçamentoDAO {

    /* =========================
       SALVAR
       ========================= */
    public void salvar(Orçamento orcamento) {

        String sql = """
            INSERT INTO orcamento
            (peca, valor, tipo_pagamento, status, ordem_servico_id)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection con = new conect().getConexao();
             PreparedStatement ps =
                     con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, orcamento.getPeca());
            ps.setBigDecimal(2, orcamento.getValor());
            ps.setString(3, orcamento.getTipoPagamento());
            ps.setString(4, orcamento.getStatus().name());
            ps.setLong(5, orcamento.getOrdemDeServico().getId());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                orcamento.setIdOrcamento(rs.getLong(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar orçamento", e);
        }
    }

    /* =========================
       ATUALIZAR STATUS
       ========================= */
    public void atualizarStatus(Orçamento orcamento) {

        String sql = """
            UPDATE orcamento
            SET status = ?
            WHERE id = ?
        """;

        try (Connection con = new conect().getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, orcamento.getStatus().name());
            ps.setLong(2, orcamento.getIdOrcamento());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar status do orçamento", e);
        }
    }

    /* =========================
       BUSCAR POR ID
       ========================= */
    public Orçamento buscarPorId(Long id, OrdemDeServico os) {

        String sql = "SELECT * FROM orcamento WHERE id = ?";

        try (Connection con = new conect().getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapear(rs, os);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar orçamento", e);
        }

        return null;
    }

    /* =========================
       LISTAR POR OS
       ========================= */
    public List<Orçamento> listarPorOrdemServico(OrdemDeServico os) {

        String sql = "SELECT * FROM orcamento WHERE ordem_servico_id = ?";
        List<Orçamento> lista = new ArrayList<>();

        try (Connection con = new conect().getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, os.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapear(rs, os));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar orçamentos", e);
        }

        return lista;
    }

    /* =========================
       APOIO
       ========================= */
    private Orçamento mapear(ResultSet rs, OrdemDeServico os) throws SQLException {

        return new Orçamento(
                rs.getLong("id"),
                rs.getString("peca"),
                rs.getBigDecimal("valor"),
                rs.getString("tipo_pagamento"),
                Orçamento.StatusOrcamento.valueOf(rs.getString("status")),
                os
        );
    }
}