package dominio.enums;

import BancoDeDados.OrdemDeServicoDAO;
import Classe.OrdemDeServico;

import java.util.List;

public class VerificadorPendenciaOS {

    private final OrdemDeServicoDAO osDAO;

    public VerificadorPendenciaOS(OrdemDeServicoDAO osDAO) {
        this.osDAO = osDAO;
    }

    public void verificarPendencias() {

        List<OrdemDeServico> ordens = osDAO.listarTodas();

        for (OrdemDeServico os : ordens) {

            if (os.estaAtrasada()) {
             //   os.verificarEAtualizarPendencia();
                osDAO.atualizar(os);
            }
        }
    }
}