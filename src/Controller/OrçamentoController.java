package Controller;

import BancoDeDados.OrçamentoDAO;
import Classe.Orçamento;
import Classe.Tecnico;

import java.math.BigDecimal;
import java.util.List;

public class OrçamentoController {

    private final OrçamentoDAO orcamentoDAO = new OrçamentoDAO();

  
    public Orçamento criar(
            String peca,
            BigDecimal valor,
            String tipoPagamento,
            Tecnico tecnicoResponsavel
    ) {

     
        Orçamento orcamento = new Orçamento(
                peca,
                valor,
                tipoPagamento,
                tecnicoResponsavel
        );

        orcamentoDAO.salvar(orcamento);
        return orcamento;
    }

  
    public void aprovar(int idOrcamento, Tecnico tecnico) {

        Orçamento orcamento = orcamentoDAO.buscarPorId((long) idOrcamento, tecnico);

        if (orcamento == null)
            throw new IllegalArgumentException("Orçamento não encontrado");

        orcamento.aprovar();
        orcamentoDAO.atualizarEstado(orcamento);
    }

   
    public void reprovar(int idOrcamento, Tecnico tecnico) {

        

        Orçamento orcamento = orcamentoDAO.buscarPorId((long) idOrcamento, tecnico);

        if (orcamento == null)
            throw new IllegalArgumentException("Orçamento não encontrado");

        orcamento.reprovar();
        orcamentoDAO.atualizarEstado(orcamento);
    }

  
    public List<Orçamento> listarPorTecnico(Tecnico tecnico) {

        if (tecnico == null)
            throw new IllegalArgumentException("Técnico obrigatório");

        return orcamentoDAO.listarPorTecnico(tecnico);
    }

    
    public Orçamento buscarPorId(int idOrcamento, Tecnico tecnico) {

        return orcamentoDAO.buscarPorId((long) idOrcamento, tecnico);
    }
}