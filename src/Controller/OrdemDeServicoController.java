package Controller;

import BancoDeDados.OrdemDeServicoDAO;
import Classe.Equipamento;
import Classe.Orçamento;
import Classe.OrdemDeServico;

import java.time.LocalDateTime;
import java.util.List;

public class OrdemDeServicoController {

    private final OrdemDeServicoDAO osDAO = new OrdemDeServicoDAO();

 
    public OrdemDeServico abrir(Equipamento equipamento,String descricaoProblema, LocalDateTime dataFechamentoPrevisto ) {

        OrdemDeServico os = new OrdemDeServico(
               null  ,equipamento,
                descricaoProblema,
                dataFechamentoPrevisto, dataFechamentoPrevisto, dataFechamentoPrevisto, null
        );

        osDAO.salvar(os);
        return os;
    }

   
    public void iniciar(Long idOS,Orçamento orcamentoAprovado,Equipamento equipamento ) {

        OrdemDeServico os = osDAO.buscarPorId(
                idOS,
                equipamento,
                orcamentoAprovado
        );

        if (os == null)
            throw new IllegalArgumentException("OS não encontrada");

        os.iniciar(orcamentoAprovado);
        osDAO.atualizar(os);
    }

   
    public void concluir(
            Long idOS,
            String observacoesTecnicas,
            Equipamento equipamento,
            Orçamento orcamento
    ) {

        OrdemDeServico os = osDAO.buscarPorId(
                idOS,
                equipamento,
                orcamento
        );

        if (os == null)
            throw new IllegalArgumentException("OS não encontrada");

        os.concluir(observacoesTecnicas);
        osDAO.atualizar(os);
    }

    
    public OrdemDeServico buscarPorId(
            Long idOS,
            Equipamento equipamento,
            Orçamento orcamento
    ) {
        return osDAO.buscarPorId(idOS, equipamento, orcamento);
    }

    public List<OrdemDeServico> listarTodas() {
        return osDAO.listarTodos();
    }

    public List<OrdemDeServico> listarPorEquipamento(
            Equipamento equipamento,
            Orçamento orcamento
    ) {
        return osDAO.listarPorEquipamento(equipamento, orcamento);
    }
}