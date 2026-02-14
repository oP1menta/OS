package Controller;

import BancoDeDados.TecnicoDAO;
import Classe.Tecnico;

import java.time.LocalDate;
import java.util.List;

public class TecnicoController {

    private final TecnicoDAO tecnicoDAO = new TecnicoDAO();

    
    public Tecnico cadastrar(String nome,String documento,LocalDate dataAssociacao
    ) {
    	
        Tecnico tecnico = new Tecnico( nome, documento,dataAssociacao);

        tecnicoDAO.salvar(tecnico);
        return tecnico;
    }

    public Tecnico buscarPorId(int idTecnico) {

        Tecnico tecnico = tecnicoDAO.buscarPorId(idTecnico);

        if (tecnico == null)
            throw new IllegalArgumentException("Técnico não encontrado");

        return tecnico;
    }

    public List<Tecnico> listarTodos() {
        return tecnicoDAO.listar();
    }
}