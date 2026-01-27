package Controller;

import BancoDeDados.OrçamentoDAO;
import Classe.Orçamento;
import Classe.OrdemDeServico;

import java.math.BigDecimal;
import java.util.List;

public class OrçamentoController {

    private final OrçamentoDAO orcamentoDAO = new OrçamentoDAO();


    public Orçamento criar(
            String peca,
            BigDecimal valor,
            String tipoPagamento,
            OrdemDeServico os) {

        if (os == null)
            throw new IllegalArgumentException("OS obrigatória");

        Orçamento orcamento =
                new Orçamento(peca, valor, tipoPagamento, os);

        orcamentoDAO.salvar(orcamento);
        return orcamento;
    }

   
    public void aprovar(Long idOrcamento, OrdemDeServico os) {

        Orçamento orcamento =
                orcamentoDAO.buscarPorId(idOrcamento, os);

        if (orcamento == null)
            throw new IllegalArgumentException("Orçamento não encontrado");

        orcamento.aprovar();               
        orcamentoDAO.atualizarStatus(orcamento);

        os.iniciar(orcamento);            
    }

   
    public void reprovar(Long idOrcamento, OrdemDeServico os) {

        Orçamento orcamento =
                orcamentoDAO.buscarPorId(idOrcamento, os);

        if (orcamento == null)
            throw new IllegalArgumentException("Orçamento não encontrado");

        orcamento.reprovar();
        orcamentoDAO.atualizarStatus(orcamento);
    }

    /* =========================
       LISTAR
       ========================= */
    public List<Orçamento> listarPorOS(OrdemDeServico os) {
        return orcamentoDAO.listarPorOrdemServico(os);
    }
}