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
                rs.getString("documento")
        );

        cliente.setAtivo(rs.getBoolean("ativo"));
        return cliente;
    }


    public void salvarcliente(Cliente cliente) {

        String sql = """
            INSERT INTO cliente (documento, nome, telefone, email, ativo)
            VALUES (?, ?, ?, ?, TRUE)
        """;

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getDocumento());
            stmt.setString(2, cliente.getNome());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar cliente", e);
        }
    }

    //UPDATE 

    public void atualizar(Cliente cliente) {

        String sql = """
            UPDATE cliente
            SET nome = ?, telefone = ?, email = ?
            WHERE documento = ?
        """;

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getDocumento());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente", e);
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
        }

        return clientes;
    }


    public void desativar(String documento) {

        String sql = "UPDATE cliente SET ativo = FALSE WHERE documento = ?";

        conect conexaoObj = new conect();

        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, documento);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao desativar cliente", e);
        }
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
        }
    }
}
