package view;

import Classe.*;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.stage.Modality;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javafx.util.StringConverter;

public class MainView extends Application {

    private BorderPane root;
    private Stage stage;

    private final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static EquipamentosProvider EQUIPAMENTOS_PROVIDER;
    private static OsProvider OS_PROVIDER;
    private static OrcamentosAprovadosProvider ORCAMENTOS_APROVADOS_PROVIDER;
    private static IniciarOsHandler INICIAR_OS_HANDLER;
    private static ConcluirOsHandler CONCLUIR_OS_HANDLER;
    private static SalvarEquipamentoHandler SALVAR_EQUIPAMENTO_HANDLER;
    private static CriarOsHandler CRIAR_OS_HANDLER;
    private static LoginHandler LOGIN_HANDLER;
    private static DashboardDataProvider DASHBOARD_PROVIDER;
    private static TecnicosProvider TECNICOS_PROVIDER;
    private static OrcamentosPorTecnicoProvider ORCAMENTOS_PROVIDER;
    private static CriarOrcamentoHandler CRIAR_ORCAMENTO_HANDLER;
    private static AprovarOrcamentoHandler APROVAR_ORCAMENTO_HANDLER;
    private static ReprovarOrcamentoHandler REPROVAR_ORCAMENTO_HANDLER;
    private static ClientesProvider CLIENTES_PROVIDER;
    private static SalvarClienteHandler SALVAR_CLIENTE_HANDLER;
    private static DeletarClienteHandler DELETAR_CLIENTE_HANDLER;
    private static DeletarEquipamentoHandler DELETAR_EQUIPAMENTO_HANDLER;
    private static CadastrarTecnicoHandler CADASTRAR_TECNICO_HANDLER;
    private static DeletarTecnicoHandler DELETAR_TECNICO_HANDLER;
    private static ExportarOrcamentoPdfHandler EXPORTAR_ORCAMENTO_PDF_HANDLER;
    private static ExportarOsPdfHandler EXPORTAR_OS_PDF_HANDLER;
    private static OrcamentosPorEquipamentoProvider ORCAMENTOS_POR_EQUIPAMENTO_PROVIDER;
    private static EquipamentosPorClienteProvider EQUIPAMENTOS_POR_CLIENTE_PROVIDER;

    @FunctionalInterface public interface LoginHandler { boolean autenticar(String login, String senha) throws Exception; }
    @FunctionalInterface public interface DashboardDataProvider { List<OrdemDeServico> carregarOrdens() throws Exception; }
    @FunctionalInterface public interface EquipamentosProvider { List<Equipamento> listarEquipamentos() throws Exception; }
    @FunctionalInterface public interface OsProvider { List<OrdemDeServico> listarOsPorEquipamento(Equipamento equipamento) throws Exception; }
    @FunctionalInterface public interface OrcamentosAprovadosProvider { List<Orçamento> listarOrcamentosAprovados() throws Exception; }
    @FunctionalInterface public interface IniciarOsHandler { void iniciar(long osId, Orçamento orcamentoAprovado, Equipamento equipamento) throws Exception; }
    @FunctionalInterface public interface ConcluirOsHandler { void concluir(long osId, String observacoes, Equipamento equipamento, Orçamento orcamentoAprovado) throws Exception; }
    @FunctionalInterface public interface CriarOsHandler { void criar(Equipamento equipamento, String descricaoProblema, LocalDate dataFechamentoPrevisto) throws Exception; }
    @FunctionalInterface public interface TecnicosProvider { List<Tecnico> listarTecnicos() throws Exception; }
    @FunctionalInterface public interface OrcamentosPorTecnicoProvider { List<Orçamento> listarPorTecnico(Tecnico tecnico) throws Exception; }
    @FunctionalInterface public interface CriarOrcamentoHandler { void criar(List<ItemOrcamento> itens, String pagamento, Tecnico tecnico, BigDecimal valorMaoDeObra) throws Exception; }
    @FunctionalInterface public interface AprovarOrcamentoHandler { void aprovar(long id, Tecnico tecnico) throws Exception; }
    @FunctionalInterface public interface ReprovarOrcamentoHandler { void reprovar(long id, Tecnico tecnico) throws Exception; }
    @FunctionalInterface public interface ClientesProvider { List<Cliente> listarClientes() throws Exception; }
    @FunctionalInterface public interface SalvarEquipamentoHandler { void salvar(String nome, String modelo, String documentoCliente) throws Exception; }
    @FunctionalInterface public interface SalvarClienteHandler { void salvar(String nome, String telefone, String email, String cidade, String cep, String documento) throws Exception; }
    @FunctionalInterface public interface DeletarClienteHandler { void deletar(String documento) throws Exception; }
    @FunctionalInterface public interface DeletarEquipamentoHandler { void deletar(int equipamentoId) throws Exception; }
    @FunctionalInterface public interface CadastrarTecnicoHandler { void cadastrar(String nome, String documento, LocalDate dataAssociacao) throws Exception; }
    @FunctionalInterface public interface DeletarTecnicoHandler { void deletar(int id) throws Exception; }
    @FunctionalInterface public interface ExportarOrcamentoPdfHandler { void exportar(Orçamento orcamento, File destino) throws Exception; }
    @FunctionalInterface public interface ExportarOsPdfHandler { void exportar(Orçamento orcamento, OrdemDeServico ordemDeServico, File destino) throws Exception; }
    @FunctionalInterface public interface OrcamentosPorEquipamentoProvider { List<Orçamento> listarPorEquipamento(Equipamento equipamento) throws Exception; }
    @FunctionalInterface public interface EquipamentosPorClienteProvider { List<Equipamento> listarPorCliente(Cliente cliente) throws Exception; }

    public static void setLoginHandler(LoginHandler handler) { LOGIN_HANDLER = handler; }
    public static void setDashboardProvider(DashboardDataProvider provider) { DASHBOARD_PROVIDER = provider; }
    public static void setEquipamentosProvider(EquipamentosProvider provider) { EQUIPAMENTOS_PROVIDER = provider; }
    public static void setOsProvider(OsProvider provider) { OS_PROVIDER = provider; }
    public static void setOrcamentosAprovadosProvider(OrcamentosAprovadosProvider provider) { ORCAMENTOS_APROVADOS_PROVIDER = provider; }
    public static void setIniciarOsHandler(IniciarOsHandler handler) { INICIAR_OS_HANDLER = handler; }
    public static void setConcluirOsHandler(ConcluirOsHandler handler) { CONCLUIR_OS_HANDLER = handler; }
    public static void setSalvarEquipamentoHandler(SalvarEquipamentoHandler handler) { SALVAR_EQUIPAMENTO_HANDLER = handler; }
    public static void setCriarOsHandler(CriarOsHandler handler) { CRIAR_OS_HANDLER = handler; }
    public static void setTecnicosProvider(TecnicosProvider provider) { TECNICOS_PROVIDER = provider; }
    public static void setOrcamentosPorTecnicoProvider(OrcamentosPorTecnicoProvider provider) { ORCAMENTOS_PROVIDER = provider; }
    public static void setCriarOrcamentoHandler(CriarOrcamentoHandler handler) { CRIAR_ORCAMENTO_HANDLER = handler; }
    public static void setAprovarOrcamentoHandler(AprovarOrcamentoHandler handler) { APROVAR_ORCAMENTO_HANDLER = handler; }
    public static void setReprovarOrcamentoHandler(ReprovarOrcamentoHandler handler) { REPROVAR_ORCAMENTO_HANDLER = handler; }
    public static void setClientesProvider(ClientesProvider provider) { CLIENTES_PROVIDER = provider; }
    public static void setSalvarClienteHandler(SalvarClienteHandler handler) { SALVAR_CLIENTE_HANDLER = handler; }
    public static void setDeletarClienteHandler(DeletarClienteHandler handler) { DELETAR_CLIENTE_HANDLER = handler; }
    public static void setDeletarEquipamentoHandler(DeletarEquipamentoHandler handler) { DELETAR_EQUIPAMENTO_HANDLER = handler; }
    public static void setCadastrarTecnicoHandler(CadastrarTecnicoHandler handler) { CADASTRAR_TECNICO_HANDLER = handler; }
    public static void setDeletarTecnicoHandler(DeletarTecnicoHandler handler) { DELETAR_TECNICO_HANDLER = handler; }
    public static void setExportarOrcamentoPdfHandler(ExportarOrcamentoPdfHandler handler) { EXPORTAR_ORCAMENTO_PDF_HANDLER = handler; }
    public static void setExportarOsPdfHandler(ExportarOsPdfHandler handler) { EXPORTAR_OS_PDF_HANDLER = handler; }
    public static void setOrcamentosPorEquipamentoProvider(OrcamentosPorEquipamentoProvider provider) { ORCAMENTOS_POR_EQUIPAMENTO_PROVIDER = provider; }
    public static void setEquipamentosPorClienteProvider(EquipamentosPorClienteProvider provider) { EQUIPAMENTOS_POR_CLIENTE_PROVIDER = provider; }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        primaryStage.setTitle("Sistema OS");
        primaryStage.setMinWidth(420);
        primaryStage.setMinHeight(560);
        primaryStage.setScene(new Scene(loginView(), 420, 560));
        primaryStage.show();
    }

    private Parent loginView() {
        BorderPane wrapper = new BorderPane();
        wrapper.setStyle("-fx-background-color:#950909;");

        VBox box = new VBox(16);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));
        box.setMaxWidth(420);
        box.setStyle(card() + "-fx-padding:36;-fx-effect:dropshadow(gaussian, rgba(15,23,42,0.10), 24, 0, 0, 8);");

        Label titulo = title("Contrimaq", 30);
        Label sub = subtitle("Entre para acessar o sistema");

        TextField login = input("Usuário");
        PasswordField senha = password("Senha");

        Button entrar = primary("Entrar");
        entrar.setMaxWidth(Double.MAX_VALUE);

        entrar.setOnAction(e -> {
            try {
                if (LOGIN_HANDLER == null) {
                    alert("Erro", "Handler de login não configurado.");
                    return;
                }

                boolean ok = LOGIN_HANDLER.autenticar(login.getText(), senha.getText());

                if (!ok) {
                    alert("Login", "Usuário ou senha inválidos.");
                    return;
                }

                abrirSistema();

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        box.getChildren().addAll(titulo, sub, login, senha, entrar);
        wrapper.setCenter(box);

        return wrapper;
    }

    private void abrirSistema() {
        root = new BorderPane();
        root.setStyle("-fx-background-color:#f8fafc;");

        VBox menu = new VBox(12);
        menu.setPadding(new Insets(28, 18, 28, 18));
        menu.setPrefWidth(250);
        menu.setStyle("-fx-background-color:#950606;");

        Label logo = new Label("CONTRIMAQ");
        logo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        logo.setStyle("-fx-text-fill:white;");

        Label subLogo = new Label("Gestão de serviços");
        subLogo.setStyle("-fx-text-fill:#FFFFFF; -fx-font-size:12px;");

        VBox marca = new VBox(2, logo, subLogo);
        marca.setPadding(new Insets(0, 0, 24, 0));

        Button dashboard = menuBtn("Dashboard", () -> setCenter(dashboard()));
        Button clientes = menuBtn("Clientes", () -> setCenter(cliente()));
        Button equipamentos = menuBtn("Equipamentos", () -> setCenter(equipamento()));
        Button tecnicos = menuBtn("Técnicos", () -> setCenter(tecnico()));
        Button orcamentos = menuBtn("Orçamentos", () -> setCenter(orcamento()));
        Button os = menuBtn("Ordens de Serviço", () -> setCenter(ordemDeServico()));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button sair = menuBtn("Sair", () -> stage.setScene(new Scene(loginView(), 1200, 760)));

        menu.getChildren().addAll(
            marca,
            dashboard,
            clientes,
            equipamentos,
            tecnicos,
            orcamentos,
            os,
            spacer,
            sair
        );

        root.setLeft(menu);
        stage.setScene(new Scene(root, 1200, 760));
        setCenter(dashboard());
    }

   private VBox dashboard() {
    VBox v = page();

    Label titulo = title("Dashboard", 22);
    Label sub = subtitle("Visão geral das Ordens de Serviço.");

    List<OrdemDeServico> ordens = carregarTodasAsOs();

    long total = ordens.size();
    long pendentes = ordens.stream().filter(OrdemDeServico::estaPendente).count();
    long andamento = ordens.stream().filter(OrdemDeServico::estaEmAndamento).count();
    long concluidas = ordens.stream().filter(OrdemDeServico::estaConcluida).count();
    long atrasadas = ordens.stream().filter(OrdemDeServico::estaAtrasada).count();

    VBox cardTotal = metricCard("Total", String.valueOf(total), "#1e293b");
    VBox cardPendentes = metricCard("Pendentes", String.valueOf(pendentes), "#64748b");
    VBox cardAndamento = metricCard("Em andamento", String.valueOf(andamento), "#2563eb");
    VBox cardConcluidas = metricCard("Concluídas", String.valueOf(concluidas), "#16a34a");
    VBox cardAtrasadas = metricCard("Atrasadas", String.valueOf(atrasadas), "#dc2626");

    cardTotal.setStyle(cardBorder() + "-fx-cursor:hand;");
    cardPendentes.setStyle(cardBorder() + "-fx-cursor:hand;");
    cardAndamento.setStyle(cardBorder() + "-fx-cursor:hand;");
    cardConcluidas.setStyle(cardBorder() + "-fx-cursor:hand;");
    cardAtrasadas.setStyle(cardBorder() + "-fx-cursor:hand;");

    HBox cards = new HBox(
        16,
        cardTotal,
        cardPendentes,
        cardAndamento,
        cardConcluidas,
        cardAtrasadas
    );

    cards.setAlignment(Pos.CENTER_LEFT);

    ObservableList<OrdemDeServico> ordensFiltradas = FXCollections.observableArrayList(ordens);

    ListView<OrdemDeServico> lista = new ListView<>();
    lista.setPrefHeight(520);
    lista.setStyle("-fx-background-color:transparent; -fx-border-color:transparent;");
    lista.setItems(ordensFiltradas);
    lista.setPlaceholder(subtitle("Nenhuma Ordem de Serviço encontrada."));

    Label listaSub = subtitle("Exibindo todas as Ordens de Serviço.");

    TextField pesquisarOs = input("Pesquisar OS por número, equipamento, status ou descrição");

    Button concluir = primary("Concluir OS selecionada");
    concluir.setDisable(true);

    final java.util.concurrent.atomic.AtomicReference<java.util.function.Predicate<OrdemDeServico>> filtroStatus =
        new java.util.concurrent.atomic.AtomicReference<>(os -> true);

    Runnable aplicarFiltros = () -> {
        String termo = pesquisarOs.getText() == null
            ? ""
            : pesquisarOs.getText().trim().toLowerCase();

        List<OrdemDeServico> resultado = ordens.stream()
            .filter(filtroStatus.get())
            .filter(os -> {
                if (os == null) return false;
                if (termo.isBlank()) return true;

                String id = String.valueOf(os.getId());

                String status = os.getStatus() == null
                    ? ""
                    : os.getStatus().name().toLowerCase();

                String descricao = os.getDescricaoProblema() == null
                    ? ""
                    : os.getDescricaoProblema().toLowerCase();

                String equipamento = os.getEquipamento() == null
                    ? ""
                    : safe(os.getEquipamento().getNome()).toLowerCase();

                String modelo = os.getEquipamento() == null
                    ? ""
                    : safe(os.getEquipamento().getModelo()).toLowerCase();

                return id.contains(termo)
                    || status.contains(termo)
                    || descricao.contains(termo)
                    || equipamento.contains(termo)
                    || modelo.contains(termo);
            })
            .collect(Collectors.toList());

        ordensFiltradas.setAll(resultado);
        lista.getSelectionModel().clearSelection();
        concluir.setDisable(true);
    };

    pesquisarOs.textProperty().addListener((obs, oldValue, newValue) -> aplicarFiltros.run());

    lista.setCellFactory(lv -> new ListCell<>() {
        @Override
        protected void updateItem(OrdemDeServico os, boolean empty) {
            super.updateItem(os, empty);
            setStyle("-fx-background-color:transparent; -fx-padding:4 0;");

            if (empty || os == null) {
                setText(null);
                setGraphic(null);
                return;
            }

            VBox card = osResumoCard(os);
            card.prefWidthProperty().bind(lv.widthProperty().subtract(24));
            setGraphic(card);
            setText(null);
        }
    });

    cardTotal.setOnMouseClicked(e -> {
        filtroStatus.set(os -> true);
        listaSub.setText("Exibindo todas as Ordens de Serviço.");
        aplicarFiltros.run();
    });

    cardPendentes.setOnMouseClicked(e -> {
        filtroStatus.set(OrdemDeServico::estaPendente);
        listaSub.setText("Exibindo apenas Ordens de Serviço pendentes.");
        aplicarFiltros.run();
    });

    cardAndamento.setOnMouseClicked(e -> {
        filtroStatus.set(OrdemDeServico::estaEmAndamento);
        listaSub.setText("Exibindo apenas Ordens de Serviço em andamento.");
        aplicarFiltros.run();
    });

    cardConcluidas.setOnMouseClicked(e -> {
        filtroStatus.set(OrdemDeServico::estaConcluida);
        listaSub.setText("Exibindo apenas Ordens de Serviço concluídas.");
        aplicarFiltros.run();
    });

    cardAtrasadas.setOnMouseClicked(e -> {
        filtroStatus.set(OrdemDeServico::estaAtrasada);
        listaSub.setText("Exibindo apenas Ordens de Serviço atrasadas.");
        aplicarFiltros.run();
    });

    lista.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
        concluir.setDisable(sel == null || !sel.estaEmAndamento() || CONCLUIR_OS_HANDLER == null);
    });

    concluir.setOnAction(e -> {
        try {
            OrdemDeServico sel = lista.getSelectionModel().getSelectedItem();

            if (sel == null) {
                throw new IllegalArgumentException("Selecione uma OS.");
            }

            if (!sel.estaEmAndamento()) {
                throw new IllegalArgumentException("Somente OS em andamento pode ser concluída.");
            }

            CONCLUIR_OS_HANDLER.concluir(
                sel.getId(),
                "",
                sel.getEquipamento(),
                sel.getOrcamentoAprovado()
            );

            alert("OS", "Ordem de Serviço concluída com sucesso.");
            setCenter(dashboard());

        } catch (Exception ex) {
            alert("Erro", ex.getMessage());
        }
    });

    VBox listaCard = new VBox(
        14,
        title("Ordens de Serviço", 16),
        listaSub,
        pesquisarOs,
        lista,
        concluir
    );

    listaCard.setPadding(new Insets(22));
    listaCard.setStyle(cardBorder());

    v.getChildren().addAll(titulo, sub, cards, listaCard);
    return v;
}

    private VBox cliente() {
        VBox v = page();

        Label titulo = title("Clientes", 22);
        Label sub = subtitle("Cadastro e listagem de clientes. Pesquise pelo nome e selecione para ver os vínculos.");

        TextField nome = input("Nome");
        TextField telefone = input("Telefone");
        TextField email = input("Email");
        TextField cidade = input("Cidade");
        TextField cep = input("CEP");
        TextField documento = input("CPF / CNPJ");

        Button salvar = primary("Salvar Cliente");
        Button deletar = primary("Deletar Cliente");
        deletar.setDisable(true);

        TextField busca = input("Pesquisar cliente pelo nome");

        ObservableList<Cliente> clientesBase = FXCollections.observableArrayList();
        FilteredList<Cliente> clientesFiltrados = new FilteredList<>(clientesBase, c -> true);

        ListView<Cliente> lista = new ListView<>();
        lista.setItems(clientesFiltrados);
        lista.setPrefHeight(520);
        lista.setStyle("-fx-background-color:transparent; -fx-border-color:transparent;");
        lista.setPlaceholder(subtitle("Nenhum cliente encontrado."));

        busca.textProperty().addListener((obs, old, termo) -> {
            String filtro = termo == null ? "" : termo.trim().toLowerCase();

            clientesFiltrados.setPredicate(c -> {
                if (c == null) return false;
                if (filtro.isBlank()) return true;

                String nomeCliente = c.getNome() == null ? "" : c.getNome().toLowerCase();
                return nomeCliente.contains(filtro);
            });

            Cliente selecionado = lista.getSelectionModel().getSelectedItem();
            if (selecionado != null && !clientesFiltrados.contains(selecionado)) {
                lista.getSelectionModel().clearSelection();
            }
        });

        lista.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Cliente c, boolean empty) {
                super.updateItem(c, empty);
                setStyle("-fx-background-color:transparent; -fx-padding:4 0;");

                if (empty || c == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                VBox card = listCard(
                    "👤",
                    safe(c.getNome()),
                    "📄 " + safe(c.getDocumento()),
                    "📞 " + safe(c.getTelefone()),
                    "✉️ " + safe(c.getEmail()),
                    "📍 " + safe(c.getCidade())
                );

                card.prefWidthProperty().bind(lv.widthProperty().subtract(24));
                setGraphic(card);
                setText(null);
            }
        });

        VBox detalhePane = new VBox(10);
        detalhePane.setPadding(new Insets(16));
        detalhePane.setStyle(cardBorder());
        detalhePane.setVisible(false);
        detalhePane.setManaged(false);

        lista.getSelectionModel().selectedItemProperty().addListener((obs, old, clienteSel) -> {
            deletar.setDisable(clienteSel == null || DELETAR_CLIENTE_HANDLER == null);
            detalhePane.getChildren().clear();

            if (clienteSel == null) {
                detalhePane.setVisible(false);
                detalhePane.setManaged(false);
                return;
            }

            detalhePane.setVisible(true);
            detalhePane.setManaged(true);

            Label detalheTitulo = title("Cliente selecionado", 16);
            Label info = subtitle(
                safe(clienteSel.getNome()) +
                " | Documento: " + safe(clienteSel.getDocumento()) +
                " | Cidade: " + safe(clienteSel.getCidade())
            );

            detalhePane.getChildren().addAll(detalheTitulo, info, new Separator());

            List<Equipamento> equipamentos = new ArrayList<>();

            if (EQUIPAMENTOS_POR_CLIENTE_PROVIDER != null) {
                try {
                    equipamentos = EQUIPAMENTOS_POR_CLIENTE_PROVIDER.listarPorCliente(clienteSel);
                } catch (Exception ex) {
                    alert("Erro", "Erro ao carregar equipamentos: " + ex.getMessage());
                }
            }

            if (equipamentos.isEmpty()) {
                detalhePane.getChildren().add(subtitle("Nenhum equipamento cadastrado para este cliente."));
                return;
            }

            for (Equipamento eq : equipamentos) {
                VBox eqBox = new VBox(6);
                eqBox.setPadding(new Insets(12));
                eqBox.setStyle(cardSoft());

                Label eqTitulo = new Label("🖥️ " + safe(eq.getNome()) + " | " + safe(eq.getModelo()));
                eqTitulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
                eqTitulo.setStyle("-fx-text-fill:#1e293b;");

                eqBox.getChildren().add(eqTitulo);

                if (ORCAMENTOS_POR_EQUIPAMENTO_PROVIDER != null) {
                    try {
                        List<Orçamento> orcs = ORCAMENTOS_POR_EQUIPAMENTO_PROVIDER.listarPorEquipamento(eq);

                        if (orcs.isEmpty()) {
                            eqBox.getChildren().add(subtitle("Nenhum orçamento vinculado."));
                        } else {
                            for (Orçamento o : orcs) {
                                eqBox.getChildren().add(subtitle(
                                    "Orçamento #" + o.getId() +
                                    " | " + o.getStatus() +
                                    " | R$ " + o.getValorTotalPecas()
                                ));
                            }
                        }
                    } catch (Exception ex) {
                        eqBox.getChildren().add(subtitle("Erro ao carregar orçamentos."));
                    }
                }

                detalhePane.getChildren().add(eqBox);
            }
        });

        try {
            clientesBase.setAll(CLIENTES_PROVIDER != null ? CLIENTES_PROVIDER.listarClientes() : Collections.emptyList());
        } catch (Exception ex) {
            alert("Erro", ex.getMessage());
            clientesBase.clear();
        }

        salvar.setOnAction(e -> {
            try {
                if (SALVAR_CLIENTE_HANDLER == null) return;

                String docLimpo = documento.getText() == null ? "" : documento.getText().replaceAll("\\D", "");

                SALVAR_CLIENTE_HANDLER.salvar(
                    nome.getText(),
                    telefone.getText(),
                    email.getText(),
                    cidade.getText(),
                    cep.getText(),
                    docLimpo
                );

                clientesBase.setAll(CLIENTES_PROVIDER != null ? CLIENTES_PROVIDER.listarClientes() : Collections.emptyList());

                nome.clear();
                telefone.clear();
                email.clear();
                cidade.clear();
                cep.clear();
                documento.clear();

                alert("Cliente", "Cliente salvo com sucesso.");

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        deletar.setOnAction(e -> {
            try {
                Cliente sel = lista.getSelectionModel().getSelectedItem();

                if (sel == null) throw new IllegalArgumentException("Selecione um cliente.");
                if (DELETAR_CLIENTE_HANDLER == null) return;

                DELETAR_CLIENTE_HANDLER.deletar(sel.getDocumento());

                clientesBase.setAll(CLIENTES_PROVIDER != null ? CLIENTES_PROVIDER.listarClientes() : Collections.emptyList());
                detalhePane.getChildren().clear();
                detalhePane.setVisible(false);
                detalhePane.setManaged(false);

                alert("Cliente", "Cliente deletado com sucesso.");

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        VBox formCard = new VBox(12, title("Cadastrar cliente", 16), subtitle("Preencha os dados principais do cliente."), nome, telefone, email, cidade, cep, documento, salvar, deletar);
        formCard.setPadding(new Insets(22));
        formCard.setStyle(cardBorder());
        formCard.setPrefWidth(380);
        formCard.setMinWidth(340);

        VBox listaCard = new VBox(12, title("Clientes cadastrados", 16), subtitle("Use a busca para localizar rapidamente pelo nome."), busca, lista, detalhePane);
        listaCard.setPadding(new Insets(22));
        listaCard.setStyle(cardBorder());
        HBox.setHgrow(listaCard, Priority.ALWAYS);

        HBox conteudo = new HBox(16, formCard, listaCard);

        v.getChildren().addAll(titulo, sub, conteudo);
        return v;
    }

    private VBox equipamento() {
        VBox v = page();

        Label titulo = title("Equipamentos", 22);
        Label sub = subtitle("Cadastro, listagem e remoção de equipamentos.");

        TextField nome = input("Nome do equipamento");
        TextField modelo = input("Modelo");
        TextField documentoCliente = input("Documento do cliente");

        Button salvar = primary("Salvar Equipamento");
        Button deletar = primary("Deletar Equipamento");
        deletar.setDisable(true);

        ListView<Equipamento> lista = new ListView<>();
        lista.setPrefHeight(520);
        lista.setStyle("-fx-background-color:transparent; -fx-border-color:transparent;");
        lista.setPlaceholder(subtitle("Nenhum equipamento encontrado."));

        Runnable carregar = () -> {
            try {
                lista.setItems(EQUIPAMENTOS_PROVIDER != null
                    ? FXCollections.observableArrayList(EQUIPAMENTOS_PROVIDER.listarEquipamentos())
                    : FXCollections.observableArrayList());
            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
                lista.setItems(FXCollections.observableArrayList());
            }
        };

        lista.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Equipamento eq, boolean empty) {
                super.updateItem(eq, empty);
                setStyle("-fx-background-color:transparent; -fx-padding:4 0;");

                if (empty || eq == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                VBox card = listCard(
                    "🖥️",
                    safe(eq.getNome()),
                    "🔧 " + safe(eq.getModelo()),
                    "📄 Cliente: " + safe(eq.getDocumentoCliente()),
                    "ID: " + eq.getId()
                );

                card.prefWidthProperty().bind(lv.widthProperty().subtract(24));
                setGraphic(card);
                setText(null);
            }
        });

        lista.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            deletar.setDisable(sel == null || DELETAR_EQUIPAMENTO_HANDLER == null);
        });

        salvar.setOnAction(e -> {
            try {
                if (SALVAR_EQUIPAMENTO_HANDLER == null) return;

                SALVAR_EQUIPAMENTO_HANDLER.salvar(
                    nome.getText(),
                    modelo.getText(),
                    documentoCliente.getText() == null ? "" : documentoCliente.getText().replaceAll("\\D", "")
                );

                nome.clear();
                modelo.clear();
                documentoCliente.clear();

                carregar.run();
                alert("Equipamento", "Equipamento salvo com sucesso.");

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        deletar.setOnAction(e -> {
            try {
                Equipamento sel = lista.getSelectionModel().getSelectedItem();

                if (sel == null) throw new IllegalArgumentException("Selecione um equipamento.");
                if (DELETAR_EQUIPAMENTO_HANDLER == null) return;

                DELETAR_EQUIPAMENTO_HANDLER.deletar(sel.getId());
                carregar.run();

                alert("Equipamento", "Equipamento deletado com sucesso.");

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        carregar.run();

        VBox formCard = new VBox(12, title("Cadastrar equipamento", 16), subtitle("Vincule o equipamento ao documento do cliente."), nome, modelo, documentoCliente, salvar, deletar);
        formCard.setPadding(new Insets(22));
        formCard.setStyle(cardBorder());
        formCard.setPrefWidth(380);
        formCard.setMinWidth(340);

        VBox listaCard = new VBox(12, title("Equipamentos cadastrados", 16), subtitle("Selecione um equipamento para remover."), lista);
        listaCard.setPadding(new Insets(22));
        listaCard.setStyle(cardBorder());
        HBox.setHgrow(listaCard, Priority.ALWAYS);

        HBox conteudo = new HBox(16, formCard, listaCard);

        v.getChildren().addAll(titulo, sub, conteudo);
        return v;
    }

    private VBox tecnico() {
        VBox v = page();

        Label titulo = title("Técnicos", 22);
        Label sub = subtitle("Cadastro e gerenciamento de técnicos.");

        TextField nome = input("Nome");
        TextField documento = input("Documento");
        DatePicker data = new DatePicker();
        data.setPromptText("Data de associação");
        data.setPrefHeight(44);
        data.setMaxWidth(Double.MAX_VALUE);
        data.setStyle(inputStyle());
        hoverFocus(data);

        Button salvar = primary("Cadastrar Técnico");
        Button deletar = primary("Deletar Técnico");
        deletar.setDisable(true);

        ListView<Tecnico> lista = new ListView<>();
        lista.setPrefHeight(520);
        lista.setStyle("-fx-background-color:transparent; -fx-border-color:transparent;");
        lista.setPlaceholder(subtitle("Nenhum técnico encontrado."));

        Runnable carregar = () -> {
            try {
                lista.setItems(TECNICOS_PROVIDER != null
                    ? FXCollections.observableArrayList(TECNICOS_PROVIDER.listarTecnicos())
                    : FXCollections.observableArrayList());
            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
                lista.setItems(FXCollections.observableArrayList());
            }
        };

        lista.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Tecnico t, boolean empty) {
                super.updateItem(t, empty);
                setStyle("-fx-background-color:transparent; -fx-padding:4 0;");

                if (empty || t == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                String dataTexto = t.getDataAssociacao() == null ? "-" : t.getDataAssociacao().format(DATE_FMT);

                VBox card = listCard(
                    "👷",
                    safe(t.getNome()),
                    "📄 " + safe(t.getDocumento()),
                    "📅 Associação: " + dataTexto,
                    "ID: " + t.getId()
                );

                card.prefWidthProperty().bind(lv.widthProperty().subtract(24));
                setGraphic(card);
                setText(null);
            }
        });

        lista.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            deletar.setDisable(sel == null || DELETAR_TECNICO_HANDLER == null);
        });

        salvar.setOnAction(e -> {
            try {
                if (CADASTRAR_TECNICO_HANDLER == null) return;

                CADASTRAR_TECNICO_HANDLER.cadastrar(
                    nome.getText(),
                    documento.getText() == null ? "" : documento.getText().replaceAll("\\D", ""),
                    data.getValue()
                );

                nome.clear();
                documento.clear();
                data.setValue(null);

                carregar.run();
                alert("Técnico", "Técnico cadastrado com sucesso.");

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        deletar.setOnAction(e -> {
            try {
                Tecnico sel = lista.getSelectionModel().getSelectedItem();

                if (sel == null) throw new IllegalArgumentException("Selecione um técnico.");
                if (DELETAR_TECNICO_HANDLER == null) return;

                DELETAR_TECNICO_HANDLER.deletar(sel.getId());
                carregar.run();

                alert("Técnico", "Técnico deletado com sucesso.");

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        carregar.run();

        VBox formCard = new VBox(12, title("Cadastrar técnico", 16), subtitle("Informe os dados principais do técnico."), nome, documento, data, salvar, deletar);
        formCard.setPadding(new Insets(22));
        formCard.setStyle(cardBorder());
        formCard.setPrefWidth(380);
        formCard.setMinWidth(340);

        VBox listaCard = new VBox(12, title("Técnicos cadastrados", 16), subtitle("Selecione um técnico para remover."), lista);
        listaCard.setPadding(new Insets(22));
        listaCard.setStyle(cardBorder());
        HBox.setHgrow(listaCard, Priority.ALWAYS);

        HBox conteudo = new HBox(16, formCard, listaCard);

        v.getChildren().addAll(titulo, sub, conteudo);
        return v;
    }

    private VBox orcamento() {
        VBox v = page();

        Label titulo = title("Orçamentos", 22);
        Label sub = subtitle("Criação, aprovação, reprovação e exportação de orçamentos.");

        ComboBox<Tecnico> tecnicos = new ComboBox<>();
        tecnicos.setPromptText("Selecione o técnico");
        tecnicos.setPrefHeight(44);
        tecnicos.setMaxWidth(Double.MAX_VALUE);
        tecnicos.setStyle(inputStyle());
        hoverFocus(tecnicos);

        TextField descricaoItem = input("Descrição do item");
        TextField valorItem = input("Valor do item");
        Button adicionarItem = primary("Adicionar item");

        ObservableList<ItemOrcamento> itens = FXCollections.observableArrayList();

        ListView<ItemOrcamento> listaItens = new ListView<>(itens);
        listaItens.setPrefHeight(150);
        listaItens.setStyle("-fx-background-color:transparent; -fx-border-color:#e5e7eb; -fx-border-radius:10; -fx-background-radius:10;");
        listaItens.setPlaceholder(subtitle("Nenhum item adicionado."));

        TextField pagamento = input("Tipo de pagamento");
        TextField maoObra = input("Valor da mão de obra");

        Button criar = primary("Criar Orçamento");

        ListView<Orçamento> listaOrcamentos = new ListView<>();
        listaOrcamentos.setPrefHeight(520);
        listaOrcamentos.setStyle("-fx-background-color:transparent; -fx-border-color:transparent;");
        listaOrcamentos.setPlaceholder(subtitle("Selecione um técnico para listar os orçamentos."));

        Button aprovar = primary("Aprovar Orçamento");
        Button reprovar = primary("Reprovar Orçamento");
        Button exportarPdf = primary("Exportar Orçamento para PDF");

        aprovar.setDisable(true);
        reprovar.setDisable(true);
        exportarPdf.setDisable(true);

        Runnable carregarTecnicos = () -> {
            try {
                tecnicos.setItems(TECNICOS_PROVIDER != null
                    ? FXCollections.observableArrayList(TECNICOS_PROVIDER.listarTecnicos())
                    : FXCollections.observableArrayList());
            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
                tecnicos.setItems(FXCollections.observableArrayList());
            }
        };

        Runnable carregarOrcamentos = () -> {
            try {
                Tecnico tec = tecnicos.getValue();

                listaOrcamentos.setItems(tec != null && ORCAMENTOS_PROVIDER != null
                    ? FXCollections.observableArrayList(ORCAMENTOS_PROVIDER.listarPorTecnico(tec))
                    : FXCollections.observableArrayList());

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
                listaOrcamentos.setItems(FXCollections.observableArrayList());
            }
        };

        listaOrcamentos.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Orçamento o, boolean empty) {
                super.updateItem(o, empty);
                setStyle("-fx-background-color:transparent; -fx-padding:4 0;");

                if (empty || o == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                VBox card = orcamentoCard(o);
                card.prefWidthProperty().bind(lv.widthProperty().subtract(24));
                setGraphic(card);
                setText(null);
            }
        });

        listaOrcamentos.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            boolean vazio = sel == null;

            aprovar.setDisable(vazio || !sel.estaPendente() || APROVAR_ORCAMENTO_HANDLER == null);
            reprovar.setDisable(vazio || !sel.estaPendente() || REPROVAR_ORCAMENTO_HANDLER == null);
            exportarPdf.setDisable(vazio || EXPORTAR_ORCAMENTO_PDF_HANDLER == null);
        });

        tecnicos.setOnAction(e -> carregarOrcamentos.run());

        adicionarItem.setOnAction(e -> {
            try {
                String desc = descricaoItem.getText();

                if (desc == null || desc.isBlank()) {
                    throw new IllegalArgumentException("Informe a descrição do item.");
                }

                BigDecimal valor = parseDecimal(valorItem.getText());
                itens.add(new ItemOrcamento(desc, valor));

                descricaoItem.clear();
                valorItem.clear();

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        criar.setOnAction(e -> {
            try {
                Tecnico tec = tecnicos.getValue();

                if (tec == null) throw new IllegalArgumentException("Selecione um técnico.");
                if (itens.isEmpty()) throw new IllegalArgumentException("Adicione pelo menos um item.");
                if (CRIAR_ORCAMENTO_HANDLER == null) return;

                CRIAR_ORCAMENTO_HANDLER.criar(
                    new ArrayList<>(itens),
                    pagamento.getText(),
                    tec,
                    parseDecimal(maoObra.getText())
                );

                itens.clear();
                pagamento.clear();
                maoObra.clear();

                carregarOrcamentos.run();
                alert("Orçamento", "Orçamento criado com sucesso.");

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        aprovar.setOnAction(e -> {
            try {
                Orçamento sel = listaOrcamentos.getSelectionModel().getSelectedItem();
                Tecnico tec = tecnicos.getValue();

                if (sel == null) throw new IllegalArgumentException("Selecione um orçamento.");
                if (tec == null) throw new IllegalArgumentException("Selecione um técnico.");
                if (APROVAR_ORCAMENTO_HANDLER == null) return;

                APROVAR_ORCAMENTO_HANDLER.aprovar(sel.getId(), tec);
                carregarOrcamentos.run();

                alert("Orçamento", "Orçamento aprovado.");

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        reprovar.setOnAction(e -> {
            try {
                Orçamento sel = listaOrcamentos.getSelectionModel().getSelectedItem();
                Tecnico tec = tecnicos.getValue();

                if (sel == null) throw new IllegalArgumentException("Selecione um orçamento.");
                if (tec == null) throw new IllegalArgumentException("Selecione um técnico.");
                if (REPROVAR_ORCAMENTO_HANDLER == null) return;

                REPROVAR_ORCAMENTO_HANDLER.reprovar(sel.getId(), tec);
                carregarOrcamentos.run();

                alert("Orçamento", "Orçamento reprovado.");

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        exportarPdf.setOnAction(e -> {
            try {
                Orçamento sel = listaOrcamentos.getSelectionModel().getSelectedItem();

                if (sel == null) {
                    throw new IllegalArgumentException("Selecione um orçamento.");
                }

                if (EXPORTAR_ORCAMENTO_PDF_HANDLER == null) {
                    alert("Exportar PDF", "Handler não configurado.");
                    return;
                }

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Salvar Orçamento em PDF");
                fileChooser.setInitialFileName("Orcamento_" + sel.getId() + ".pdf");

                fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Arquivo PDF (*.pdf)", "*.pdf")
                );

                File destino = fileChooser.showSaveDialog(root.getScene().getWindow());

                if (destino == null) {
                    return;
                }

                if (!destino.getName().toLowerCase().endsWith(".pdf")) {
                    File pasta = destino.getParentFile();

                    destino = pasta == null
                        ? new File(destino.getName() + ".pdf")
                        : new File(pasta, destino.getName() + ".pdf");
                }

                EXPORTAR_ORCAMENTO_PDF_HANDLER.exportar(sel, destino);

                alert("Exportar PDF", "PDF salvo com sucesso em:\n" + destino.getAbsolutePath());

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        carregarTecnicos.run();

        VBox formCard = new VBox(
            12,
            title("Criar orçamento", 16),
            subtitle("Selecione o técnico, adicione os itens e informe pagamento/mão de obra."),
            tecnicos,
            descricaoItem,
            valorItem,
            adicionarItem,
            listaItens,
            pagamento,
            maoObra,
            criar
        );

        formCard.setPadding(new Insets(22));
        formCard.setStyle(cardBorder());
        formCard.setPrefWidth(400);
        formCard.setMinWidth(360);

        VBox listaCard = new VBox(
            12,
            title("Orçamentos do técnico", 16),
            subtitle("Selecione um orçamento para aprovar, reprovar ou exportar."),
            listaOrcamentos,
            aprovar,
            reprovar,
            exportarPdf
        );

        listaCard.setPadding(new Insets(22));
        listaCard.setStyle(cardBorder());
        HBox.setHgrow(listaCard, Priority.ALWAYS);

        HBox conteudo = new HBox(16, formCard, listaCard);

        v.getChildren().addAll(titulo, sub, conteudo);
        return v;
    }

   private VBox ordemDeServico() {
    VBox v = page();

    Label titulo = title("Ordens de Serviço", 22);
    Label sub = subtitle("Criação, acompanhamento, início e exportação das Ordens de Serviço.");

    ObservableList<Equipamento> equipamentosBase = FXCollections.observableArrayList();
    FilteredList<Equipamento> equipamentosFiltrados = new FilteredList<>(equipamentosBase, eq -> true);

    java.util.function.Function<Equipamento, String> textoEquipamento = eq -> {
        if (eq == null) return "";
        return safe(eq.getNome()) + "  -  " + safe(eq.getModelo());
    };

    java.util.function.Function<Equipamento, String> buscaEquipamento = eq -> {
        if (eq == null) return "";

        String id = String.valueOf(eq.getId());
        String nome = safe(eq.getNome()).toLowerCase();
        String modelo = safe(eq.getModelo()).toLowerCase();
        String documentoCliente = safe(eq.getDocumentoCliente()).toLowerCase();

        return id + " " + nome + " " + modelo + " " + documentoCliente;
    };

    ComboBox<Equipamento> equipamentos = new ComboBox<>(equipamentosFiltrados);
    equipamentos.setPromptText("Pesquisar ou selecionar equipamento");
    equipamentos.setEditable(true);
    equipamentos.setPrefHeight(54);
    equipamentos.setMaxWidth(Double.MAX_VALUE);
    equipamentos.setStyle(
        "-fx-background-color:white;" +
        "-fx-background-radius:14;" +
        "-fx-border-radius:14;" +
        "-fx-border-color:#cbd5e1;" +
        "-fx-border-width:1;" +
        "-fx-padding:4 10;" +
        "-fx-font-size:13px;"
    );
    hoverFocus(equipamentos);

    final boolean[] atualizandoTextoEquipamento = { false };

    equipamentos.setConverter(new javafx.util.StringConverter<Equipamento>() {
        @Override
        public String toString(Equipamento eq) {
            return textoEquipamento.apply(eq);
        }

        @Override
        public Equipamento fromString(String texto) {
            if (texto == null || texto.isBlank()) {
                return equipamentos.getValue();
            }

            String filtro = texto.trim().toLowerCase();

            return equipamentosBase.stream()
                .filter(eq -> buscaEquipamento.apply(eq).contains(filtro))
                .findFirst()
                .orElse(equipamentos.getValue());
        }
    });

    equipamentos.setCellFactory(lv -> new ListCell<>() {
        @Override
        protected void updateItem(Equipamento eq, boolean empty) {
            super.updateItem(eq, empty);

            if (empty || eq == null) {
                setText(null);
                setGraphic(null);
                setStyle("-fx-background-color:white;");
                return;
            }

            Label nome = new Label(safe(eq.getNome()));
            nome.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
            nome.setStyle("-fx-text-fill:#0f172a;");

            Label modelo = new Label("Modelo: " + safe(eq.getModelo()));
            modelo.setStyle("-fx-text-fill:#64748b; -fx-font-size:12px;");

            Label cliente = new Label("Documento do cliente: " + safe(eq.getDocumentoCliente()));
            cliente.setStyle("-fx-text-fill:#94a3b8; -fx-font-size:11px;");

            VBox box = new VBox(3, nome, modelo, cliente);
            box.setPadding(new Insets(8, 10, 8, 10));

            setGraphic(box);
            setText(null);

            setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#f1f5f9;" +
                "-fx-border-width:0 0 1 0;"
            );
        }
    });

    equipamentos.setButtonCell(new ListCell<>() {
        @Override
        protected void updateItem(Equipamento eq, boolean empty) {
            super.updateItem(eq, empty);

            if (empty || eq == null) {
                setText(null);
            } else {
                setText(textoEquipamento.apply(eq));
            }
        }
    });

    equipamentos.getEditor().setPromptText("Digite nome, modelo ou documento do cliente");

    equipamentos.getEditor().textProperty().addListener((obs, oldText, newText) -> {
        if (atualizandoTextoEquipamento[0]) {
            return;
        }

        String filtro = newText == null ? "" : newText.trim().toLowerCase();

        equipamentosFiltrados.setPredicate(eq -> {
            if (eq == null) return false;
            if (filtro.isBlank()) return true;

            return buscaEquipamento.apply(eq).contains(filtro);
        });

        if (equipamentos.isFocused() && !equipamentos.isShowing()) {
            equipamentos.show();
        }
    });

    equipamentos.setOnMouseClicked(e -> {
        if (!equipamentos.isShowing()) {
            equipamentos.show();
        }
    });

    TextArea descricao = area("Descrição do problema");
    descricao.setPrefHeight(150);
    descricao.setWrapText(true);

    DatePicker dataPrevista = new DatePicker();
    dataPrevista.setPromptText("Data prevista");
    dataPrevista.setPrefHeight(44);
    dataPrevista.setMaxWidth(Double.MAX_VALUE);
    dataPrevista.setStyle(inputStyle());
    hoverFocus(dataPrevista);

    Button criarOs = primary("Criar OS");
    criarOs.setMaxWidth(Double.MAX_VALUE);

    TextField buscarOs = input("Buscar OS por número, status ou descrição");

    ListView<OrdemDeServico> ordensServico = new ListView<>();
    ordensServico.setPrefHeight(440);
    ordensServico.setStyle("-fx-background-color:transparent; -fx-border-color:transparent;");
    ordensServico.setPlaceholder(subtitle("Selecione um equipamento para visualizar as ordens vinculadas."));

    final List<OrdemDeServico> ordensBase = new ArrayList<>();

    ordensServico.setCellFactory(lv -> new ListCell<>() {
        @Override
        protected void updateItem(OrdemDeServico os, boolean empty) {
            super.updateItem(os, empty);
            setStyle("-fx-background-color:transparent; -fx-padding:4 0;");

            if (empty || os == null) {
                setGraphic(null);
                setText(null);
                return;
            }

            Label id = new Label("OS #" + os.getId());
            id.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
            id.setStyle("-fx-text-fill:#1e293b;");

            Label badge = osStatusBadge(os);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox header = new HBox(8, id, spacer, badge);
            header.setAlignment(Pos.CENTER_LEFT);

            String previsto = os.getDataFechamentoPrevisto() == null
                ? "-"
                : os.getDataFechamentoPrevisto().format(DT_FMT);

            String abertura = os.getDataAbertura() == null
                ? "-"
                : os.getDataAbertura().format(DT_FMT);

            String descricaoResumo = os.getDescricaoProblema() == null || os.getDescricaoProblema().isBlank()
                ? "Sem descrição cadastrada"
                : os.getDescricaoProblema();

            if (descricaoResumo.length() > 95) {
                descricaoResumo = descricaoResumo.substring(0, 95) + "...";
            }

            Label desc = new Label("📝 " + descricaoResumo);
            desc.setWrapText(true);
            desc.setStyle("-fx-text-fill:#475569; -fx-font-size:12px;");

            HBox datas = new HBox(
                16,
                subtitle("📅 Abertura: " + abertura),
                subtitle("⏱ Previsto: " + previsto)
            );

            datas.setAlignment(Pos.CENTER_LEFT);

            VBox card = new VBox(8, header, desc, datas);
            card.setPadding(new Insets(14));
            card.setStyle(osListCardStyle(os, "white"));
            card.prefWidthProperty().bind(lv.widthProperty().subtract(22));

            card.setOnMouseEntered(e -> card.setStyle(osListCardStyle(os, "#f8fafc")));
            card.setOnMouseExited(e -> card.setStyle(osListCardStyle(os, "white")));

            setGraphic(card);
            setText(null);
        }
    });

    ComboBox<Orçamento> orcamentos = new ComboBox<>();
    orcamentos.setPromptText("Selecione o orçamento aprovado");
    orcamentos.setPrefHeight(44);
    orcamentos.setMaxWidth(Double.MAX_VALUE);
    orcamentos.setStyle(inputStyle());
    hoverFocus(orcamentos);

    orcamentos.setCellFactory(lv -> new ListCell<>() {
        @Override
        protected void updateItem(Orçamento o, boolean empty) {
            super.updateItem(o, empty);

            if (empty || o == null) {
                setText(null);
                return;
            }

            String tecnico = o.getTecnicoResponsavel() == null
                ? "Técnico não informado"
                : o.getTecnicoResponsavel().getNome();

            setText("Orçamento #" + o.getId() + " | " + tecnico + " | R$ " + o.getValorTotalPecas());
        }
    });

    orcamentos.setButtonCell(new ListCell<>() {
        @Override
        protected void updateItem(Orçamento o, boolean empty) {
            super.updateItem(o, empty);
            setText(empty || o == null ? null : "Orçamento #" + o.getId() + " | R$ " + o.getValorTotalPecas());
        }
    });

    Button iniciar = primary("Iniciar OS selecionada");
    iniciar.setMaxWidth(Double.MAX_VALUE);
    iniciar.setDisable(true);

    Button exportarPdf = primary("Exportar OS para PDF");
    exportarPdf.setMaxWidth(Double.MAX_VALUE);
    exportarPdf.setDisable(true);

    VBox detalhePane = new VBox(10);
    detalhePane.setPadding(new Insets(14));
    detalhePane.setStyle(
        "-fx-background-color:#f8fafc;" +
        "-fx-background-radius:12;" +
        "-fx-border-color:#e5e7eb;" +
        "-fx-border-radius:12;"
    );
    detalhePane.setVisible(false);
    detalhePane.setManaged(false);

    Consumer<String> aplicarFiltro = termo -> {
        String filtro = termo == null ? "" : termo.trim().toLowerCase();

        List<OrdemDeServico> filtradas = ordensBase.stream()
            .filter(os -> {
                if (os == null) return false;
                if (filtro.isBlank()) return true;

                String id = os.getId() == null ? "" : String.valueOf(os.getId());
                String status = os.getStatus() == null ? "" : os.getStatus().name().toLowerCase();
                String desc = os.getDescricaoProblema() == null ? "" : os.getDescricaoProblema().toLowerCase();

                return id.contains(filtro) || status.contains(filtro) || desc.contains(filtro);
            })
            .collect(Collectors.toList());

        OrdemDeServico selecionada = ordensServico.getSelectionModel().getSelectedItem();

        ordensServico.setItems(FXCollections.observableArrayList(filtradas));

        if (selecionada != null && filtradas.contains(selecionada)) {
            ordensServico.getSelectionModel().select(selecionada);
        } else {
            ordensServico.getSelectionModel().clearSelection();
        }
    };

    Runnable atualizarOrdens = () -> {
        recarregarOsEOrcamentos(equipamentos, ordensServico, orcamentos, ordensBase);
        aplicarFiltro.accept(buscarOs.getText());
    };

    buscarOs.textProperty().addListener((obs, oldValue, newValue) -> aplicarFiltro.accept(newValue));

    ordensServico.getSelectionModel().selectedItemProperty().addListener((obs, old, osSelecionada) -> {
        iniciar.setDisable(osSelecionada == null || !osSelecionada.estaPendente() || INICIAR_OS_HANDLER == null);
        exportarPdf.setDisable(osSelecionada == null);

        detalhePane.getChildren().clear();

        if (osSelecionada == null) {
            detalhePane.setVisible(false);
            detalhePane.setManaged(false);
            return;
        }

        detalhePane.setVisible(true);
        detalhePane.setManaged(true);

        Label detalheTitulo = new Label("Detalhes da OS #" + osSelecionada.getId());
        detalheTitulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        detalheTitulo.setStyle("-fx-text-fill:#1e293b;");

        String abertura = osSelecionada.getDataAbertura() == null
            ? "-"
            : osSelecionada.getDataAbertura().format(DT_FMT);

        String inicio = osSelecionada.getDataInicio() == null
            ? "-"
            : osSelecionada.getDataInicio().format(DT_FMT);

        String previsto = osSelecionada.getDataFechamentoPrevisto() == null
            ? "-"
            : osSelecionada.getDataFechamentoPrevisto().format(DT_FMT);

        String fechamento = osSelecionada.getDataFechamentoReal() == null
            ? "-"
            : osSelecionada.getDataFechamentoReal().format(DT_FMT);

        String problema = osSelecionada.getDescricaoProblema() == null || osSelecionada.getDescricaoProblema().isBlank()
            ? "Sem descrição cadastrada."
            : osSelecionada.getDescricaoProblema();

        Label status = osStatusBadge(osSelecionada);

        Label datas = subtitle(
            "📅 Abertura: " + abertura +
            "   |   Início: " + inicio +
            "   |   Previsto: " + previsto +
            "   |   Fechamento: " + fechamento
        );

        datas.setWrapText(true);

        Label problemaLabel = new Label("📝 " + problema);
        problemaLabel.setWrapText(true);
        problemaLabel.setStyle("-fx-text-fill:#475569; -fx-font-size:12px;");

        detalhePane.getChildren().addAll(detalheTitulo, status, datas, problemaLabel);
    });

    try {
        equipamentosBase.setAll(EQUIPAMENTOS_PROVIDER != null
            ? EQUIPAMENTOS_PROVIDER.listarEquipamentos()
            : Collections.emptyList());
    } catch (Exception ex) {
        alert("Erro", ex.getMessage());
        equipamentosBase.clear();
    }

    equipamentos.valueProperty().addListener((obs, oldValue, newValue) -> {
        if (newValue == null) {
            return;
        }

        atualizandoTextoEquipamento[0] = true;

        String texto = textoEquipamento.apply(newValue);
        equipamentos.getEditor().setText(texto);
        equipamentos.getEditor().positionCaret(texto.length());

        atualizandoTextoEquipamento[0] = false;

        atualizarOrdens.run();
    });

    criarOs.setOnAction(e -> {
        try {
            if (CRIAR_OS_HANDLER == null) return;

            Equipamento eq = equipamentos.getValue();

            if (eq == null) throw new IllegalArgumentException("Selecione um equipamento.");

            String desc = descricao.getText();

            if (desc == null || desc.isBlank()) {
                throw new IllegalArgumentException("Informe a descrição do problema.");
            }

            LocalDate data = dataPrevista.getValue();

            if (data == null) {
                throw new IllegalArgumentException("Selecione a data prevista.");
            }

            CRIAR_OS_HANDLER.criar(eq, desc, data);

            descricao.clear();
            dataPrevista.setValue(null);

            atualizarOrdens.run();

            alert("OS", "Ordem de Serviço criada com sucesso.");

        } catch (Exception ex) {
            alert("Erro", ex.getMessage());
        }
    });

    iniciar.setOnAction(e -> {
        try {
            if (INICIAR_OS_HANDLER == null) return;

            OrdemDeServico os = ordensServico.getSelectionModel().getSelectedItem();
            Equipamento eq = equipamentos.getValue();
            Orçamento orc = orcamentos.getValue();

            if (os == null) throw new IllegalArgumentException("Selecione uma OS.");
            if (eq == null) throw new IllegalArgumentException("Selecione um equipamento.");
            if (orc == null || !orc.estaAprovado()) throw new IllegalArgumentException("Selecione um orçamento aprovado.");

            INICIAR_OS_HANDLER.iniciar(os.getId(), orc, eq);

            atualizarOrdens.run();

            alert("OS", "Ordem de Serviço iniciada com sucesso.");

        } catch (Exception ex) {
            alert("Erro", ex.getMessage());
        }
    });

    exportarPdf.setOnAction(e -> {
        try {
            OrdemDeServico osSel = ordensServico.getSelectionModel().getSelectedItem();
            Orçamento orcSel = orcamentos.getValue();

            if (osSel == null) {
                throw new IllegalArgumentException("Selecione uma OS para exportar.");
            }

            if (EXPORTAR_OS_PDF_HANDLER == null) {
                alert("Exportar PDF", "Handler não configurado.");
                return;
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar Ordem de Serviço em PDF");
            fileChooser.setInitialFileName("OrdemDeServico_" + osSel.getId() + ".pdf");

            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Arquivo PDF (*.pdf)", "*.pdf")
            );

            File destino = fileChooser.showSaveDialog(root.getScene().getWindow());

            if (destino == null) {
                return;
            }

            if (!destino.getName().toLowerCase().endsWith(".pdf")) {
                File pasta = destino.getParentFile();
                destino = pasta == null
                    ? new File(destino.getName() + ".pdf")
                    : new File(pasta, destino.getName() + ".pdf");
            }

            EXPORTAR_OS_PDF_HANDLER.exportar(orcSel, osSel, destino);

            alert("Exportar PDF", "PDF salvo com sucesso em:\n" + destino.getAbsolutePath());

        } catch (Exception ex) {
            alert("Erro", ex.getMessage());
        }
    });

    Label formTitulo = title("Criar nova OS", 16);
    Label formSub = subtitle("Escolha o equipamento, descreva o problema e defina a previsão de fechamento.");

    VBox formCard = new VBox(12, formTitulo, formSub, equipamentos, descricao, dataPrevista, criarOs);
    formCard.setPadding(new Insets(22));
    formCard.setStyle(cardBorder());
    formCard.setMinWidth(330);
    formCard.setPrefWidth(380);

    Label listaTitulo = title("Ordens do equipamento", 16);
    Label listaSub = subtitle("Selecione uma ordem para visualizar detalhes, iniciar ou exportar.");

    VBox listaCard = new VBox(12, listaTitulo, listaSub, buscarOs, ordensServico, detalhePane);
    listaCard.setPadding(new Insets(22));
    listaCard.setStyle(cardBorder());
    VBox.setVgrow(ordensServico, Priority.ALWAYS);

    Label acaoTitulo = title("Ações da OS", 16);
    Label acaoSub = subtitle("Para iniciar, selecione uma OS pendente e vincule um orçamento aprovado.");

    VBox acaoCard = new VBox(
        12,
        acaoTitulo,
        acaoSub,
        orcamentos,
        iniciar,
        exportarPdf,
        subtitle("Para concluir uma OS, use o Dashboard e selecione uma ordem em andamento.")
    );

    acaoCard.setPadding(new Insets(22));
    acaoCard.setStyle(cardBorder());

    VBox colunaEsquerda = new VBox(16, formCard, acaoCard);
    colunaEsquerda.setMinWidth(330);
    colunaEsquerda.setPrefWidth(380);

    HBox conteudo = new HBox(16, colunaEsquerda, listaCard);
    HBox.setHgrow(listaCard, Priority.ALWAYS);
    VBox.setVgrow(conteudo, Priority.ALWAYS);

    v.getChildren().addAll(titulo, sub, conteudo);

    return v;
}

    private void recarregarOsEOrcamentos(
        ComboBox<Equipamento> equipamentos,
        ListView<OrdemDeServico> ordensServico,
        ComboBox<Orçamento> orcamentos,
        List<OrdemDeServico> ordensBase
    ) {
        Equipamento eq = equipamentos.getValue();
        ordensBase.clear();

        if (eq == null) {
            ordensServico.setItems(FXCollections.observableArrayList());
            orcamentos.setItems(FXCollections.observableArrayList());
            return;
        }

        try {
            if (OS_PROVIDER != null) {
                ordensBase.addAll(OS_PROVIDER.listarOsPorEquipamento(eq));
            }

            ordensServico.setItems(FXCollections.observableArrayList(ordensBase));

            orcamentos.setItems(ORCAMENTOS_APROVADOS_PROVIDER != null
                ? FXCollections.observableArrayList(ORCAMENTOS_APROVADOS_PROVIDER.listarOrcamentosAprovados())
                : FXCollections.observableArrayList());

        } catch (Exception ex) {
            alert("Erro", ex.getMessage());
            ordensBase.clear();
            ordensServico.setItems(FXCollections.observableArrayList());
            orcamentos.setItems(FXCollections.observableArrayList());
        }
    }

    private Label osStatusBadge(OrdemDeServico os) {
        String texto;
        String cor;

        if (os != null && os.estaAtrasada()) {
            texto = "ATRASADA";
            cor = "#dc2626";
        } else if (os != null && os.getStatus() != null) {
            texto = os.getStatus().name().replace('_', ' ');
            cor = switch (os.getStatus()) {
                case PENDENTE -> "#64748b";
                case EM_ANDAMENTO -> "#2563eb";
                case CONCLUIDA -> "#16a34a";
            };
        } else {
            texto = "SEM STATUS";
            cor = "#64748b";
        }

        Label badge = new Label(texto);

        badge.setStyle(
            "-fx-background-color:" + cor + ";" +
            "-fx-text-fill:white;" +
            "-fx-font-weight:bold;" +
            "-fx-font-size:11px;" +
            "-fx-background-radius:999;" +
            "-fx-padding:4 10;"
        );

        return badge;
    }

    private String osListCardStyle(OrdemDeServico os, String bg) {
        String border;

        if (os != null && os.estaAtrasada()) {
            border = "#dc2626";
        } else if (os != null && os.getStatus() != null) {
            border = switch (os.getStatus()) {
                case PENDENTE -> "#94a3b8";
                case EM_ANDAMENTO -> "#2563eb";
                case CONCLUIDA -> "#16a34a";
            };
        } else {
            border = "#e5e7eb";
        }

        return "-fx-background-color:" + bg + ";" +
            "-fx-background-radius:14;" +
            "-fx-border-radius:14;" +
            "-fx-border-width:1 1 1 4;" +
            "-fx-border-color:#e5e7eb #e5e7eb #e5e7eb " + border + ";" +
            "-fx-effect:dropshadow(gaussian, rgba(15,23,42,0.05), 10, 0, 0, 2);";
    }

    private VBox metricCard(String titulo, String valor, String cor) {
        VBox box = new VBox(8);
        box.setPadding(new Insets(18));
        box.setMinWidth(150);
        box.setStyle(cardBorder());

        Label t = subtitle(titulo);

        Label v = new Label(valor);
        v.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        v.setStyle("-fx-text-fill:" + cor + ";");

        box.getChildren().addAll(t, v);

        return box;
    }

    private VBox osResumoCard(OrdemDeServico os) {
        Label id = new Label("OS #" + os.getId());
        id.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        id.setStyle("-fx-text-fill:#1e293b;");

        Label status = osStatusBadge(os);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(8, id, spacer, status);
        header.setAlignment(Pos.CENTER_LEFT);

        String equipamento = os.getEquipamento() == null ? "-" : safe(os.getEquipamento().getNome());
        String abertura = os.getDataAbertura() == null ? "-" : os.getDataAbertura().format(DT_FMT);
        String previsto = os.getDataFechamentoPrevisto() == null ? "-" : os.getDataFechamentoPrevisto().format(DT_FMT);

        Label eq = subtitle("🖥️ Equipamento: " + equipamento);
        Label datas = subtitle("📅 Abertura: " + abertura + " | ⏱ Previsto: " + previsto);

        VBox card = new VBox(8, header, eq, datas);
        card.setPadding(new Insets(14));
        card.setStyle(osListCardStyle(os, "white"));

        return card;
    }

    private VBox orcamentoCard(Orçamento o) {
        Label id = new Label("Orçamento #" + o.getId());
        id.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        id.setStyle("-fx-text-fill:#1e293b;");

        String cor = o.estaAprovado() ? "#16a34a" : o.estaReprovado() ? "#dc2626" : "#64748b";

        Label status = new Label(o.getStatus().name());
        status.setStyle(
            "-fx-background-color:" + cor + ";" +
            "-fx-text-fill:white;" +
            "-fx-font-weight:bold;" +
            "-fx-font-size:11px;" +
            "-fx-background-radius:999;" +
            "-fx-padding:4 10;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(8, id, spacer, status);
        header.setAlignment(Pos.CENTER_LEFT);

        String tecnico = o.getTecnicoResponsavel() == null ? "-" : safe(o.getTecnicoResponsavel().getNome());

        Label info = subtitle(
            "👷 Técnico: " + tecnico +
            " | 💰 Peças: R$ " + o.getValorTotalPecas() +
            " | 🧾 Pagamento: " + safe(o.getTipoPagamento())
        );

        VBox card = new VBox(8, header, info);
        card.setPadding(new Insets(14));
        card.setStyle(cardSoft());

        return card;
    }

    private VBox listCard(String iconText, String titulo, String... detalhes) {
        Label icon = new Label(iconText);
        icon.setFont(Font.font("Segoe UI", 22));
        icon.setMinWidth(40);

        Label tituloLabel = new Label(titulo);
        tituloLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        tituloLabel.setStyle("-fx-text-fill:#1e293b;");

        VBox textos = new VBox(3, tituloLabel);

        for (String detalhe : detalhes) {
            if (detalhe == null || detalhe.isBlank()) continue;

            Label det = new Label(detalhe);
            det.setStyle("-fx-text-fill:#6b7280; -fx-font-size:12px;");
            textos.getChildren().add(det);
        }

        HBox conteudo = new HBox(12, icon, textos);
        conteudo.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(conteudo);
        card.setPadding(new Insets(12, 16, 12, 16));
        card.setStyle(cardSoft());

        card.setOnMouseEntered(e -> card.setStyle(cardHover()));
        card.setOnMouseExited(e -> card.setStyle(cardSoft()));

        return card;
    }

    private List<OrdemDeServico> carregarTodasAsOs() {
        try {
            return DASHBOARD_PROVIDER != null ? DASHBOARD_PROVIDER.carregarOrdens() : Collections.emptyList();
        } catch (Exception ex) {
            alert("Erro", ex.getMessage());
            return Collections.emptyList();
        }
    }

    private BigDecimal parseDecimal(String value) {
        if (value == null || value.isBlank()) {
            return BigDecimal.ZERO;
        }

        String limpo = value.trim().replace(".", "").replace(",", ".");

        try {
            return new BigDecimal(limpo);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Valor inválido: " + value);
        }
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }

    private void setCenter(Node node) {
        ScrollPane scroll = new ScrollPane(node);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background:#FFFFFF; -fx-background-color:#f8fafc;");

        fade(scroll);
        root.setCenter(scroll);
    }

    private void fade(Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(180), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    private VBox page() {
        VBox v = new VBox(20);
        v.setPadding(new Insets(40));
        v.setStyle("-fx-background-color:#f8fafc;");
        return v;
    }

    private TextField input(String prompt) {
        TextField t = new TextField();
        t.setPromptText(prompt);
        t.setPrefHeight(44);
        t.setMaxWidth(Double.MAX_VALUE);
        t.setStyle(inputStyle());
        hoverFocus(t);
        return t;
    }

    private PasswordField password(String prompt) {
        PasswordField t = new PasswordField();
        t.setPromptText(prompt);
        t.setPrefHeight(44);
        t.setMaxWidth(Double.MAX_VALUE);
        t.setStyle(inputStyle());
        hoverFocus(t);
        return t;
    }

    private TextArea area(String prompt) {
        TextArea t = new TextArea();
        t.setPromptText(prompt);
        t.setStyle(inputStyle());
        hoverFocus(t);
        return t;
    }

    private Button primary(String text) {
        Button b = new Button(text);
        b.setPrefHeight(46);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setStyle(btn());
        hoverButton(b);
        return b;
    }

    private Button menuBtn(String text, Runnable action) {
        Button b = new Button(text);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setPrefHeight(42);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setStyle(menuBtn());
        b.setOnAction(e -> action.run());
        hoverMenu(b);
        return b;
    }

    private Label title(String text, int size) {
        Label label = new Label(text);
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, size));
        label.setStyle("-fx-text-fill:#0f172a;");
        return label;
    }

    private Label subtitle(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill:#64748b; -fx-font-size:12px;");
        label.setWrapText(true);
        return label;
    }

    private void alert(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, message);
        a.setTitle(title);
        a.setHeaderText(null);
        a.showAndWait();
    }

    private void hoverButton(Button button) {
        button.setOnMouseEntered(e -> button.setStyle(btnHover()));
        button.setOnMouseExited(e -> button.setStyle(btn()));
    }

    private void hoverMenu(Button button) {
        button.setOnMouseEntered(e -> button.setStyle(menuBtnHover()));
        button.setOnMouseExited(e -> button.setStyle(menuBtn()));
    }

    private void hoverFocus(Control control) {
        control.focusedProperty().addListener((obs, old, focused) -> {
            control.setStyle(focused ? inputFocus() : inputStyle());
        });
    }

    private String inputStyle() {
        return "-fx-background-color:white;" +
            "-fx-background-radius:10;" +
            "-fx-border-radius:10;" +
            "-fx-border-color:#FFFFFF;" +
            "-fx-border-width:1;" +
            "-fx-padding:12;" +
            "-fx-font-size:13px;";
    }

    private String inputFocus() {
        return "-fx-background-color:white;" +
            "-fx-background-radius:10;" +
            "-fx-border-radius:10;" +
            "-fx-border-color:#2563eb;" +
            "-fx-border-width:1.5;" +
            "-fx-padding:12;" +
            "-fx-font-size:13px;";
    }

    private String btn() {
        return "-fx-background-color:#950606;" +
            "-fx-text-fill:white;" +
            "-fx-font-weight:bold;" +
            "-fx-background-radius:10;" +
            "-fx-font-size:13px;";
    }

    private String btnHover() {
        return "-fx-background-color:#9B111E;" +
            "-fx-text-fill:white;" +
            "-fx-font-weight:bold;" +
            "-fx-background-radius:10;" +
            "-fx-font-size:13px;";
    }

    private String menuBtn() {
        return "-fx-background-color:transparent;" +
            "-fx-text-fill:#f8fafc;" +
            "-fx-font-size:14px;" +
            "-fx-padding:0 14;" +
            "-fx-background-radius:10;";
    }

    private String menuBtnHover() {
        return "-fx-background-color:#9B111E;" +
            "-fx-text-fill:white;" +
            "-fx-font-size:14px;" +
            "-fx-padding:0 14;" +
            "-fx-background-radius:10;";
    }

    private String card() {
        return "-fx-background-color:white;" +
            "-fx-background-radius:16;";
    }

    private String cardBorder() {
        return card() +
            "-fx-border-color:#e5e7eb;" +
            "-fx-border-radius:16;" +
            "-fx-effect:dropshadow(gaussian, rgba(15,23,42,0.05), 12, 0, 0, 2);";
    }

    private String cardSoft() {
        return "-fx-background-color:white;" +
            "-fx-background-radius:14;" +
            "-fx-border-radius:14;" +
            "-fx-border-color:#e5e7eb;" +
            "-fx-border-width:1;" +
            "-fx-effect:dropshadow(gaussian, rgba(15,23,42,0.04), 8, 0, 0, 1);";
    }

    private String cardHover() {
        return "-fx-background-color:#f8fafc;" +
            "-fx-background-radius:14;" +
            "-fx-border-radius:14;" +
            "-fx-border-color:#9B111E;" +
            "-fx-border-width:1;" +
            "-fx-effect:dropshadow(gaussian, rgba(15,23,42,0.08), 12, 0, 0, 2);";
    }
}