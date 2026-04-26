package view;

import Exception.InvalidArgumentException;
import Classe.*;
import dominio.enums.StatusOrdemServico;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class MainView extends Application {

    private BorderPane root;
    private VBox drawer;
    private final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

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

    private enum DashboardFiltro { ATRASADAS, EM_ANDAMENTO, CONCLUIDAS }

    @FunctionalInterface public interface LoginHandler { boolean autenticar(String login, String senha) throws Exception; }
    @FunctionalInterface public interface DeletarTecnicoHandler { void deletar(int id) throws Exception; }
    @FunctionalInterface public interface DashboardDataProvider { List<OrdemDeServico> carregarOrdens() throws Exception; }
    @FunctionalInterface public interface EquipamentosProvider { List<Equipamento> listarEquipamentos() throws Exception; }
    @FunctionalInterface public interface DeletarEquipamentoHandler { void deletar(int equipamentoId) throws Exception; }
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
    @FunctionalInterface public interface CadastrarTecnicoHandler { void cadastrar(String nome, String documento, java.time.LocalDate dataAssociacao) throws Exception; }
    @FunctionalInterface public interface ClientesProvider { List<Cliente> listarClientes() throws Exception; }
    @FunctionalInterface public interface SalvarEquipamentoHandler { void salvar(String nome, String modelo, String documentoCliente) throws Exception; }
    @FunctionalInterface public interface SalvarClienteHandler { void salvar(String nome, String telefone, String email, String Cidade, String CEP, String documento) throws Exception; }
    @FunctionalInterface public interface DeletarClienteHandler { void deletar(String documento) throws Exception; }
    @FunctionalInterface public interface ExportarOrcamentoPdfHandler { void exportar(Orçamento orcamento) throws Exception; }
    @FunctionalInterface public interface ExportarOsPdfHandler { void exportar(Orçamento orcamento, OrdemDeServico ordemDeServico) throws Exception; }
    @FunctionalInterface public interface OrcamentosPorEquipamentoProvider { List<Orçamento> listarPorEquipamento(Equipamento equipamento) throws Exception; }
    @FunctionalInterface public interface EquipamentosPorClienteProvider { List<Equipamento> listarPorCliente(Cliente cliente) throws Exception; }

    public static void setLoginHandler(LoginHandler handler)                         { LOGIN_HANDLER = handler; }
    public static void setDashboardProvider(DashboardDataProvider provider)          { DASHBOARD_PROVIDER = provider; }
    public static void setDeletarClienteHandler(DeletarClienteHandler h)             { DELETAR_CLIENTE_HANDLER = h; }
    public static void setDeletarEquipamentoHandler(DeletarEquipamentoHandler h)     { DELETAR_EQUIPAMENTO_HANDLER = h; }
    public static void setDeletarTecnicoHandler(DeletarTecnicoHandler h)             { DELETAR_TECNICO_HANDLER = h; }
    public static void setEquipamentosProvider(EquipamentosProvider p)               { EQUIPAMENTOS_PROVIDER = p; }
    public static void setSalvarEquipamentoHandler(SalvarEquipamentoHandler h)       { SALVAR_EQUIPAMENTO_HANDLER = h; }
    public static void setOsProvider(OsProvider p)                                   { OS_PROVIDER = p; }
    public static void setOrcamentosAprovadosProvider(OrcamentosAprovadosProvider p) { ORCAMENTOS_APROVADOS_PROVIDER = p; }
    public static void setIniciarOsHandler(IniciarOsHandler h)                       { INICIAR_OS_HANDLER = h; }
    public static void setConcluirOsHandler(ConcluirOsHandler h)                     { CONCLUIR_OS_HANDLER = h; }
    public static void setCriarOsHandler(CriarOsHandler h)                           { CRIAR_OS_HANDLER = h; }
    public static void setTecnicosProvider(TecnicosProvider p)                       { TECNICOS_PROVIDER = p; }
    public static void setOrcamentosPorTecnicoProvider(OrcamentosPorTecnicoProvider p){ ORCAMENTOS_PROVIDER = p; }
    public static void setCriarOrcamentoHandler(CriarOrcamentoHandler h)             { CRIAR_ORCAMENTO_HANDLER = h; }
    public static void setAprovarOrcamentoHandler(AprovarOrcamentoHandler h)         { APROVAR_ORCAMENTO_HANDLER = h; }
    public static void setReprovarOrcamentoHandler(ReprovarOrcamentoHandler h)       { REPROVAR_ORCAMENTO_HANDLER = h; }
    public static void setClientesProvider(ClientesProvider p)                       { CLIENTES_PROVIDER = p; }
    public static void setSalvarClienteHandler(SalvarClienteHandler h)               { SALVAR_CLIENTE_HANDLER = h; }
    public static void setCadastrarTecnicoHandler(CadastrarTecnicoHandler h)         { CADASTRAR_TECNICO_HANDLER = h; }
    public static void setExportarOrcamentoPdfHandler(ExportarOrcamentoPdfHandler h) { EXPORTAR_ORCAMENTO_PDF_HANDLER = h; }
    public static void setExportarOsPdfHandler(ExportarOsPdfHandler h)               { EXPORTAR_OS_PDF_HANDLER = h; }
    public static void setOrcamentosPorEquipamentoProvider(OrcamentosPorEquipamentoProvider p) { ORCAMENTOS_POR_EQUIPAMENTO_PROVIDER = p; }
    public static void setEquipamentosPorClienteProvider(EquipamentosPorClienteProvider p)     { EQUIPAMENTOS_POR_CLIENTE_PROVIDER = p; }

    // ══════════════════════════════════════════════════════════════════════
    // CARD AUXILIAR GENÉRICO
    // ══════════════════════════════════════════════════════════════════════

    private VBox listCard(String iconText, String titulo, String... detalhes) {
        Label icon = new Label(iconText);
        icon.setFont(Font.font("Segoe UI", 22));
        icon.setMinWidth(40);

        Label tituloLabel = new Label(titulo);
        tituloLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        tituloLabel.setStyle("-fx-text-fill:#1e293b;");

        VBox textos = new VBox(3, tituloLabel);
        for (String d : detalhes) {
            if (d == null || d.isBlank()) continue;
            Label det = new Label(d);
            det.setStyle("-fx-text-fill:#6b7280; -fx-font-size:12px;");
            textos.getChildren().add(det);
        }

        HBox conteudo = new HBox(12, icon, textos);
        conteudo.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(conteudo);
        card.setPadding(new Insets(12, 16, 12, 16));
        card.setStyle(cardStyle("#e5e7eb", "white"));

        card.setOnMouseEntered(e -> card.setStyle(cardStyle("#9B111E", "#f8fafc")));
        card.setOnMouseExited(e  -> card.setStyle(cardStyle("#e5e7eb", "white")));

        return card;
    }

    // ══════════════════════════════════════════════════════════════════════
    // CARD DE OS PARA O DRAWER DO DASHBOARD
    // ══════════════════════════════════════════════════════════════════════

    private VBox osCard(OrdemDeServico os, DashboardFiltro filtro) {

        String icon, accentColor, badgeText, badgeBg;

        switch (filtro) {
            case ATRASADAS -> {
                icon = "🔴"; accentColor = "#dc2626";
                badgeText = "ATRASADA";    badgeBg = "#fef2f2";
            }
            case EM_ANDAMENTO -> {
                icon = "🔵"; accentColor = "#2563eb";
                badgeText = "EM ANDAMENTO"; badgeBg = "#eff6ff";
            }
            default -> {
                icon = "🟢"; accentColor = "#16a34a";
                badgeText = "CONCLUÍDA";   badgeBg = "#f0fdf4";
            }
        }

        Label badge = new Label(badgeText);
        badge.setStyle(
            "-fx-background-color:" + badgeBg + ";" +
            "-fx-text-fill:" + accentColor + ";" +
            "-fx-font-size:11px;" +
            "-fx-font-weight:bold;" +
            "-fx-padding:3 10;" +
            "-fx-background-radius:20;"
        );

        Label osId = new Label("OS #" + os.getId());
        osId.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        osId.setStyle("-fx-text-fill:#1e293b;");

        HBox tituloRow = new HBox(10, osId, badge);
        tituloRow.setAlignment(Pos.CENTER_LEFT);

        String nomeEq = (os.getEquipamento() != null) ? os.getEquipamento().getNome() : "-";
        Label equipLabel = new Label("🖥️  " + nomeEq);
        equipLabel.setStyle("-fx-text-fill:#374151; -fx-font-size:12px;");

        String abertura  = os.getDataAbertura()          == null ? "-" : os.getDataAbertura().format(DT_FMT);
        String previsto  = os.getDataFechamentoPrevisto() == null ? "-" : os.getDataFechamentoPrevisto().format(DT_FMT);
        String concluida = os.getDataFechamentoReal()     == null ? "-" : os.getDataFechamentoReal().format(DT_FMT);

        Label lblAbertura  = new Label("📅 Abertura: " + abertura);
        lblAbertura.setStyle("-fx-text-fill:#6b7280; -fx-font-size:10px;");
        Label lblPrevisto  = new Label("⏰ Previsto:  " + previsto);
        lblPrevisto.setStyle("-fx-text-fill:#6b7280; -fx-font-size:10px;");
        Label lblConcluida = new Label("✅ Concluída: " + concluida);
        lblConcluida.setStyle("-fx-text-fill:#6b7280; -fx-font-size:10px;");

        HBox datasRow = new HBox(20, lblAbertura, lblPrevisto, lblConcluida);

        VBox info = new VBox(6, tituloRow, equipLabel, datasRow);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font("Segoe UI", 20));
        iconLabel.setMinWidth(36);
        iconLabel.setAlignment(Pos.TOP_CENTER);

        HBox conteudo = new HBox(12, iconLabel, info);
        conteudo.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(conteudo);
        card.setPadding(new Insets(14, 16, 14, 16));
        card.setStyle(osCardStyle(accentColor, "white"));

        card.setOnMouseEntered(e -> card.setStyle(osCardStyle(accentColor, "#f8fafc")));
        card.setOnMouseExited(e  -> card.setStyle(osCardStyle(accentColor, "white")));

        return card;
    }

    private String cardStyle(String border, String bg) {
        return "-fx-background-color:" + bg + ";" +
               "-fx-background-radius:10;" +
               "-fx-border-color:" + border + ";" +
               "-fx-border-radius:10;" +
               "-fx-border-width:1;";
    }

    private String osCardStyle(String accent, String bg) {
        return "-fx-background-color:" + bg + ";" +
               "-fx-background-radius:10;" +
               "-fx-border-color:#e5e7eb;" +
               "-fx-border-left-color:" + accent + ";" +
               "-fx-border-radius:10;" +
               "-fx-border-width:1 1 1 4;";
    }

    // ══════════════════════════════════════════════════════════════════════
    // START / LOGIN / APP
    // ══════════════════════════════════════════════════════════════════════

    @Override
    public void start(Stage stage) {
        stage.setTitle("CONTRIMAQ • Service Desk");
        stage.setScene(login());
        stage.show();
    }

    private Scene login() {
        VBox card = new VBox(18);
        card.setPadding(new Insets(40));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(380);
        card.setStyle(card());

        Label logo = title("CONTRIMAQ", 30);
        Label sub  = subtitle("Service Management");

        TextField     user = input("Usuário");
        PasswordField pass = password("Senha");

        Button entrar = primary("Entrar");
        entrar.setOnAction(e -> {
            try {
                if (LOGIN_HANDLER == null) { alert("Erro", "LoginHandler não configurado."); return; }
                boolean ok = LOGIN_HANDLER.autenticar(user.getText(), pass.getText());
                if (ok) { Stage s = (Stage) entrar.getScene().getWindow(); s.setScene(app()); }
                else    { alert("Erro", "Login inválido"); }
            } catch (Exception ex) { alert("Erro", ex.getMessage()); }
        });

        card.getChildren().addAll(logo, sub, user, pass, entrar);
        StackPane bg = new StackPane(card);
        bg.setStyle("-fx-background-color:#f2f4f8;");
        return new Scene(bg);
    }

    private Scene app() throws InvalidArgumentException {
        root = new BorderPane();
        root.setStyle("-fx-background-color:#f2f4f8;");
        root.setLeft(menu());
        setCenter(dashboard());
        return new Scene(root, 1400, 850);
    }

    private VBox menu() throws InvalidArgumentException {
        VBox m = new VBox(20);
        m.setPadding(new Insets(30));
        m.setPrefWidth(260);
        m.setStyle("-fx-background-color:#9B111E;");

        Label logo = title("CONTRIMAQ", 22);
        logo.setStyle("-fx-text-fill:white;");

        m.getChildren().addAll(
            logo,
            menuBtn("📊 Dashboard",    () -> setCenter(dashboard())),
            menuBtn("👤 Clientes",     () -> setCenter(cliente())),
            menuBtn("🛠 Equipamentos", () -> setCenter(equipamento())),
            menuBtn("🧑‍🔧 Técnicos",  () -> setCenter(tecnico())),
            menuBtn("📄 Orçamento",    () -> setCenter(orcamentos())),
            menuBtn("📄 Ordens",       () -> setCenter(ordemDeServico()))
        );
        return m;
    }

    // ══════════════════════════════════════════════════════════════════════
    // DASHBOARD
    // ══════════════════════════════════════════════════════════════════════

    private VBox dashboard() {
        VBox v = page();

        List<OrdemDeServico> todas = Collections.emptyList();
        try {
            if (DASHBOARD_PROVIDER != null) todas = DASHBOARD_PROVIDER.carregarOrdens();
        } catch (Exception ex) { alert("Erro", ex.getMessage()); }

        Label titulo = title("Dashboard", 22);
        Label sub    = subtitle("Fila e prioridade");

        List<OrdemDeServico> todasFinal = todas;
        HBox cards = new HBox(20,
            cardDashboard("🔴  Atrasadas",    DashboardFiltro.ATRASADAS,    todasFinal),
            cardDashboard("🔵  Em andamento", DashboardFiltro.EM_ANDAMENTO, todasFinal),
            cardDashboard("🟢  Concluídas",   DashboardFiltro.CONCLUIDAS,   todasFinal)
        );

        Label hint = subtitle("Clique em um card para abrir a lista detalhada.");
        v.getChildren().addAll(titulo, sub, cards, hint);
        return v;
    }

    private VBox cardDashboard(String titulo, DashboardFiltro filtro, List<OrdemDeServico> todas) {
        long count = filtrarDashboard(todas, filtro).size();

        String cor = switch (filtro) {
            case ATRASADAS    -> "#dc2626";
            case EM_ANDAMENTO -> "#2563eb";
            case CONCLUIDAS   -> "#16a34a";
        };

        Label numLabel = title(String.valueOf(count), 28);
        numLabel.setStyle("-fx-text-fill:" + cor + ";");

        VBox b = new VBox(6, subtitle(titulo), numLabel);
        b.setPadding(new Insets(25));
        b.setStyle(card());

        b.setOnMouseClicked(e -> openDrawer(buildOsCardsDrawer(titulo.replaceAll("^.{2}\\s*", ""), filtro, todas)));
        b.setOnMouseEntered(e -> b.setStyle(card() + "-fx-border-color:" + cor + ";-fx-border-width:2;"));
        b.setOnMouseExited(e  -> b.setStyle(card()));
        return b;
    }

    private Node buildOsCardsDrawer(String titulo, DashboardFiltro filtro, List<OrdemDeServico> todas) {

        String cor = switch (filtro) {
            case ATRASADAS    -> "#dc2626";
            case EM_ANDAMENTO -> "#2563eb";
            case CONCLUIDAS   -> "#16a34a";
        };

        Label tituloLabel = title(titulo, 18);
        tituloLabel.setStyle("-fx-text-fill:" + cor + ";");

        Button fechar = new Button("✕ Fechar");
        fechar.setOnAction(e -> closeDrawer());
        fechar.setStyle(
            "-fx-background-color:transparent;" +
            "-fx-text-fill:#2563eb;" +
            "-fx-font-weight:bold;" +
            "-fx-font-size:13px;" +
            "-fx-cursor:hand;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(10, tituloLabel, spacer, fechar);
        header.setAlignment(Pos.CENTER_LEFT);

        List<OrdemDeServico> filtradas = filtrarDashboard(todas, filtro);
        Label contador = new Label(filtradas.size() + " ordem(ns) encontrada(s)");
        contador.setStyle("-fx-text-fill:#9ca3af; -fx-font-size:12px;");

        VBox listaCards = new VBox(10);

        if (filtradas.isEmpty()) {
            Label vazio = new Label("✅  Nenhuma ordem nesta categoria.");
            vazio.setStyle("-fx-text-fill:#9ca3af; -fx-font-size:13px;");
            listaCards.getChildren().add(vazio);
        } else {
            for (OrdemDeServico os : filtradas) {
                VBox card = osCard(os, filtro);

                if (filtro == DashboardFiltro.EM_ANDAMENTO && CONCLUIR_OS_HANDLER != null) {
                    Button btnConcluir = new Button("✔  Concluir OS");
                    btnConcluir.setStyle(
                        "-fx-background-color:#16a34a;" +
                        "-fx-text-fill:white;" +
                        "-fx-font-weight:bold;" +
                        "-fx-background-radius:8;" +
                        "-fx-font-size:12px;" +
                        "-fx-padding:6 14;"
                    );
                    btnConcluir.setOnMouseEntered(ev -> btnConcluir.setStyle(
                        "-fx-background-color:#15803d;" +
                        "-fx-text-fill:white;" +
                        "-fx-font-weight:bold;" +
                        "-fx-background-radius:8;" +
                        "-fx-font-size:12px;" +
                        "-fx-padding:6 14;"
                    ));
                    btnConcluir.setOnMouseExited(ev -> btnConcluir.setStyle(
                        "-fx-background-color:#16a34a;" +
                        "-fx-text-fill:white;" +
                        "-fx-font-weight:bold;" +
                        "-fx-background-radius:8;" +
                        "-fx-font-size:12px;" +
                        "-fx-padding:6 14;"
                    ));
                    btnConcluir.setDisable(os.getStatus() != StatusOrdemServico.EM_ANDAMENTO);
                    btnConcluir.setOnAction(ev -> {
                        try {
                            CONCLUIR_OS_HANDLER.concluir(
                                os.getId(), "Concluída via dashboard",
                                os.getEquipamento(), os.getOrcamentoAprovado()
                            );
                            setCenter(dashboard());
                        } catch (Exception ex) { alert("Erro", ex.getMessage()); }
                    });

                    HBox acoes = new HBox(btnConcluir);
                    acoes.setAlignment(Pos.CENTER_RIGHT);
                    acoes.setPadding(new Insets(8, 0, 0, 0));
                    card.getChildren().add(acoes);
                }

                listaCards.getChildren().add(card);
            }
        }

        ScrollPane scroll = new ScrollPane(listaCards);
        scroll.setFitToWidth(true);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background-color:transparent; -fx-background:transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        VBox box = new VBox(12, header, contador, scroll);
        box.setPadding(new Insets(4));
        VBox.setVgrow(box, Priority.ALWAYS);
        return box;
    }

    private List<OrdemDeServico> filtrarDashboard(List<OrdemDeServico> todas, DashboardFiltro filtro) {
        if (todas == null) return List.of();
        return todas.stream().filter(os -> {
            if (os == null || os.getStatus() == null) return false;
            return switch (filtro) {
                case ATRASADAS    -> os.estaAtrasada();
                case EM_ANDAMENTO -> os.getStatus() == StatusOrdemServico.EM_ANDAMENTO;
                case CONCLUIDAS   -> os.getStatus() == StatusOrdemServico.CONCLUIDA;
            };
        }).toList();
    }

    private void openDrawer(Node content) {
        if (drawer == null) {
            drawer = new VBox();
            drawer.setPrefWidth(600);
            drawer.setStyle("-fx-background-color:white;-fx-border-color:#e5e7eb;-fx-border-width:0 0 0 1;");
            drawer.setPadding(new Insets(20));
        }
        VBox.setVgrow(content, Priority.ALWAYS);
        drawer.getChildren().setAll(content);
        root.setRight(drawer);
        fade(drawer);
    }

    private void closeDrawer() { root.setRight(null); }

    // ══════════════════════════════════════════════════════════════════════
    // TÉCNICOS
    // ══════════════════════════════════════════════════════════════════════

    private VBox tecnico() {
        VBox v = page();

        Label titulo = title("Técnicos", 22);
        Label sub    = subtitle("Cadastro e listagem de técnicos do sistema");

        TextField nome      = input("Nome do técnico");
        TextField documento = input("Documento");
        Button salvar  = primary("Salvar Técnico");
        Button deletar = primary("Deletar Técnico");

        ListView<Tecnico> lista = new ListView<>();
        lista.setPrefHeight(450);
        lista.setStyle("-fx-background-color:transparent; -fx-border-color:transparent;");

        lista.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Tecnico t, boolean empty) {
                super.updateItem(t, empty);
                setStyle("-fx-background-color:transparent; -fx-padding:4 0;");
                if (empty || t == null) { setGraphic(null); setText(null); return; }
                VBox card = listCard("🧑‍🔧", t.getNome(),
                    "📄 " + t.getDocumento(),
                    "📅 Desde: " + t.getDataAssociacao());
                card.prefWidthProperty().bind(lv.widthProperty().subtract(20));
                setGraphic(card); setText(null);
            }
        });

        try {
            lista.setItems(TECNICOS_PROVIDER != null
                ? FXCollections.observableArrayList(TECNICOS_PROVIDER.listarTecnicos())
                : FXCollections.observableArrayList());
        } catch (Exception ex) { alert("Erro", ex.getMessage()); lista.setItems(FXCollections.observableArrayList()); }

        salvar.setOnAction(e -> {
            try {
                if (CADASTRAR_TECNICO_HANDLER == null) return;
                CADASTRAR_TECNICO_HANDLER.cadastrar(nome.getText(), documento.getText(), java.time.LocalDate.now());
                if (TECNICOS_PROVIDER != null)
                    lista.setItems(FXCollections.observableArrayList(TECNICOS_PROVIDER.listarTecnicos()));
                nome.clear(); documento.clear();
            } catch (Exception ex) { alert("Erro", ex.getMessage()); }
        });

        deletar.setDisable(true);
        lista.getSelectionModel().selectedItemProperty().addListener((obs, o, n) ->
            deletar.setDisable(n == null || DELETAR_TECNICO_HANDLER == null));

        deletar.setOnAction(e -> {
            try {
                if (DELETAR_TECNICO_HANDLER == null) return;
                Tecnico sel = lista.getSelectionModel().getSelectedItem();
                if (sel == null) throw new IllegalArgumentException("Selecione um técnico");
                DELETAR_TECNICO_HANDLER.deletar(sel.getId());
                if (TECNICOS_PROVIDER != null)
                    lista.setItems(FXCollections.observableArrayList(TECNICOS_PROVIDER.listarTecnicos()));
            } catch (Exception ex) { alert("Erro", ex.getMessage()); }
        });

        v.getChildren().addAll(titulo, sub, nome, documento, salvar, deletar, lista);
        return v;
    }

    // ══════════════════════════════════════════════════════════════════════
    // ORÇAMENTOS
    // ══════════════════════════════════════════════════════════════════════

    private VBox orcamentos() {
        VBox v = page();

        Label titulo = title("Orçamentos", 22);
        Label sub    = subtitle("Criação e aprovação de orçamentos por técnico");

        ComboBox<Tecnico> tecnicos = new ComboBox<>();
        tecnicos.setPromptText("Selecione o técnico");
        try {
            tecnicos.setItems(TECNICOS_PROVIDER != null
                ? FXCollections.observableArrayList(TECNICOS_PROVIDER.listarTecnicos())
                : FXCollections.observableArrayList());
        } catch (Exception ex) { alert("Erro", ex.getMessage()); tecnicos.setItems(FXCollections.observableArrayList()); }

        TextField maodeObra = input("Valor da Mão de Obra");
        TextField pagamento = input("Tipo de pagamento");

        VBox painelItens = new VBox(6);
        Runnable addLinha = () -> {
            TextField desc = input("Descrição da peça");
            TextField vlr  = input("Valor");
            Button remov   = new Button("✕");
            remov.setStyle("-fx-background-color:transparent;-fx-text-fill:#cc0000;-fx-font-size:13px;-fx-cursor:hand;");

            HBox linha = new HBox(8, desc, vlr, remov);
            linha.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(desc, Priority.ALWAYS);
            vlr.setPrefWidth(110);

            remov.setOnAction(ev -> {
                if (painelItens.getChildren().size() > 1) painelItens.getChildren().remove(linha);
                else alert("Aviso", "O orçamento precisa ter ao menos uma peça.");
            });
            painelItens.getChildren().add(linha);
        };
        addLinha.run();

        Button btnAddItem  = primary("+ Adicionar peça");
        btnAddItem.setOnAction(e -> addLinha.run());
        Button criar       = primary("Criar orçamento");
        Button exportarPdf = primary("Exportar para PDF");

        ListView<Orçamento> lista = new ListView<>();
        lista.setPrefHeight(350);
        lista.setStyle("-fx-background-color:transparent; -fx-border-color:transparent;");

        tecnicos.setOnAction(e -> {
            Tecnico t = tecnicos.getValue();
            if (t == null) return;
            try {
                lista.setItems(ORCAMENTOS_PROVIDER != null
                    ? FXCollections.observableArrayList(ORCAMENTOS_PROVIDER.listarPorTecnico(t))
                    : FXCollections.observableArrayList());
            } catch (Exception ex) { alert("Erro", ex.getMessage()); lista.setItems(FXCollections.observableArrayList()); }
        });

        criar.setOnAction(e -> {
            try {
                if (CRIAR_ORCAMENTO_HANDLER == null) return;
                Tecnico t = tecnicos.getValue();
                if (t == null)                    throw new IllegalArgumentException("Selecione um técnico");
                if (maodeObra.getText().isBlank()) throw new IllegalArgumentException("Informe o valor da mão de obra");
                if (pagamento.getText().isBlank()) throw new IllegalArgumentException("Informe o tipo de pagamento");

                List<ItemOrcamento> itens = new java.util.ArrayList<>();
                for (javafx.scene.Node node : painelItens.getChildren()) {
                    HBox linha = (HBox) node;
                    String desc = ((TextField) linha.getChildren().get(0)).getText().trim();
                    String vlr  = ((TextField) linha.getChildren().get(1)).getText().trim();
                    if (desc.isBlank() && vlr.isBlank()) continue;
                    if (desc.isBlank()) throw new IllegalArgumentException("Preencha a descrição de todas as peças");
                    if (vlr.isBlank())  throw new IllegalArgumentException("Preencha o valor de todas as peças");
                    itens.add(new ItemOrcamento(desc, new BigDecimal(vlr)));
                }
                if (itens.isEmpty()) throw new IllegalArgumentException("Adicione ao menos uma peça ao orçamento");

                CRIAR_ORCAMENTO_HANDLER.criar(itens, pagamento.getText(), t, new BigDecimal(maodeObra.getText()));
                tecnicos.fireEvent(new ActionEvent());
                maodeObra.clear(); pagamento.clear();
                painelItens.getChildren().clear(); addLinha.run();
            } catch (Exception ex) { alert("Erro", ex.getMessage()); }
        });

        lista.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Orçamento o, boolean empty) {
                super.updateItem(o, empty);
                setStyle("-fx-background-color:transparent; -fx-padding:4 0;");
                if (empty || o == null) { setGraphic(null); setText(null); return; }
                String statusIcon = switch (o.getStatus().toString().toUpperCase()) {
                    case "APROVADO"  -> "✅";
                    case "REPROVADO" -> "❌";
                    default          -> "⏳";
                };
                VBox card = listCard(statusIcon, "Orçamento #" + o.getId(),
                    "💰 Total peças: R$ " + o.getValorTotalPecas(),
                    "📋 Status: " + o.getStatus());
                card.prefWidthProperty().bind(lv.widthProperty().subtract(20));
                setGraphic(card); setText(null);
            }
        });

        Button aprovar  = primary("Aprovar");
        Button reprovar = primary("Reprovar");
        aprovar.setDisable(true); reprovar.setDisable(true); exportarPdf.setDisable(true);

        lista.getSelectionModel().selectedItemProperty().addListener((o, a, n) -> {
            boolean pendente = n != null && n.estaPendente();
            aprovar.setDisable(!pendente || APROVAR_ORCAMENTO_HANDLER == null);
            reprovar.setDisable(!pendente || REPROVAR_ORCAMENTO_HANDLER == null);
            exportarPdf.setDisable(n == null);
        });

        aprovar.setOnAction(e -> {
            try {
                if (APROVAR_ORCAMENTO_HANDLER == null) return;
                Orçamento o = lista.getSelectionModel().getSelectedItem();
                Tecnico t   = tecnicos.getValue();
                if (o == null) throw new IllegalArgumentException("Selecione um orçamento");
                if (t == null) throw new IllegalArgumentException("Selecione um técnico");
                APROVAR_ORCAMENTO_HANDLER.aprovar(o.getId(), t);
                tecnicos.fireEvent(new ActionEvent());
            } catch (Exception ex) { alert("Erro", ex.getMessage()); }
        });

        reprovar.setOnAction(e -> {
            try {
                if (REPROVAR_ORCAMENTO_HANDLER == null) return;
                Orçamento o = lista.getSelectionModel().getSelectedItem();
                Tecnico t   = tecnicos.getValue();
                if (o == null) throw new IllegalArgumentException("Selecione um orçamento");
                if (t == null) throw new IllegalArgumentException("Selecione um técnico");
                REPROVAR_ORCAMENTO_HANDLER.reprovar(o.getId(), t);
                tecnicos.fireEvent(new ActionEvent());
            } catch (Exception ex) { alert("Erro", ex.getMessage()); }
        });

        exportarPdf.setOnAction(e -> {
            try {
                Orçamento orcSel = lista.getSelectionModel().getSelectedItem();
                if (orcSel == null) throw new IllegalArgumentException("Selecione um orçamento para exportar");
                if (EXPORTAR_ORCAMENTO_PDF_HANDLER == null) { alert("Exportar PDF", "Handler não configurado."); return; }
                alert("Exportar PDF", "PDF baixado com sucesso em C:\\Programing\\OS");
                EXPORTAR_ORCAMENTO_PDF_HANDLER.exportar(orcSel);
            } catch (Exception ex) { alert("Erro", ex.getMessage()); }
        });

        v.getChildren().addAll(titulo, sub, tecnicos, maodeObra, pagamento,
            painelItens, btnAddItem, criar, exportarPdf, lista, aprovar, reprovar);

        ScrollPane scroll = new ScrollPane(v);
        scroll.setFitToWidth(true);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background-color:transparent; -fx-background:transparent;");
        VBox outer = new VBox(scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        return outer;
    }

    // ══════════════════════════════════════════════════════════════════════
    // CLIENTES  ← tela refatorada com hierarquia expansível
    // ══════════════════════════════════════════════════════════════════════

    private VBox cliente() {
        VBox v = page();

        Label titulo = title("Clientes", 22);
        Label sub    = subtitle("Cadastro e listagem de clientes · clique em um cliente para ver equipamentos, orçamentos e OS");

        TextField nome      = input("Nome");
        TextField telefone  = input("Telefone");
        TextField email     = input("Email");
        TextField cidade    = input("Cidade");
        TextField cep       = input("CEP");
        TextField documento = input("CPF / CNPJ");
        Button salvar  = primary("Salvar Cliente");
        Button deletar = primary("Deletar Cliente");
        deletar.setDisable(true);

        // painel de detalhe hierárquico (direita)
        VBox detalhePane = new VBox(10);
        detalhePane.setPadding(new Insets(0, 0, 0, 8));
        detalhePane.setVisible(false);
        detalhePane.setManaged(false);

        // lista de clientes (esquerda)
        ListView<Cliente> lista = new ListView<>();
        lista.setPrefHeight(500);
        lista.setStyle("-fx-background-color:transparent; -fx-border-color:transparent;");

        lista.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Cliente c, boolean empty) {
                super.updateItem(c, empty);
                setStyle("-fx-background-color:transparent; -fx-padding:4 0;");
                if (empty || c == null) { setGraphic(null); setText(null); return; }
                VBox card = listCard("👤", c.getNome(),
                    "📄 " + c.getDocumento(),
                    c.getTelefone() != null ? "📞 " + c.getTelefone() : null,
                    c.getEmail()    != null ? "✉️ "  + c.getEmail()   : null);
                card.prefWidthProperty().bind(lv.widthProperty().subtract(20));
                setGraphic(card); setText(null);
            }
        });

        // ao selecionar cliente → monta painel hierárquico
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

            // cabeçalho do cliente
            Label clienteTitulo = new Label("👤  " + clienteSel.getNome());
            clienteTitulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
            clienteTitulo.setStyle("-fx-text-fill:#9B111E;");

            String cidadeStr = clienteSel.getCidade() != null && !clienteSel.getCidade().isBlank()
                ? "   📍 " + clienteSel.getCidade() : "";
            Label clienteDoc = new Label("📄  " + clienteSel.getDocumento() + cidadeStr);
            clienteDoc.setStyle("-fx-text-fill:#6b7280; -fx-font-size:12px;");

            detalhePane.getChildren().addAll(new Separator(), clienteTitulo, clienteDoc);

            // buscar equipamentos do cliente
            List<Equipamento> equipamentos = new java.util.ArrayList<>();
            if (EQUIPAMENTOS_POR_CLIENTE_PROVIDER != null) {
                try { equipamentos = EQUIPAMENTOS_POR_CLIENTE_PROVIDER.listarPorCliente(clienteSel); }
                catch (Exception ex) { alert("Erro", "Erro ao carregar equipamentos: " + ex.getMessage()); }
            }

            if (equipamentos.isEmpty()) {
                Label semEq = new Label("   Nenhum equipamento cadastrado para este cliente.");
                semEq.setStyle("-fx-text-fill:#9ca3af; -fx-font-size:12px; -fx-padding:6 0 0 8;");
                detalhePane.getChildren().add(semEq);
                return;
            }

            // para cada equipamento → acordeão clicável
            for (Equipamento eq : equipamentos) {

                Label eqLabel = new Label("🖥️  " + eq.getNome() + "   |   🔧 " + eq.getModelo());
                eqLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
                eqLabel.setStyle("-fx-text-fill:#1e293b;");

                VBox orcamentosContainer = new VBox(6);
                orcamentosContainer.setPadding(new Insets(4, 0, 4, 20));
                orcamentosContainer.setVisible(false);
                orcamentosContainer.setManaged(false);

                Label seta = new Label("▶");
                seta.setStyle("-fx-text-fill:#9B111E; -fx-font-size:11px;");

                HBox eqHeader = new HBox(8, seta, eqLabel);
                eqHeader.setAlignment(Pos.CENTER_LEFT);
                eqHeader.setPadding(new Insets(8, 12, 8, 8));
                eqHeader.setStyle("-fx-background-color:#f1f5f9;-fx-border-color:#e2e8f0;-fx-border-radius:6;-fx-background-radius:6;-fx-cursor:hand;");
                eqHeader.setOnMouseEntered(e -> eqHeader.setStyle("-fx-background-color:#e2e8f0;-fx-border-color:#9B111E;-fx-border-radius:6;-fx-background-radius:6;-fx-cursor:hand;"));
                eqHeader.setOnMouseExited (e -> eqHeader.setStyle("-fx-background-color:#f1f5f9;-fx-border-color:#e2e8f0;-fx-border-radius:6;-fx-background-radius:6;-fx-cursor:hand;"));

                eqHeader.setOnMouseClicked(e -> {
                    boolean aberto = orcamentosContainer.isVisible();
                    if (!aberto) {
                        orcamentosContainer.getChildren().clear();

                        // buscar orçamentos do equipamento
                        List<Orçamento> orcamentos = new java.util.ArrayList<>();
                        if (ORCAMENTOS_POR_EQUIPAMENTO_PROVIDER != null) {
                            try { orcamentos = ORCAMENTOS_POR_EQUIPAMENTO_PROVIDER.listarPorEquipamento(eq); }
                            catch (Exception ex) { alert("Erro", "Erro ao carregar orçamentos: " + ex.getMessage()); }
                        }

                        if (orcamentos.isEmpty()) {
                            Label semOrc = new Label("      Nenhum orçamento vinculado a este equipamento.");
                            semOrc.setStyle("-fx-text-fill:#9ca3af; -fx-font-size:12px;");
                            orcamentosContainer.getChildren().add(semOrc);
                        } else {
                            for (Orçamento orc : orcamentos) {

                                String statusEmoji = switch (orc.getStatus()) {
                                    case APROVADO  -> "✅";
                                    case REPROVADO -> "❌";
                                    default        -> "⏳";
                                };
                                String tecNome = orc.getTecnicoResponsavel() != null
                                    ? orc.getTecnicoResponsavel().getNome() : "-";

                                Label orcLabel = new Label(
                                    statusEmoji + "  Orçamento #" + orc.getId()
                                    + "   " + orc.getStatus()
                                    + "   👷 " + tecNome);
                                orcLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));
                                orcLabel.setStyle("-fx-text-fill:#374151;");

                                Label setaOrc = new Label("▶");
                                setaOrc.setStyle("-fx-text-fill:#6b7280; -fx-font-size:10px;");

                                VBox osContainer = new VBox(4);
                                osContainer.setPadding(new Insets(2, 0, 2, 24));
                                osContainer.setVisible(false);
                                osContainer.setManaged(false);

                                HBox orcHeader = new HBox(8, setaOrc, orcLabel);
                                orcHeader.setAlignment(Pos.CENTER_LEFT);
                                orcHeader.setPadding(new Insets(6, 12, 6, 12));
                                orcHeader.setStyle("-fx-background-color:#f8fafc;-fx-border-color:#e5e7eb;-fx-border-radius:5;-fx-background-radius:5;-fx-cursor:hand;");
                                orcHeader.setOnMouseEntered(ev -> orcHeader.setStyle("-fx-background-color:#eff6ff;-fx-border-color:#3b82f6;-fx-border-radius:5;-fx-background-radius:5;-fx-cursor:hand;"));
                                orcHeader.setOnMouseExited (ev -> orcHeader.setStyle("-fx-background-color:#f8fafc;-fx-border-color:#e5e7eb;-fx-border-radius:5;-fx-background-radius:5;-fx-cursor:hand;"));

                                // ao clicar no orçamento → mostra OS vinculadas
                                orcHeader.setOnMouseClicked(ev -> {
                                    boolean orcAberto = osContainer.isVisible();
                                    if (!orcAberto) {
                                        osContainer.getChildren().clear();

                                        // buscar todas as OS do equipamento e filtrar pelo orçamento
                                        List<OrdemDeServico> osList = new java.util.ArrayList<>();
                                        if (OS_PROVIDER != null) {
                                            try { osList = OS_PROVIDER.listarOsPorEquipamento(eq); }
                                            catch (Exception ex) { alert("Erro", "Erro ao carregar OS: " + ex.getMessage()); }
                                        }

                                        final int orcId = orc.getId();
                                        List<OrdemDeServico> osFiltrada = osList.stream()
                                            .filter(os -> os.getOrcamentoAprovado() != null
                                                       && os.getOrcamentoAprovado().getId() == orcId)
                                            .toList();

                                        if (osFiltrada.isEmpty()) {
                                            Label semOs = new Label("         Nenhuma OS vinculada a este orçamento.");
                                            semOs.setStyle("-fx-text-fill:#9ca3af; -fx-font-size:11px;");
                                            osContainer.getChildren().add(semOs);
                                        } else {
                                            for (OrdemDeServico os : osFiltrada) {
                                                String osEmoji = switch (os.getStatus()) {
                                                    case CONCLUIDA    -> "🟢";
                                                    case EM_ANDAMENTO -> "🔵";
                                                    default           -> "⚪";
                                                };
                                                String dataAb = os.getDataAbertura() != null
                                                    ? "   📅 " + os.getDataAbertura()
                                                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                                    : "";
                                                Label osLabel = new Label(
                                                    osEmoji + "  OS #" + os.getId()
                                                    + "   " + os.getStatus() + dataAb);
                                                osLabel.setStyle(
                                                    "-fx-text-fill:#374151;" +
                                                    "-fx-font-size:11px;" +
                                                    "-fx-background-color:#f0fdf4;" +
                                                    "-fx-border-color:#bbf7d0;" +
                                                    "-fx-border-radius:4;" +
                                                    "-fx-background-radius:4;" +
                                                    "-fx-padding:4 10;");
                                                osContainer.getChildren().add(osLabel);
                                            }
                                        }
                                        setaOrc.setText("▼");
                                    } else {
                                        setaOrc.setText("▶");
                                    }
                                    osContainer.setVisible(!orcAberto);
                                    osContainer.setManaged(!orcAberto);
                                });

                                orcamentosContainer.getChildren().addAll(orcHeader, osContainer);
                            }
                        }
                        seta.setText("▼");
                    } else {
                        seta.setText("▶");
                    }
                    orcamentosContainer.setVisible(!aberto);
                    orcamentosContainer.setManaged(!aberto);
                });

                VBox eqBloco = new VBox(4, eqHeader, orcamentosContainer);
                detalhePane.getChildren().add(eqBloco);
            }
        });

        // carregar lista inicial
        try {
            lista.setItems(CLIENTES_PROVIDER != null
                ? FXCollections.observableArrayList(CLIENTES_PROVIDER.listarClientes())
                : FXCollections.observableArrayList());
        } catch (Exception ex) { alert("Erro", ex.getMessage()); lista.setItems(FXCollections.observableArrayList()); }

        salvar.setOnAction(e -> {
            try {
                if (SALVAR_CLIENTE_HANDLER == null) return;
                String docLimpo = documento.getText() == null ? "" : documento.getText().replaceAll("\\D", "");
                SALVAR_CLIENTE_HANDLER.salvar(nome.getText(), telefone.getText(), email.getText(),
                    cidade.getText(), cep.getText(), docLimpo);
                if (CLIENTES_PROVIDER != null)
                    lista.setItems(FXCollections.observableArrayList(CLIENTES_PROVIDER.listarClientes()));
                nome.clear(); telefone.clear(); email.clear(); cidade.clear(); cep.clear(); documento.clear();
            } catch (Exception ex) { alert("Erro", ex.getMessage()); }
        });

        deletar.setOnAction(e -> {
            try {
                if (DELETAR_CLIENTE_HANDLER == null) return;
                Cliente sel = lista.getSelectionModel().getSelectedItem();
                if (sel == null) throw new IllegalArgumentException("Selecione um cliente");
                DELETAR_CLIENTE_HANDLER.deletar(sel.getDocumento());
                detalhePane.setVisible(false);
                detalhePane.setManaged(false);
                detalhePane.getChildren().clear();
                if (CLIENTES_PROVIDER != null)
                    lista.setItems(FXCollections.observableArrayList(CLIENTES_PROVIDER.listarClientes()));
            } catch (Exception ex) { alert("Erro", ex.getMessage()); }
        });

        ScrollPane detalheScroll = new ScrollPane(detalhePane);
        detalheScroll.setFitToWidth(true);
        detalheScroll.setStyle("-fx-background:transparent; -fx-background-color:transparent;");
        detalheScroll.setPrefHeight(500);

        HBox conteudo = new HBox(16, lista, detalheScroll);
        HBox.setHgrow(lista, Priority.SOMETIMES);
        HBox.setHgrow(detalheScroll, Priority.ALWAYS);
        lista.setMaxWidth(340);

        v.getChildren().addAll(titulo, sub, nome, telefone, email, cidade, cep, documento, salvar, deletar, conteudo);
        return v;
    }

    // ══════════════════════════════════════════════════════════════════════
    // EQUIPAMENTOS
    // ══════════════════════════════════════════════════════════════════════

    private VBox equipamento() {
        VBox v = page();

        Label titulo = title("Equipamentos", 22);
        Label sub    = subtitle("Cadastro e listagem de equipamentos do sistema");

        TextField nome             = input("Equipamento");
        TextField modelo           = input("Modelo");
        TextField documentoCliente = input("Documento do Cliente");
        Button salvar  = primary("Salvar Equipamento");
        Button deletar = primary("Deletar Equipamento");

        ListView<Equipamento> lista = new ListView<>();
        lista.setPrefHeight(450);
        lista.setStyle("-fx-background-color:transparent; -fx-border-color:transparent;");

        lista.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Equipamento eq, boolean empty) {
                super.updateItem(eq, empty);
                setStyle("-fx-background-color:transparent; -fx-padding:4 0;");
                if (empty || eq == null) { setGraphic(null); setText(null); return; }
                VBox card = listCard("🖥️", eq.getNome(),
                    "🔧 Modelo: " + eq.getModelo(),
                    eq.getDocumentoCliente() != null ? "👤 Cliente: " + eq.getDocumentoCliente() : null);
                card.prefWidthProperty().bind(lv.widthProperty().subtract(20));
                setGraphic(card); setText(null);
            }
        });

        try {
            lista.setItems(EQUIPAMENTOS_PROVIDER != null
                ? FXCollections.observableArrayList(EQUIPAMENTOS_PROVIDER.listarEquipamentos())
                : FXCollections.observableArrayList());
        } catch (Exception ex) { alert("Erro", ex.getMessage()); lista.setItems(FXCollections.observableArrayList()); }

        salvar.setOnAction(e -> {
            try {
                if (SALVAR_EQUIPAMENTO_HANDLER == null) return;
                SALVAR_EQUIPAMENTO_HANDLER.salvar(nome.getText(), modelo.getText(), documentoCliente.getText());
                if (EQUIPAMENTOS_PROVIDER != null)
                    lista.setItems(FXCollections.observableArrayList(EQUIPAMENTOS_PROVIDER.listarEquipamentos()));
                nome.clear(); modelo.clear(); documentoCliente.clear();
            } catch (Exception ex) { alert("Erro", ex.getMessage()); }
        });

        deletar.setDisable(true);
        lista.getSelectionModel().selectedItemProperty().addListener((obs, o, n) ->
            deletar.setDisable(n == null || DELETAR_EQUIPAMENTO_HANDLER == null));

        deletar.setOnAction(e -> {
            try {
                if (DELETAR_EQUIPAMENTO_HANDLER == null) return;
                Equipamento sel = lista.getSelectionModel().getSelectedItem();
                if (sel == null) throw new IllegalArgumentException("Selecione um equipamento");
                DELETAR_EQUIPAMENTO_HANDLER.deletar(sel.getId());
                if (EQUIPAMENTOS_PROVIDER != null)
                    lista.setItems(FXCollections.observableArrayList(EQUIPAMENTOS_PROVIDER.listarEquipamentos()));
            } catch (Exception ex) { alert("Erro", ex.getMessage()); }
        });

        v.getChildren().addAll(titulo, sub, nome, modelo, documentoCliente, salvar, deletar, lista);
        return v;
    }

    // ══════════════════════════════════════════════════════════════════════
    // ORDENS DE SERVIÇO
    // ══════════════════════════════════════════════════════════════════════

    private VBox ordemDeServico() {
        VBox v = page();

        Label titulo = title("Ordens de Serviço", 22);
        Label sub    = subtitle("Criação e início das Ordens de Serviço. A conclusão ocorre no Dashboard, em 'Em andamento'.");

        ComboBox<Equipamento>    equipamentos  = new ComboBox<>();
        ComboBox<OrdemDeServico> ordensServico = new ComboBox<>();
        equipamentos.setPromptText("Selecione o equipamento");
        ordensServico.setPromptText("Selecione a OS do equipamento");

        ordensServico.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(OrdemDeServico os, boolean empty) {
                super.updateItem(os, empty);
                if (empty || os == null) { setText(null); return; }
                String prev = os.getDataFechamentoPrevisto() == null ? "-" : os.getDataFechamentoPrevisto().format(DT_FMT);
                setText("OS #" + os.getId() + " | " + os.getStatus() + (os.estaAtrasada() ? " | ATRASADA" : "") + " | Previsto: " + prev);
            }
        });
        ordensServico.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(OrdemDeServico os, boolean empty) {
                super.updateItem(os, empty);
                setText(empty || os == null ? null : "OS #" + os.getId() + " | " + os.getStatus());
            }
        });

        Label criarTitulo = title("Criar nova OS", 16);
        Label criarSub    = subtitle("Defina descrição e data/hora prevista de fechamento");

        TextArea descricao = area("Descrição do problema");
        descricao.setPrefHeight(120);

        DatePicker dataPrevista = new DatePicker();
        dataPrevista.setPromptText("Data prevista");
        dataPrevista.setPrefHeight(44);
        dataPrevista.setStyle(inputStyle());
        hoverFocus(dataPrevista);

        Button criarOs     = primary("Criar OS");
        Button exportarPdf = primary("Exportar para PDF");

        ComboBox<Orçamento> orcamentos = new ComboBox<>();
        orcamentos.setPromptText("Selecione o orçamento aprovado");
        orcamentos.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Orçamento o, boolean empty) {
                super.updateItem(o, empty);
                if (empty || o == null) { setText(null); return; }
                setText("ID " + o.getId() + " | " + o.getStatus() + " | R$ " + o.getValorTotalPecas());
            }
        });
        orcamentos.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(Orçamento o, boolean empty) {
                super.updateItem(o, empty);
                setText(empty || o == null ? null : "R$ " + o.getValorTotalPecas());
            }
        });

        Button iniciar = primary("Iniciar OS");
        iniciar.setDisable(true); exportarPdf.setDisable(true);

        try {
            equipamentos.setItems(EQUIPAMENTOS_PROVIDER != null
                ? FXCollections.observableArrayList(EQUIPAMENTOS_PROVIDER.listarEquipamentos())
                : FXCollections.observableArrayList());
        } catch (Exception ex) { alert("Erro", ex.getMessage()); equipamentos.setItems(FXCollections.observableArrayList()); }

        equipamentos.setOnAction(e -> recarregarOsEOrcamentos(equipamentos, ordensServico, orcamentos));

        criarOs.setOnAction(e -> {
            try {
                if (CRIAR_OS_HANDLER == null) return;
                Equipamento eq = equipamentos.getValue();
                if (eq == null) throw new IllegalArgumentException("Selecione um equipamento");
                String desc = descricao.getText();
                if (desc == null || desc.isBlank()) throw new IllegalArgumentException("Informe a descrição do problema");
                LocalDate d = dataPrevista.getValue();
                if (d == null) throw new IllegalArgumentException("Selecione a data prevista");
                CRIAR_OS_HANDLER.criar(eq, desc, d);
                descricao.clear(); dataPrevista.setValue(null);
                recarregarOsEOrcamentos(equipamentos, ordensServico, orcamentos);
            } catch (Exception ex) { alert("Erro", ex.getMessage()); }
        });

        ordensServico.getSelectionModel().selectedItemProperty().addListener((o, a, n) -> {
            iniciar.setDisable(n == null || !n.estaPendente() || INICIAR_OS_HANDLER == null);
            exportarPdf.setDisable(n == null);
        });

        iniciar.setOnAction(e -> {
            try {
                if (INICIAR_OS_HANDLER == null) return;
                OrdemDeServico os = ordensServico.getValue();
                Equipamento    eq = equipamentos.getValue();
                Orçamento     orc = orcamentos.getValue();
                if (os == null) throw new IllegalArgumentException("Selecione uma OS");
                if (eq == null) throw new IllegalArgumentException("Selecione um equipamento");
                if (orc == null || !orc.estaAprovado()) throw new IllegalArgumentException("Selecione um orçamento aprovado");
                INICIAR_OS_HANDLER.iniciar(os.getId(), orc, eq);
                recarregarOsEOrcamentos(equipamentos, ordensServico, orcamentos);
            } catch (Exception ex) { alert("Erro", ex.getMessage()); }
        });

        exportarPdf.setOnAction(e -> {
            try {
                OrdemDeServico osSel  = ordensServico.getValue();
                Orçamento      orcSel = orcamentos.getValue();
                if (osSel == null) throw new IllegalArgumentException("Selecione uma OS para exportar");
                if (EXPORTAR_OS_PDF_HANDLER == null) { alert("Exportar PDF", "Handler não configurado."); return; }
                if (EXPORTAR_ORCAMENTO_PDF_HANDLER != null) alert("Exportar PDF", "PDF Baixado com sucesso, em C:\\Programing\\OS");
                EXPORTAR_OS_PDF_HANDLER.exportar(orcSel, osSel);
            } catch (Exception ex) { alert("Erro", ex.getMessage()); }
        });

        v.getChildren().addAll(titulo, sub, equipamentos, ordensServico,
            criarTitulo, criarSub, descricao, new HBox(10, dataPrevista),
            criarOs, exportarPdf, orcamentos, iniciar,
            subtitle("Para concluir uma OS, use o Dashboard e abra a lista de ordens 'Em andamento'."));
        return v;
    }

    private void recarregarOsEOrcamentos(
        ComboBox<Equipamento> equipamentos,
        ComboBox<OrdemDeServico> ordensServico,
        ComboBox<Orçamento> orcamentos
    ) {
        Equipamento eq = equipamentos.getValue();
        if (eq == null) {
            ordensServico.setItems(FXCollections.observableArrayList());
            orcamentos.setItems(FXCollections.observableArrayList());
            return;
        }
        try {
            ordensServico.setItems(OS_PROVIDER != null
                ? FXCollections.observableArrayList(OS_PROVIDER.listarOsPorEquipamento(eq))
                : FXCollections.observableArrayList());
            orcamentos.setItems(ORCAMENTOS_APROVADOS_PROVIDER != null
                ? FXCollections.observableArrayList(ORCAMENTOS_APROVADOS_PROVIDER.listarOrcamentosAprovados())
                : FXCollections.observableArrayList());
        } catch (Exception ex) {
            alert("Erro", ex.getMessage());
            ordensServico.setItems(FXCollections.observableArrayList());
            orcamentos.setItems(FXCollections.observableArrayList());
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // UTILITÁRIOS DE UI
    // ══════════════════════════════════════════════════════════════════════

    private void setCenter(Node n) { fade(n); root.setCenter(n); }

    private void fade(Node n) {
        FadeTransition ft = new FadeTransition(Duration.millis(220), n);
        ft.setFromValue(0); ft.setToValue(1); ft.play();
    }

    private VBox page() { VBox v = new VBox(20); v.setPadding(new Insets(40)); return v; }

    private TextField input(String p) {
        TextField t = new TextField();
        t.setPromptText(p); t.setPrefHeight(44); t.setStyle(inputStyle()); hoverFocus(t); return t;
    }

    private PasswordField password(String p) {
        PasswordField t = new PasswordField();
        t.setPromptText(p); t.setPrefHeight(44); t.setStyle(inputStyle()); hoverFocus(t); return t;
    }

    private TextArea area(String p) {
        TextArea t = new TextArea(); t.setPromptText(p); t.setStyle(inputStyle()); return t;
    }

    private Button primary(String txt) {
        Button b = new Button(txt); b.setPrefHeight(46); b.setStyle(btn()); hoverButton(b); return b;
    }

    private Button menuBtn(String txt, Runnable r) {
        Button b = new Button(txt);
        b.setOnAction(e -> r.run()); b.setMaxWidth(Double.MAX_VALUE); b.setStyle(menuBtn()); hoverMenu(b); return b;
    }

    private void hoverButton(Button b) {
        b.setOnMouseEntered(e -> b.setStyle(btnHover()));
        b.setOnMouseExited(e  -> b.setStyle(btn()));
    }

    private void hoverMenu(Button b) {
        b.setOnMouseEntered(e -> b.setStyle(menuBtnHover()));
        b.setOnMouseExited(e  -> b.setStyle(menuBtn()));
    }

    private void hoverFocus(Control c) {
        c.focusedProperty().addListener((o, a, f) -> c.setStyle(f ? inputFocus() : inputStyle()));
    }

    private Label title(String t, int s) {
        Label l = new Label(t); l.setFont(Font.font("Segoe UI", FontWeight.BOLD, s)); return l;
    }

    private Label subtitle(String t) {
        Label l = new Label(t); l.setStyle("-fx-text-fill:#6b7280;"); return l;
    }

    private void alert(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, m); a.setTitle(t); a.setHeaderText(null); a.showAndWait();
    }

    private String inputStyle()  { return "-fx-background-radius:10;-fx-border-radius:10;-fx-border-color:#9B111E;-fx-padding:12;"; }
    private String inputFocus()  { return "-fx-background-radius:10;-fx-border-radius:10;-fx-border-color:#2563eb;-fx-padding:12;"; }
    private String btn()         { return "-fx-background-color:#2563eb;-fx-text-fill:white;-fx-font-weight:bold;-fx-background-radius:10;"; }
    private String btnHover()    { return "-fx-background-color:#1d4ed8;-fx-text-fill:white;-fx-font-weight:bold;-fx-background-radius:10;"; }
    private String menuBtn()     { return "-fx-background-color:transparent;-fx-text-fill:#cbd5f5;-fx-font-size:14;"; }
    private String menuBtnHover(){ return "-fx-background-color:#9B111E;-fx-text-fill:white;-fx-background-radius:8;"; }
    private String card()        { return "-fx-background-color:white;-fx-background-radius:16;"; }
}