package BancoDeDados;

import Classe.Orçamento;
import Classe.Tecnico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrçamentoDAO {

    /* ==============================
       MAPEAMENTO
       ============================== */
    private Orçamento criarOrcamentoDoResultSet(ResultSet rs, Tecnico tecnico)
            throws SQLException {

        Orçamento orcamento = new Orçamento(
                rs.getString("peca"),
                rs.getBigDecimal("valor"),
                rs.getString("tipo_pagamento"),
                tecnico
        );

        // controle interno
        orcamento.setId(rs.getInt("id"));

        // estado
        Orçamento.Status status =
                Orçamento.Status.valueOf(rs.getString("status"));

        switch (status) {
            case APROVADO -> orcamento.aprovar();
            case REPROVADO -> orcamento.reprovar();
            case EXPIRADO -> orcamento.expirar();
            default -> { /* permanece pendente */ }
        }

        return orcamento;
    }

    /* ==============================
       CREATE
       ============================== */
    public void salvar(Orçamento orcamento) {

        String sql = """
            INSERT INTO orcamento
            (peca, valor, tipo_pagamento, tecnico_id, status,
             data_criacao, data_decisao, observacoes)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, orcamento.getPeca());
            stmt.setBigDecimal(2, orcamento.getValor());
            stmt.setString(3, orcamento.getTipoPagamento());
            stmt.setLong(4, orcamento.getTecnicoResponsavel().getId());
            stmt.setString(5, orcamento.getStatus().name());
            stmt.setTimestamp(6, Timestamp.valueOf(orcamento.getDataCriacao()));
            stmt.setTimestamp(7,
                    orcamento.getDataDecisao() == null
                            ? null
                            : Timestamp.valueOf(orcamento.getDataDecisao()));
            stmt.setString(8, orcamento.getObservacoes());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    orcamento.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar orçamento", e);
        } finally {
            conexaoObj.fecharConexao();
        }
    }

    /* ==============================
       UPDATE (STATUS)
       ============================== */
    public void atualizarEstado(Orçamento orcamento) {

        String sql = """
            UPDATE orcamento
            SET status = ?, data_decisao = ?, observacoes = ?
            WHERE id = ?
        """;

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, orcamento.getStatus().name());
            stmt.setTimestamp(2,
                    orcamento.getDataDecisao() == null
                            ? null
                            : Timestamp.valueOf(orcamento.getDataDecisao()));
            stmt.setString(3, orcamento.getObservacoes());
            stmt.setLong(4, orcamento.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar orçamento", e);
        } finally {
            conexaoObj.fecharConexao();
        }
    }

    /* ==============================
       READ
       ============================== */
    public Orçamento buscarPorId(Long id, Tecnico tecnico) {

        String sql = "SELECT * FROM orcamento WHERE id = ?";

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarOrcamentoDoResultSet(rs, tecnico);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar orçamento", e);
        } finally {
            conexaoObj.fecharConexao();
        }

        return null;
    }

    public List<Orçamento> listarPorTecnico(Tecnico tecnico) {

        List<Orçamento> lista = new ArrayList<>();

        String sql = """
            SELECT * FROM orcamento
            WHERE tecnico_id = ?
            ORDER BY data_criacao DESC
        """;

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, tecnico.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(criarOrcamentoDoResultSet(rs, tecnico));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar orçamentos", e);
        } finally {
            conexaoObj.fecharConexao();
        }

        return lista;
    }
}