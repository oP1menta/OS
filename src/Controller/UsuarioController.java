package Controller;

import BancoDeDados.UsuarioDAO;
import Classe.Usuario;

public class UsuarioController {

    private UsuarioDAO usuarioDAO;

    private static final String USUARIO_COMUM = "contrimaq_adm";
    private static final String USUARIO_OCULTO = "contrimaq_dev";

    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
        inicializarUsuarios();
    }

    private void inicializarUsuarios() {

        if (usuarioDAO.buscarPorLogin(USUARIO_COMUM) == null) {
            usuarioDAO.salvar(new Usuario(USUARIO_COMUM, "empresa123"));
        }

        if (usuarioDAO.buscarPorLogin(USUARIO_OCULTO) == null) {
            usuarioDAO.salvar(new Usuario(USUARIO_OCULTO, "dev123456"));
        }
    }

    public boolean login(String login, String senha) {
        Usuario usuario = usuarioDAO.buscarPorLogin(login);
        return usuario != null && usuario.autenticar(senha);
    }

    public void recuperarSenha(String login, String novaSenha) {
        Usuario usuario = usuarioDAO.buscarPorLogin(login);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        usuario.recuperarSenha(novaSenha);
        usuarioDAO.salvar(usuario);
    }
}
