package BancoDeDados;

import Classe.OrdemDeServico;
import Classe.Equipamento;
import dominio.enums.StatusOrdemServico;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdemDeServicoDAO {

    private final EquipamentoDAO equipamentoDAO;

 

    public OrdemDeServicoDAO() {
        this.equipamentoDAO = new EquipamentoDAO();
    }

    public OrdemDeServicoDAO(EquipamentoDAO equipamentoDAO) {
        this.equipamentoDAO = equipamentoDAO;
    }



    public void salvar(OrdemDeServico os) {

        String sql = """
            INSERT INTO ordem_servico
            (equipamento_id, descricao_problema, observacoes_tecnicas,
             data_abertura, data_inicio, data_fechamento, status)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        conect conexaoObj = new conect();

        try (Connection con = conexaoObj.getConexao();
             PreparedStatement stmt =
                     con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, os.getEquipamento().getId());
            stmt.setString(2, os.getDescricaoProblema());
            stmt.setString(3, os.getObservacoesTecnicas());
            stmt.setTimestamp(4, Timestamp.valueOf(os.getDataAbertura()));
            stmt.setTimestamp(5, toTimestamp(os.getDataInicio()));
            stmt.setTimestamp(6, toTimestamp(os.getDataFechamentoPrevisto()));
            stmt.setString(7, os.getStatus().name());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    setField(os, "id", rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar Ordem de Serviço", e);
        }
    }

 
    public void atualizar(OrdemDeServico os) {

        String sql = """
            UPDATE ordem_servico SET
                observacoes_tecnicas = ?,
                data_inicio = ?,
                data_fechamento = ?,
                status = ?
            WHERE id = ?
        """;

        conect conexaoObj = new conect();

        try (Connection con = conexaoObj.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, os.getObservacoesTecnicas());
            stmt.setTimestamp(2, toTimestamp(os.getDataInicio()));
            stmt.setTimestamp(3, toTimestamp(os.getDataFechamentoPrevisto()));
            stmt.setString(4, os.getStatus().name());
            stmt.setLong(5, os.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar Ordem de Serviço", e);
        }
    }

   

    public OrdemDeServico buscarPorId(Long id) {

        String sql = "SELECT * FROM ordem_servico WHERE id = ?";

        conect conexaoObj = new conect();

        try (Connection con = conexaoObj.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    Equipamento equipamento =
                            equipamentoDAO.buscarPorId(rs.getLong("equipamento_id"));

                    if (equipamento != null) {
                        return mapear(rs, equipamento);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar Ordem de Serviço por ID", e);
        }

        return null;
    }

  

    public List<OrdemDeServico> listarPorEquipamento(Equipamento equipamento) {

        String sql = "SELECT * FROM ordem_servico WHERE equipamento_id = ?";
        List<OrdemDeServico> lista = new ArrayList<>();

        conect conexaoObj = new conect();

        try (Connection con = conexaoObj.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setLong(1, equipamento.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs, equipamento));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar Ordens de Serviço", e);
        }

        return lista;
    }

    public List<OrdemDeServico> listarTodas() {

        String sql = "SELECT * FROM ordem_servico";
        List<OrdemDeServico> lista = new ArrayList<>();

        conect conexaoObj = new conect();

        try (Connection con = conexaoObj.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Equipamento equipamento =
                        equipamentoDAO.buscarPorId(rs.getLong("equipamento_id"));

                if (equipamento != null) {
                    lista.add(mapear(rs, equipamento));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar Ordens de Serviço", e);
        }

        return lista;
    }

   

    private OrdemDeServico mapear(ResultSet rs, Equipamento equipamento)
            throws SQLException {

        OrdemDeServico os = new OrdemDeServico(
                equipamento,
                rs.getString("descricao_problema"), null
        );

        setField(os, "id", rs.getLong("id"));
        setField(os, "dataAbertura",
                rs.getTimestamp("data_abertura").toLocalDateTime());
        setField(os, "dataInicio",
                toLocalDateTime(rs.getTimestamp("data_inicio")));
        setField(os, "dataFechamento",
                toLocalDateTime(rs.getTimestamp("data_fechamento")));
        setField(os, "observacoesTecnicas",
                rs.getString("observacoes_tecnicas"));

        String statusDb = rs.getString("status");
        if (statusDb != null) {
            setField(os, "status", StatusOrdemServico.valueOf(statusDb));
        }

        return os;
    }

    private Timestamp toTimestamp(LocalDateTime dateTime) {
        return dateTime == null ? null : Timestamp.valueOf(dateTime);
    }

    private LocalDateTime toLocalDateTime(Timestamp ts) {
        return ts == null ? null : ts.toLocalDateTime();
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao mapear campo '" + fieldName + "'", e
            );
        }
    }
}
