package Controller;

import BancoDeDados.ClienteDAO;
import Classe.Cliente;
import Factory.Fac;
import Exception.InvalidArgumentException;

import java.util.List;

public class ClienteController {

    private ClienteDAO clienteDAO;
    private Fac factory;

    

   
    public ClienteController() {
        this.clienteDAO = new ClienteDAO();
        this.factory = Fac.getInstancia();
    }

   

    public void cadastrarClienteFisico(String nome, String telefone, String email, String cpf)
            throws InvalidArgumentException {

        Cliente cliente = factory.criarCliente(nome, telefone, email, cpf);
        clienteDAO.salvarcliente(cliente);
    }

    public void cadastrarClienteJuridico(String nome, String telefone, String email, String cnpj)
            throws InvalidArgumentException {

        Cliente cliente = factory.criarCliente(nome, telefone, email, cnpj);
        clienteDAO.salvarcliente(cliente);
    }

   

    public Cliente buscarClientePorDocumento(String documento) {
        return clienteDAO.buscarPorDocumento(documento);
    }

    public List<Cliente> listarClientesPorNome(String nome) {
        return clienteDAO.listarPorNome(nome);
    }

   

    public void deletarCliente(String documento) {
        clienteDAO.deletarCliente(documento);
    }
    
    
    public void desativarCliente(String documento) {
        clienteDAO.desativar(documento);
    }
    
    
}
