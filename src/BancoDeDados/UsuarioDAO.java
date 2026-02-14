package BancoDeDados;

import Classe.Usuario;
import java.sql.*;

public class UsuarioDAO {

    public void salvar(Usuario usuario) {
        String sql = """
            INSERT INTO usuarios (login, senha_hash)
            VALUES (?, ?)
            ON CONFLICT (login)
            DO UPDATE SET senha_hash = EXCLUDED.senha_hash
        """;

        conect conexaoObj = new conect();
        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getLogin());
            stmt.setString(2, usuario.getSenhaHash());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário: " + e.getMessage());
        } finally {
            conexaoObj.fecharConexao();
        }
    }

    public Usuario buscarPorLogin(String login) {
        String sql = "SELECT login, senha_hash FROM usuarios WHERE login = ?";

        conect conexaoObj = new conect();
        try (Connection conn = conexaoObj.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getString("login"),
                        rs.getString("senha_hash"),
                        true
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário: " + e.getMessage());
        } finally {
            conexaoObj.fecharConexao();
        }
        return null;
    }
}
