package BancoDeDados;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Classe.Cliente;
import Exception.InvalidArgumentException;
import Factory.Fac;

public class ClienteDAO {

    private Cliente criarClienteDoResultSet(ResultSet rs)
            throws SQLException, InvalidArgumentException {

        Fac factory = Fac.getInstancia();

        Cliente cliente = factory.criarCliente(
                rs.getString("nome"),
                rs.getString("telefone"),
                rs.getString("email"),
                rs.getString("cidade"),
                rs.getString("cep"),
                rs.getString("documento")
        );

        cliente.setAtivo(rs.getBoolean("ativo"));
        return cliente;
    }

    public void salvarcliente(Cliente cliente) {

        String sql = """
            INSERT INTO cliente (documento, nome, telefone, email, cidade, cep, ativo)
            VALUES (?, ?, ?, ?, ?, ?, TRUE)
        """;

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getDocumento());
            stmt.setString(2, cliente.getNome());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            stmt.setString(5, cliente.getCidade());
            stmt.setString(6, cliente.getCEP());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar cliente", e);
        } finally {
            if (conexaoObj != null) {
                conexaoObj.fecharConexao();
            }
        }
    }

    public void atualizar(Cliente cliente) {

        String sql = """
            UPDATE cliente
            SET nome = ?, telefone = ?, email = ?, cidade = ?, cep = ?
            WHERE documento = ?
        """;

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getCidade());
            stmt.setString(5, cliente.getCEP());
            stmt.setString(6, cliente.getDocumento());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente", e);
        } finally {
            if (conexaoObj != null) {
                conexaoObj.fecharConexao();
            }
        }
    }

    public Cliente buscarPorDocumento(String documento) {

        String sql = "SELECT * FROM cliente WHERE documento = ?";

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, documento);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarClienteDoResultSet(rs);
                }
            }

        } catch (SQLException | InvalidArgumentException e) {
            throw new RuntimeException("Erro ao buscar cliente", e);
        } finally {
            if (conexaoObj != null) {
                conexaoObj.fecharConexao();
            }
        }

        return null;
    }

    public List<Cliente> listarPorNome(String nome) {

        List<Cliente> clientes = new ArrayList<>();

        String sql = """
            SELECT * FROM cliente
            WHERE nome ILIKE ? AND ativo = TRUE
        """;

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(criarClienteDoResultSet(rs));
                }
            }

        } catch (SQLException | InvalidArgumentException e) {
            throw new RuntimeException("Erro ao listar clientes", e);
        } finally {
            if (conexaoObj != null) {
                conexaoObj.fecharConexao();
            }
        }

        return clientes;
    }

    public void deletarCliente(String documento) {

        String sql = "DELETE FROM cliente WHERE documento = ?";

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, documento);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir cliente", e);
        } finally {
            if (conexaoObj != null) {
                conexaoObj.fecharConexao();
            }
        }
    }
}