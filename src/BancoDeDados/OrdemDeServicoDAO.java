package BancoDeDados;

import Classe.Equipamento;
import Classe.ItemOrcamento;
import Classe.OrdemDeServico;
import Classe.Orçamento;
import Classe.Tecnico;
import dominio.enums.StatusOrdemServico;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdemDeServicoDAO {

    public void salvar(OrdemDeServico os) {

        String sql = """
            INSERT INTO ordem_de_servico
            (equipamento_id, orcamento_id, descricao_problema,
             data_abertura, data_fechamento_previsto, status)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, os.getEquipamento().getId());

            if (os.getOrcamentoAprovado() != null) {
                stmt.setLong(2, os.getOrcamentoAprovado().getId());
            } else {
                stmt.setNull(2, Types.BIGINT);
            }

            stmt.setString(3, os.getDescricaoProblema());
            stmt.setTimestamp(4, Timestamp.valueOf(os.getDataAbertura()));
            stmt.setTimestamp(5, Timestamp.valueOf(os.getDataFechamentoPrevisto()));
            stmt.setString(6, os.getStatus().name());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) os.setId(rs.getLong(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar Ordem de Serviço", e);
        } finally {
            conexaoObj.fecharConexao();
        }
    }

    public void atualizar(OrdemDeServico os) {

        String sql = """
            UPDATE ordem_de_servico
            SET observacoes_tecnicas = ?,
                data_inicio = ?,
                data_fechamento_real = ?,
                status = ?,
                orcamento_id = ?
            WHERE id = ?
        """;

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, os.getObservacoesTecnicas());
            stmt.setTimestamp(2, toTimestamp(os.getDataInicio()));
            stmt.setTimestamp(3, toTimestamp(os.getDataFechamentoReal()));
            stmt.setString(4, os.getStatus().name());

            if (os.getOrcamentoAprovado() != null) {
                stmt.setLong(5, os.getOrcamentoAprovado().getId());
            } else {
                stmt.setNull(5, Types.BIGINT);
            }

            stmt.setLong(6, os.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar Ordem de Serviço", e);
        } finally {
            conexaoObj.fecharConexao();
        }
    }

    public OrdemDeServico buscarPorId(Long id, Equipamento equipamento, Orçamento orcamentoIgnorado) {

        String sql = """
            SELECT
                os.id,
                os.equipamento_id,
                os.orcamento_id,
                os.descricao_problema,
                os.data_abertura,
                os.data_inicio,
                os.data_fechamento_previsto,
                os.data_fechamento_real,
                os.observacoes_tecnicas,
                os.status,

                o.id           AS orc_id,
                o.tipo_pagamento,
                o.status       AS orc_status,
                o.data_criacao,
                o.data_decisao,
                o.observacoes,
                o.valor_mao_de_obra,

                t.id           AS tecnico_id,
                t.nome         AS tecnico_nome,
                t.documento    AS tecnico_documento,
                t.data_associacao AS tecnico_data_associacao

            FROM ordem_de_servico os
            LEFT JOIN orcamento o ON o.id = os.orcamento_id
            LEFT JOIN tecnico t   ON t.id = o.tecnico_id
            WHERE os.id = ?
        """;

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Equipamento equipamentoFinal = equipamento != null
                        ? equipamento
                        : buscarEquipamentoPorId(conn, rs.getLong("equipamento_id"));
                    return mapear(rs, equipamentoFinal, conn);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar OS", e);
        } finally {
            conexaoObj.fecharConexao();
        }

        return null;
    }

    public List<OrdemDeServico> listarPorEquipamento(Equipamento equipamento, Orçamento orcamentoIgnorado) {

        List<OrdemDeServico> lista = new ArrayList<>();

        String sql = """
            SELECT
                os.id,
                os.equipamento_id,
                os.orcamento_id,
                os.descricao_problema,
                os.data_abertura,
                os.data_inicio,
                os.data_fechamento_previsto,
                os.data_fechamento_real,
                os.observacoes_tecnicas,
                os.status,

                o.id           AS orc_id,
                o.tipo_pagamento,
                o.status       AS orc_status,
                o.data_criacao,
                o.data_decisao,
                o.observacoes,
                o.valor_mao_de_obra,

                t.id           AS tecnico_id,
                t.nome         AS tecnico_nome,
                t.documento    AS tecnico_documento,
                t.data_associacao AS tecnico_data_associacao

            FROM ordem_de_servico os
            LEFT JOIN orcamento o ON o.id = os.orcamento_id
            LEFT JOIN tecnico t   ON t.id = o.tecnico_id
            WHERE os.equipamento_id = ?
            ORDER BY os.id DESC
        """;

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, equipamento.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs, equipamento, conn));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao listar OS", e);
        } finally {
            conexaoObj.fecharConexao();
        }

        return lista;
    }

    public List<OrdemDeServico> listarTodos() {

        List<OrdemDeServico> lista = new ArrayList<>();

        String sql = """
            SELECT
                os.id,
                os.equipamento_id,
                os.orcamento_id,
                os.descricao_problema,
                os.data_abertura,
                os.data_inicio,
                os.data_fechamento_previsto,
                os.data_fechamento_real,
                os.observacoes_tecnicas,
                os.status,

                e.id           AS eq_id,
                e.nome,
                e.modelo,
                e.documento_cliente,
                e.ativo,

                o.id           AS orc_id,
                o.tipo_pagamento,
                o.status       AS orc_status,
                o.data_criacao,
                o.data_decisao,
                o.observacoes,
                o.valor_mao_de_obra,

                t.id           AS tecnico_id,
                t.nome         AS tecnico_nome,
                t.documento    AS tecnico_documento,
                t.data_associacao AS tecnico_data_associacao

            FROM ordem_de_servico os
            JOIN  equipamento e ON e.id = os.equipamento_id
            LEFT JOIN orcamento o ON o.id = os.orcamento_id
            LEFT JOIN tecnico t   ON t.id = o.tecnico_id
            ORDER BY os.id DESC
        """;

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Equipamento equipamento = new Equipamento(
                    rs.getInt("eq_id"),
                    rs.getString("nome"),
                    rs.getString("modelo"),
                    rs.getString("documento_cliente"),
                    rs.getBoolean("ativo")
                );
                lista.add(mapear(rs, equipamento, conn));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao listar ordens de serviço", e);
        } finally {
            conexaoObj.fecharConexao();
        }

        return lista;
    }

    // ─── Helpers privados ──────────────────────────────────────────────────────

    private OrdemDeServico mapear(ResultSet rs, Equipamento equipamento, Connection conn) throws SQLException {

        OrdemDeServico os = new OrdemDeServico(
            rs.getLong("id"),
            equipamento,
            rs.getString("descricao_problema"),
            rs.getTimestamp("data_abertura").toLocalDateTime(),
            rs.getTimestamp("data_fechamento_previsto").toLocalDateTime(),
            toLocalDateTime(rs.getTimestamp("data_fechamento_real")),
            StatusOrdemServico.valueOf(rs.getString("status"))
        );

        os.setId(rs.getLong("id"));
        os.setDataAbertura(rs.getTimestamp("data_abertura").toLocalDateTime());
        os.setDataInicio(toLocalDateTime(rs.getTimestamp("data_inicio")));
        os.setDataFechamcentoReal(toLocalDateTime(rs.getTimestamp("data_fechamento_real")));
        os.setObservacoesTecnicas(rs.getString("observacoes_tecnicas"));
        os.setStatus(StatusOrdemServico.valueOf(rs.getString("status")));
        os.setOrcamentoAprovado(criarOrcamentoSeExistir(rs, conn));

        return os;
    }

    private Orçamento criarOrcamentoSeExistir(ResultSet rs, Connection conn) throws SQLException {

        long orcId = rs.getLong("orc_id");
        if (rs.wasNull()) return null;

        Tecnico tecnico = new Tecnico(
            rs.getString("tecnico_nome"),
            rs.getString("tecnico_documento"),
            rs.getDate("tecnico_data_associacao").toLocalDate()
        );
        tecnico.setId(rs.getInt("tecnico_id"));

        BigDecimal valorMaoDeObra = rs.getBigDecimal("valor_mao_de_obra");
        if (valorMaoDeObra == null) valorMaoDeObra = BigDecimal.ZERO;

        // Placeholder para passar a validação do construtor;
        // os itens reais são carregados logo abaixo via setItens()
        List<ItemOrcamento> placeholder = new ArrayList<>();
        placeholder.add(new ItemOrcamento("carregando", BigDecimal.ONE));

        Orçamento orc = new Orçamento(
            placeholder,
            rs.getString("tipo_pagamento"),
            tecnico,
            valorMaoDeObra
        );

        orc.setId((int) orcId);
        orc.setStatus(Orçamento.Status.valueOf(rs.getString("orc_status")));
        orc.setDataCriacao(toLocalDateTime(rs.getTimestamp("data_criacao")));
        orc.setDataDecisao(toLocalDateTime(rs.getTimestamp("data_decisao")));
        orc.setObservacoes(rs.getString("observacoes"));
        orc.setMão_de_obra(valorMaoDeObra);

        // Carrega os itens reais reutilizando a conexão já aberta
        orc.setItens(buscarItensPorOrcamentoId(conn, (int) orcId));

        return orc;
    }

    private List<ItemOrcamento> buscarItensPorOrcamentoId(Connection conn, int orcamentoId) throws SQLException {

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

    private Equipamento buscarEquipamentoPorId(Connection conn, long id) throws SQLException {

        String sql = "SELECT id, nome, modelo, documento_cliente, ativo FROM equipamento WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Equipamento(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("modelo"),
                        rs.getString("documento_cliente"),
                        rs.getBoolean("ativo")
                    );
                }
            }
        }

        throw new SQLException("Equipamento não encontrado para a OS");
    }

    private Timestamp toTimestamp(LocalDateTime dt) {
        return dt == null ? null : Timestamp.valueOf(dt);
    }

    private LocalDateTime toLocalDateTime(Timestamp ts) {
        return ts == null ? null : ts.toLocalDateTime();
    }
}