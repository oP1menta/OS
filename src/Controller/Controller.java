//package Controller;
//
//import java.sql.Connection;
//
//import BancoDeDados.conect;
//import BancoDeDados.ClienteDAO;
//import BancoDeDados.EquipamentoDAO;
//import BancoDeDados.OrdemDeServicoDAO;
//
//public class Controller {
//
//    private ClienteController clienteController;
//    private EquipamentoController equipamentoController;
//    private OrdemDeServicoController osController;
//    public Controller() {
//
//        conect conexao = new conect();
//        Connection connection = conexao.getConexao();
//
//        EquipamentoDAO equipamentoDAO =
//                new EquipamentoDAO(connection);
//
//        OrdemDeServicoDAO ordemDeServicoDAO =
//                new OrdemDeServicoDAO(connection, equipamentoDAO);
//
//        ClienteDAO clienteDAO = new ClienteDAO(connection);
//
//        this.clienteController =
//                new ClienteController(clienteDAO);
//
//        this.equipamentoController =
//                new EquipamentoController(equipamentoDAO);
//
//        this.osController =
//                new OrdemDeServicoController(ordemDeServicoDAO);
//    }
//
//
//    public ClienteController getClienteController() {
//        return clienteController;
//    }
//
//    public EquipamentoController getEquipamentoController() {
//        return equipamentoController;
//    }
//
//    public OrdemDeServicoController getOsController() {
//        return osController;
//    }
//}
