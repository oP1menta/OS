package BancoDeDados;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conect {

    private final String driver = "org.postgresql.Driver";
    private final String user = "postgres";
    private final String senha = "GodHypnos.66";
    private final String url = "jdbc:postgresql://localhost:5432/ProjetoIntegradorBD";

    private Connection con;

    public conect() {
        try {
            Class.forName(driver);
            this.con = DriverManager.getConnection(url, user, senha);
            System.out.println("Conexão realizada com sucesso.");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao conectar com o banco de dados", e);
        }
    }

    public Connection getConexao() {
        return con;
    }

    public void fecharConexao() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao fechar conexão", e);
        }
    }
}
