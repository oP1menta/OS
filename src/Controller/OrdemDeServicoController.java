package Controller;

import BancoDeDados.OrdemDeServicoDAO;
import Classe.Equipamento;
import Classe.OrdemDeServico;
import Classe.Orçamento;
import Factory.Fac;

import java.time.LocalDateTime;
import java.util.List;

public class OrdemDeServicoController {

    private final OrdemDeServicoDAO ordemDeServicoDAO = new OrdemDeServicoDAO();
    private final Fac factory = Fac.getInstancia();

    /* ===================== CRIAÇÃO ===================== */

    public OrdemDeServico abrirOrdem(
            Equipamento equipamento,
            String descricaoProblema,
            LocalDateTime dataFechamentoPrevista
    ) {

        if (equipamento == null)
            throw new IllegalArgumentException("Equipamento obrigatório");

        OrdemDeServico os =
                factory.criarOrdemDeServico(
                        equipamento,
                        descricaoProblema,
                        dataFechamentoPrevista
                );

        ordemDeServicoDAO.salvar(os);
        return os;
    }

    /* ===================== FLUXO ===================== */

    public void iniciarOrdem(OrdemDeServico os, Orçamento orcamento) {

        if (os == null)
            throw new IllegalArgumentException("Ordem de serviço inválida");

        if (orcamento == null)
            throw new IllegalArgumentException("Orçamento obrigatório");

        if (!orcamento.getOrdemDeServico().getId().equals(os.getId()))
            throw new IllegalStateException("Orçamento não pertence a esta OS");

        if (!orcamento.estaAprovado())
            throw new IllegalStateException("Orçamento precisa estar aprovado");

        os.iniciar(orcamento);
        ordemDeServicoDAO.atualizar(os);
    }

    public void concluirOrdem(OrdemDeServico os, String observacoesTecnicas) {

        if (os == null)
            throw new IllegalArgumentException("Ordem de serviço inválida");

        os.concluir(observacoesTecnicas);
        ordemDeServicoDAO.atualizar(os);
    }

    /* ===================== CONSULTAS ===================== */

    public OrdemDeServico buscarPorId(Long id) {

        if (id == null)
            throw new IllegalArgumentException("ID inválido");

        return ordemDeServicoDAO.buscarPorId(id);
    }

    public List<OrdemDeServico> listarTodas() {
        return ordemDeServicoDAO.listarTodas();
    }

    public List<OrdemDeServico> listarPorEquipamento(Equipamento equipamento) {

        if (equipamento == null)
            throw new IllegalArgumentException("Equipamento inválido");

        return ordemDeServicoDAO.listarPorEquipamento(equipamento);
    }
}