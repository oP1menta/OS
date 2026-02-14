package Controller;

import BancoDeDados.EquipamentoDAO;
import Classe.Equipamento;
import Exception.InvalidArgumentException;

import java.sql.SQLException;
import java.util.List;

public class EquipamentoController {

    private final EquipamentoDAO equipamentoDAO;

    // Construtor padrão (alinhado com ClienteController)
    public EquipamentoController() {
        this.equipamentoDAO = new EquipamentoDAO();
    }

   

    public void cadastrarEquipamento(String nome, String modelo, String documentoCliente)
            throws SQLException, InvalidArgumentException {

        Equipamento equipamento = new Equipamento(nome, modelo, documentoCliente);
        equipamentoDAO.salvar(equipamento);
    }

 

    public List<Equipamento> listarTodosEquipamentos() throws SQLException {
        return equipamentoDAO.listarTodos();
    }

    public List<Equipamento> listarEquipamentosPorCliente(String documentoCliente)
            throws SQLException {

        return equipamentoDAO.buscarPorDocumentoCliente(documentoCliente);
    }

   

    public void deletarEquipamento(int id) throws SQLException {
        equipamentoDAO.deletar(id);
    }
}
