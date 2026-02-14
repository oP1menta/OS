package Classe;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Usuario {
    private final String login;
    private String senhaHash;

    public Usuario(String login, String senha) {
        if (login == null || login.isEmpty()) {
            throw new IllegalArgumentException("Login Obrigatorio");
        }
        this.login = login;
        definirSenha(senha);
    }

    public Usuario(String login, String senhaHash, boolean isHash) {
        this.login = login;
        if (isHash) {
            this.setSenhaHash (senhaHash)
            ;
        } else {
            definirSenha(senhaHash);
        }
    }
    
    public String getLogin() { return login; }
    public String getSenhaHash() { return senhaHash; }

    public boolean autenticar(String senhaInformada) {
        return senhaHash.equals(gerarHash(senhaInformada));
    }

    public void alterarSenha(String senhaAtual, String novaSenha) {
        if (!autenticar(senhaAtual)) {
            throw new IllegalArgumentException("Senha atual incorreta");
        }
        definirSenha(novaSenha);
    }

    public void recuperarSenha(String novaSenha) {
        definirSenha(novaSenha);
    }

    private void definirSenha(String senha) {
        if (senha == null || senha.length() < 6) {
            throw new IllegalArgumentException("Senha fraca (mínimo 6 caracteres)");
        }
        this.senhaHash = gerarHash(senha);
    }
    
    private String gerarHash(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(senha.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar Hash");
        }
    }
	public void setSenhaHash(String senhaHash) {
		this.senhaHash = senhaHash;
	}
}
