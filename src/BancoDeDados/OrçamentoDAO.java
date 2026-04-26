package BancoDeDados;

import Classe.ItemOrcamento;
import Classe.Orçamento;
import Classe.Tecnico;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrçamentoDAO {

    public void salvar(Orçamento orcamento) {

        String sqlOrc = """
            INSERT INTO orcamento
            (tipo_pagamento, tecnico_id, status, data_criacao, data_decisao, observacoes, valor_mao_de_obra)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        String sqlItem = """
            INSERT INTO item_orcamento (orcamento_id, descricao, valor)
            VALUES (?, ?, ?)
        """;

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao()) {

            conn.setAutoCommit(false);

            int orcId;
            try (PreparedStatement stmt = conn.prepareStatement(sqlOrc, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, orcamento.getTipoPagamento());
                stmt.setLong(2, orcamento.getTecnicoResponsavel().getId());
                stmt.setString(3, orcamento.getStatus().name());
                stmt.setTimestamp(4, Timestamp.valueOf(orcamento.getDataCriacao()));

                if (orcamento.getDataDecisao() != null) {
                    stmt.setTimestamp(5, Timestamp.valueOf(orcamento.getDataDecisao()));
                } else {
                    stmt.setNull(5, Types.TIMESTAMP);
                }

                stmt.setString(6, orcamento.getObservacoes());
                stmt.setBigDecimal(7, orcamento.getMão_de_obra() != null
                        ? orcamento.getMão_de_obra() : BigDecimal.ZERO);

                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    rs.next();
                    orcId = rs.getInt(1);
                    orcamento.setId(orcId);
                }
            }

            try (PreparedStatement stmtItem = conn.prepareStatement(
                    sqlItem, Statement.RETURN_GENERATED_KEYS)) {

                for (ItemOrcamento item : orcamento.getItens()) {
                    stmtItem.setInt(1, orcId);
                    stmtItem.setString(2, item.getDescricao());
                    stmtItem.setBigDecimal(3, item.getValor());
                    stmtItem.executeUpdate();

                    try (ResultSet rs = stmtItem.getGeneratedKeys()) {
                        if (rs.next()) item.setId(rs.getInt(1));
                    }
                }
            }

            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar orçamento", e);
        } finally {
            conexaoObj.fecharConexao();
        }
    }

    public void atualizarEstado(Orçamento orcamento) {

        String sql = """
            UPDATE orcamento
            SET status = ?, data_decisao = ?, observacoes = ?, valor_mao_de_obra = ?
            WHERE id = ?
        """;

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, orcamento.getStatus().name());

            if (orcamento.getDataDecisao() != null) {
                stmt.setTimestamp(2, Timestamp.valueOf(orcamento.getDataDecisao()));
            } else {
                stmt.setNull(2, Types.TIMESTAMP);
            }

            stmt.setString(3, orcamento.getObservacoes());
            stmt.setBigDecimal(4, orcamento.getMão_de_obra() != null
                    ? orcamento.getMão_de_obra() : BigDecimal.ZERO);
            stmt.setLong(5, orcamento.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar orçamento", e);
        } finally {
            conexaoObj.fecharConexao();
        }
    }

    public Orçamento buscarPorId(Long id, Tecnico tecnico) {

        String sql = """
            SELECT
                o.id, o.tipo_pagamento, o.status,
                o.data_criacao, o.data_decisao, o.observacoes, o.valor_mao_de_obra,
                t.id AS tecnico_id, t.nome AS tecnico_nome,
                t.documento AS tecnico_documento,
                t.data_associacao AS tecnico_data_associacao
            FROM orcamento o
            JOIN tecnico t ON t.id = o.tecnico_id
            WHERE o.id = ?
        """;

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Tecnico tec = tecnico != null ? tecnico : criarTecnico(rs);
                    Orçamento orc = criarOrcamentoDoResultSet(rs, tec);
                    orc.setItens(buscarItens(conn, orc.getId()));
                    return orc;
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
            SELECT
                o.id, o.tipo_pagamento, o.status,
                o.data_criacao, o.data_decisao, o.observacoes, o.valor_mao_de_obra,
                t.id AS tecnico_id, t.nome AS tecnico_nome,
                t.documento AS tecnico_documento,
                t.data_associacao AS tecnico_data_associacao
            FROM orcamento o
            JOIN tecnico t ON t.id = o.tecnico_id
            WHERE o.tecnico_id = ?
            ORDER BY o.data_criacao DESC
        """;

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, tecnico.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Orçamento orc = criarOrcamentoDoResultSet(rs, tecnico);
                    orc.setItens(buscarItens(conn, orc.getId()));
                    lista.add(orc);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar orçamentos", e);
        } finally {
            conexaoObj.fecharConexao();
        }

        return lista;
    }

    public List<Orçamento> listarAprovados() {

        List<Orçamento> lista = new ArrayList<>();

        String sql = """
            SELECT
                o.id, o.tipo_pagamento, o.status,
                o.data_criacao, o.data_decisao, o.observacoes, o.valor_mao_de_obra,
                t.id AS tecnico_id, t.nome AS tecnico_nome,
                t.documento AS tecnico_documento,
                t.data_associacao AS tecnico_data_associacao
            FROM orcamento o
            JOIN tecnico t ON t.id = o.tecnico_id
            WHERE o.status = ?
            ORDER BY o.data_criacao DESC
        """;

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, Orçamento.Status.APROVADO.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Tecnico tecnico = criarTecnico(rs);
                    Orçamento orc = criarOrcamentoDoResultSet(rs, tecnico);
                    orc.setItens(buscarItens(conn, orc.getId()));
                    lista.add(orc);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar orçamentos aprovados", e);
        } finally {
            conexaoObj.fecharConexao();
        }

        return lista;
    }

    // ── NOVO: busca orçamentos vinculados às OS de um equipamento ──────────────
    public List<Orçamento> listarPorEquipamento(int equipamentoId) {

        List<Orçamento> lista = new ArrayList<>();

        String sql = """
            SELECT DISTINCT
                o.id, o.tipo_pagamento, o.status,
                o.data_criacao, o.data_decisao, o.observacoes, o.valor_mao_de_obra,
                t.id AS tecnico_id, t.nome AS tecnico_nome,
                t.documento AS tecnico_documento,
                t.data_associacao AS tecnico_data_associacao
            FROM orcamento o
            JOIN tecnico t ON t.id = o.tecnico_id
            JOIN ordem_de_servico os ON os.orcamento_id = o.id
            WHERE os.equipamento_id = ?
            ORDER BY o.data_criacao DESC
        """;

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, equipamentoId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Tecnico tecnico = criarTecnico(rs);
                    Orçamento orc = criarOrcamentoDoResultSet(rs, tecnico);
                    orc.setItens(buscarItens(conn, orc.getId()));
                    lista.add(orc);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar orçamentos por equipamento", e);
        } finally {
            conexaoObj.fecharConexao();
        }

        return lista;
    }

    private List<ItemOrcamento> buscarItens(Connection conn, int orcamentoId) throws SQLException {

        List<ItemOrcamento> itens = new ArrayList<>();
        String sql = "SELECT id, descricao, valor FROM item_orcamento WHERE orcamento_id = ? ORDER BY id";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orcamentoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItemOrcamento item = new ItemOrcamento(
                        rs.getString("descricao"),
                        rs.getBigDecimal("valor")
                    );
                    item.setId(rs.getInt("id"));
                    itens.add(item);
                }
            }
        }

        return itens;
    }

    private Orçamento criarOrcamentoDoResultSet(ResultSet rs, Tecnico tecnico) throws SQLException {

        BigDecimal valorMaoDeObra = rs.getBigDecimal("valor_mao_de_obra");
        if (valorMaoDeObra == null) valorMaoDeObra = BigDecimal.ZERO;

        List<ItemOrcamento> placeholder = new ArrayList<>();
        placeholder.add(new ItemOrcamento("carregando", BigDecimal.ONE));

        Orçamento orc = new Orçamento(placeholder, rs.getString("tipo_pagamento"), tecnico, valorMaoDeObra);

        orc.setId(rs.getInt("id"));
        orc.setStatus(Orçamento.Status.valueOf(rs.getString("status")));
        orc.setMão_de_obra(valorMaoDeObra);

        Timestamp criacao = rs.getTimestamp("data_criacao");
        if (criacao != null) orc.setDataCriacao(criacao.toLocalDateTime());

        Timestamp decisao = rs.getTimestamp("data_decisao");
        if (decisao != null) orc.setDataDecisao(decisao.toLocalDateTime());

        orc.setObservacoes(rs.getString("observacoes"));

        return orc;
    }

    private Tecnico criarTecnico(ResultSet rs) throws SQLException {
        Tecnico tecnico = new Tecnico(
            rs.getString("tecnico_nome"),
            rs.getString("tecnico_documento"),
            rs.getDate("tecnico_data_associacao").toLocalDate()
        );
        tecnico.setId(rs.getInt("tecnico_id"));
        return tecnico;
    }
}