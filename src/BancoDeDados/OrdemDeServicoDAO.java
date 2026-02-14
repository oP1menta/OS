package BancoDeDados;

import Classe.OrdemDeServico;
import Classe.Equipamento;
import Classe.Orçamento;
import dominio.enums.StatusOrdemServico;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdemDeServicoDAO {

    private final EquipamentoDAO equipamentoDAO = new EquipamentoDAO();

    /* ==============================
       CREATE
       ============================== */
    public void salvar(OrdemDeServico os) {

        String sql = """
            INSERT INTO ordem_de_servico
            (equipamento_id, orcamento_id, descricao_problema,
             data_abertura, data_fechamento_previsto, status)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, os.getEquipamento().getId());
            stmt.setLong(2, os.getOrcamentoAprovado().getId());
            stmt.setString(3, os.getDescricaoProblema());
            stmt.setTimestamp(4, Timestamp.valueOf(os.getDataAbertura()));
            stmt.setTimestamp(5, Timestamp.valueOf(os.getDataFechamentoPrevisto()));
            stmt.setString(6, os.getStatus().name());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    os.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
        	 e.printStackTrace();
            throw new RuntimeException("Erro ao salvar Ordem de Serviço", e);
        } finally {
            conexaoObj.fecharConexao();
        }
    }

    /* ==============================
       UPDATE
       ============================== */
    public void atualizar(OrdemDeServico os) {

        String sql = """
            UPDATE ordem_de_servico
            SET observacoes_tecnicas = ?,
                data_inicio = ?,
                data_fechamento_real = ?,
                status = ?
            WHERE id = ?
        """;

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, os.getObservacoesTecnicas());
            stmt.setTimestamp(2, toTimestamp(os.getDataInicio()));
            stmt.setTimestamp(3, toTimestamp(os.getDataFechamentoReal()));
            stmt.setString(4, os.getStatus().name());
            stmt.setLong(5, os.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
        	 e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar Ordem de Serviço", e);
        } finally {
            conexaoObj.fecharConexao();
        }
    }

    /* ==============================
       READ
       ============================== */
    public OrdemDeServico buscarPorId(
            Long id,
            Equipamento equipamento,
            Orçamento orcamento) {

        String sql = "SELECT * FROM ordem_de_servico WHERE id = ?";

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs, equipamento, orcamento);
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

    public List<OrdemDeServico> listarPorEquipamento(
            Equipamento equipamento,
            Orçamento orcamento) {

        List<OrdemDeServico> lista = new ArrayList<>();

        String sql = """
            SELECT * FROM ordem_de_servico
            WHERE equipamento_id = ?
        """;

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, equipamento.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs, equipamento, orcamento));
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

    /* ==============================
       MAPEAMENTO
       ============================== */
    private OrdemDeServico mapear(
            ResultSet rs,
            Equipamento equipamento,
            Orçamento orcamento) throws SQLException {

    	OrdemDeServico os = new OrdemDeServico(
    	        rs.getLong("id"),
    	        equipamento,
    	        rs.getString("descricao_problema"),
    	        rs.getTimestamp("data_abertura").toLocalDateTime(),
    	        rs.getTimestamp("data_fechamento_previsto").toLocalDateTime(),
    	        rs.getTimestamp("data_conclusao") != null
    	                ? rs.getTimestamp("data_conclusao").toLocalDateTime()
    	                : null,
    	        StatusOrdemServico.valueOf(rs.getString("status"))
    	);

        os.setId(rs.getLong("id"));
        os.setDataAbertura(rs.getTimestamp("data_abertura").toLocalDateTime());
        os.setDataInicio(toLocalDateTime(rs.getTimestamp("data_inicio")));
        os.setDataFechamcentoReal(toLocalDateTime(rs.getTimestamp("data_fechamento_real")));
        os.setObservacoesTecnicas(rs.getString("observacoes_tecnicas"));
        os.setStatus(StatusOrdemServico.valueOf(rs.getString("status")));

        return os;
    }

    
    private Timestamp toTimestamp(LocalDateTime dt) {
        return dt == null ? null : Timestamp.valueOf(dt);
    }

    private LocalDateTime toLocalDateTime(Timestamp ts) {
        return ts == null ? null : ts.toLocalDateTime();
    }

    public List<OrdemDeServico> listarTodos() {

        List<OrdemDeServico> lista = new ArrayList<>();

        String sql = """
            SELECT
                os.id,
                os.descricao_problema,
                os.data_abertura,
                os.data_fechamento_previsto,
                os.data_inicio,
                os.data_fechamento_real,
                os.observacoes_tecnicas,
                os.status,

                e.id   AS equipamento_id,
                e.nome,
                e.modelo,
                e.documento_cliente,
                e.ativo

            FROM ordem_de_servico os
            JOIN equipamento e ON e.id = os.equipamento_id
        """;

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Equipamento equipamento = new Equipamento(
                        rs.getInt("equipamento_id"),
                        rs.getString("nome"),
                        rs.getString("modelo"),
                        rs.getString("documento_cliente"),
                        rs.getBoolean("ativo")
                );

                // 🔥 CRIA USANDO O CONSTRUTOR REAL
                OrdemDeServico os = new OrdemDeServico(
                         null, equipamento,
                        rs.getString("descricao_problema"),
                        rs.getTimestamp("data_fechamento_previsto").toLocalDateTime(), null, null, null
                );

                // 🔥 RECONSTRUÇÃO DE ESTADO (carregamento ≠ criação)
                os.setId(rs.getLong("id"));
                os.setDataAbertura(rs.getTimestamp("data_abertura").toLocalDateTime());
                os.setDataInicio(toLocalDateTime(rs.getTimestamp("data_inicio")));
                os.setDataFechamcentoReal(toLocalDateTime(rs.getTimestamp("data_fechamento_real")));
                os.setObservacoesTecnicas(rs.getString("observacoes_tecnicas"));
                os.setStatus(StatusOrdemServico.valueOf(rs.getString("status")));

                lista.add(os);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao listar ordens de serviço", e);
        } finally {
            conexaoObj.fecharConexao();
        }

        return lista;
    }
}