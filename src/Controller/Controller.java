package Controller;

import BancoDeDados.ClienteDAO;
import BancoDeDados.EquipamentoDAO;
import BancoDeDados.OrdemDeServicoDAO;
import BancoDeDados.OrçamentoDAO;
import BancoDeDados.TecnicoDAO;
import BancoDeDados.UsuarioDAO;
import PDF.GeradorPDFOrcamento;
import PDF.GeradorPDFOS;

import Classe.*;
import Exception.InvalidArgumentException;
import Factory.Fac;
import dominio.enums.StatusOrdemServico;
import view.MainView;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Controller {

    private final UsuarioDAO usuarioDAO;
    private final ClienteDAO clienteDAO;
    private final Fac factory;
    private final EquipamentoDAO equipamentoDAO;
    private final TecnicoDAO tecnicoDAO;
    private final OrçamentoDAO orcamentoDAO;
    private final OrdemDeServicoDAO osDAO;

    private static final String USUARIO_COMUM  = "contrimaq_adm";
    private static final String USUARIO_OCULTO = "contrimaq_dev";

    public Controller() {
        this.usuarioDAO = new UsuarioDAO();
        inicializarUsuarios();

        this.clienteDAO     = new ClienteDAO();
        this.factory        = Fac.getInstancia();
        this.equipamentoDAO = new EquipamentoDAO();
        this.tecnicoDAO     = new TecnicoDAO();
        this.orcamentoDAO   = new OrçamentoDAO();
        this.osDAO          = new OrdemDeServicoDAO();

        wire();
    }

    private void wire() {

        MainView.setLoginHandler(this::login);

        MainView.setOrcamentosAprovadosProvider(this::listarAprovado);
        MainView.setTecnicosProvider(this::listarTodos);
        MainView.setOrcamentosPorTecnicoProvider(this::listarPorTecnico);

        MainView.setDashboardProvider(this::listarTodas);

        MainView.setEquipamentosProvider(this::listarTodosEquipamentos);

        MainView.setOsProvider(eq -> listarPorEquipamento(eq, null));

        MainView.setCriarOsHandler(this::abrir);

        MainView.setCriarOrcamentoHandler(this::criar);
        MainView.setAprovarOrcamentoHandler((id, tec) -> aprovar((int) id, tec));
        MainView.setReprovarOrcamentoHandler((id, tec) -> reprovar((int) id, tec));

        MainView.setCadastrarTecnicoHandler(this::cadastrar);

        MainView.setSalvarClienteHandler((nome, telefone, email, cidade, cep, documento) ->
            cadastrarCliente(nome, telefone, email, cidade, cep, documento)
        );
        MainView.setClientesProvider(() -> listarClientesPorNome(""));

        MainView.setSalvarEquipamentoHandler((nome, modelo, documentoCliente) ->
            cadastrarEquipamento(nome, modelo, documentoCliente)
        );

        MainView.setIniciarOsHandler((idOS, orc, eq) -> iniciar(idOS, orc, eq));
        MainView.setConcluirOsHandler((idOS, obs, eq, orc) -> concluir(idOS, obs, eq, orc));

        MainView.setDeletarClienteHandler(documento -> deletarCliente(documento));
        MainView.setDeletarEquipamentoHandler(id -> deletarEquipamento(id));
        MainView.setDeletarTecnicoHandler(id -> deletarTecnico(id));

        MainView.setExportarOsPdfHandler((orc, ord) -> gerarPdfOS(orc, ord));
        MainView.setExportarOrcamentoPdfHandler(orc -> gerarPdfOrcamento(orc));
    }

    private void inicializarUsuarios() {
        if (usuarioDAO.buscarPorLogin(USUARIO_COMUM) == null)
            usuarioDAO.salvar(new Usuario(USUARIO_COMUM, "empresa123"));
        if (usuarioDAO.buscarPorLogin(USUARIO_OCULTO) == null)
            usuarioDAO.salvar(new Usuario(USUARIO_OCULTO, "dev123456"));
    }

    public boolean login(String login, String senha) {
        Usuario usuario = usuarioDAO.buscarPorLogin(login);
        return usuario != null && usuario.autenticar(senha);
    }

    public void recuperarSenha(String login, String novaSenha) {
        Usuario usuario = usuarioDAO.buscarPorLogin(login);
        if (usuario == null)
            throw new IllegalArgumentException("Usuário não encontrado");
        usuario.recuperarSenha(novaSenha);
        usuarioDAO.salvar(usuario);
    }

    public void cadastrarCliente(String nome, String telefone, String email,
                                  String Cidade, String CEP, String Documento)
            throws InvalidArgumentException {
        Cliente cliente = factory.criarCliente(nome, telefone, email, Cidade, CEP, Documento);
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

    public void cadastrarEquipamento(String nome, String modelo, String documentoCliente)
            throws SQLException, InvalidArgumentException {
        Equipamento equipamento = factory.criarEquipamento(nome, modelo, documentoCliente);
        equipamentoDAO.salvar(equipamento);
    }

    public List<Equipamento> listarTodosEquipamentos() throws SQLException {
        return equipamentoDAO.listarTodos();
    }

    public List<Equipamento> listarEquipamentosPorCliente(String documentoCliente) throws SQLException {
        return equipamentoDAO.buscarPorDocumentoCliente(documentoCliente);
    }

    public void deletarEquipamento(int id) throws SQLException {
        equipamentoDAO.deletar(id);
    }

    public Tecnico cadastrar(String nome, String documento, LocalDate dataAssociacao) {
        Tecnico tecnico = factory.criarTecnico(nome, documento, dataAssociacao);
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

    // ─── Orçamento ─────────────────────────────────────────────────────────────

    public void criar(List<ItemOrcamento> itens,
                      String tipoPagamento,
                      Tecnico tecnico,
                      BigDecimal maodeobra) {
        Orçamento orcamento = factory.criarOrcamento(itens, tipoPagamento, tecnico, maodeobra);
        orcamentoDAO.salvar(orcamento);
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

    public List<Orçamento> listarAprovado() {
        return orcamentoDAO.listarAprovados();
    }

    // ─── Ordem de Serviço ──────────────────────────────────────────────────────

    public OrdemDeServico abrir(Equipamento equipamento, String descricaoProblema,
                                 LocalDate dataFechamentoPrevisto) {

        if (equipamento == null)
            throw new IllegalArgumentException("Selecione um equipamento");
        if (descricaoProblema == null || descricaoProblema.isBlank())
            throw new IllegalArgumentException("Informe a descrição do problema");
        if (dataFechamentoPrevisto == null)
            throw new IllegalArgumentException("Selecione a data prevista");

        LocalDateTime dataAbertura = LocalDateTime.now();
        LocalDateTime dataPrevista = dataFechamentoPrevisto.atTime(LocalTime.of(23, 59));

        OrdemDeServico os = new OrdemDeServico(
            null, equipamento, descricaoProblema,
            dataAbertura, dataPrevista, null,
            StatusOrdemServico.PENDENTE
        );

        os.setOrcamentoAprovado(null);
        osDAO.salvar(os);
        return os;
    }

    public void iniciar(Long idOS, Orçamento orcamentoAprovado, Equipamento equipamento) {
        OrdemDeServico os = osDAO.buscarPorId(idOS, equipamento, orcamentoAprovado);
        if (os == null)
            throw new IllegalArgumentException("OS não encontrada");
        os.iniciar(orcamentoAprovado);
        osDAO.atualizar(os);
    }

    public void iniciar(long idOS, Orçamento orcamentoAprovado, Equipamento equipamento) {
        iniciar(Long.valueOf(idOS), orcamentoAprovado, equipamento);
    }

    public void concluir(Long idOS, String observacoesTecnicas,
                          Equipamento equipamento, Orçamento orcamento) {
        OrdemDeServico os = osDAO.buscarPorId(idOS, equipamento, orcamento);
        if (os == null)
            throw new IllegalArgumentException("OS não encontrada");
        os.concluir(observacoesTecnicas);
        osDAO.atualizar(os);
    }

    public void concluir(long idOS, String observacoesTecnicas,
                          Equipamento equipamento, Orçamento orcamento) {
        concluir(Long.valueOf(idOS), observacoesTecnicas, equipamento, orcamento);
    }

    public OrdemDeServico buscarPorId(Long idOS, Equipamento equipamento, Orçamento orcamento) {
        return osDAO.buscarPorId(idOS, equipamento, orcamento);
    }

    public List<OrdemDeServico> listarTodas() {
        return osDAO.listarTodos();
    }

    public List<OrdemDeServico> listarPorEquipamento(Equipamento equipamento, Orçamento orcamento) {
        return osDAO.listarPorEquipamento(equipamento, orcamento);
    }

    // ─── PDF ───────────────────────────────────────────────────────────────────

    public void gerarPdfOS(Orçamento orc, OrdemDeServico os) {
        if (os == null)
            throw new IllegalArgumentException("OS não encontrada");
        if (os.getEquipamento() == null)
            throw new IllegalArgumentException("A OS não possui equipamento vinculado");

        String documentoCliente = os.getEquipamento().getDocumentoCliente();
        if (documentoCliente == null || documentoCliente.isBlank())
            throw new IllegalArgumentException("O equipamento não possui documento do cliente");

        Cliente cliente = clienteDAO.buscarPorDocumento(documentoCliente);
        if (cliente == null)
            throw new IllegalArgumentException("Cliente não encontrado para o documento: " + documentoCliente);

        GeradorPDFOS.gerarOS(orc, os, cliente);
    }

    public void gerarPdfOrcamento(Orçamento orcamento) {
        if (orcamento == null)
            throw new IllegalArgumentException("Orçamento inválido");
        GeradorPDFOrcamento.gerarOrcamento(orcamento);
    }

    public void deletarTecnico(int id) throws SQLException {
        tecnicoDAO.deletar(id);
    }
}