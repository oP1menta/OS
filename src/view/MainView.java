package view;

import Exception.InvalidArgumentException;
import Classe.*;
import dominio.enums.StatusOrdemServico;

import javafx.animation.FadeTransition;
import javafx.application.Application;
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
import java.util.Collections;
import java.util.List;

public class MainView extends Application {

    private BorderPane root;

   
    private static EquipamentosProvider EQUIPAMENTOS_PROVIDER;
    private static OsProvider OS_PROVIDER;
    private static OrcamentosAprovadosProvider ORCAMENTOS_APROVADOS_PROVIDER;
    private static IniciarOsHandler INICIAR_OS_HANDLER;
    private static ConcluirOsHandler CONCLUIR_OS_HANDLER;

    private static LoginHandler LOGIN_HANDLER;
    private static DashboardDataProvider DASHBOARD_PROVIDER;

    private static TecnicosProvider TECNICOS_PROVIDER;
    private static OrcamentosPorTecnicoProvider ORCAMENTOS_PROVIDER;
    private static CriarOrcamentoHandler CRIAR_ORCAMENTO_HANDLER;
    private static AprovarOrcamentoHandler APROVAR_ORCAMENTO_HANDLER;
    private static ReprovarOrcamentoHandler REPROVAR_ORCAMENTO_HANDLER;

    private static ClientesProvider CLIENTES_PROVIDER;
    private static SalvarClienteHandler SALVAR_CLIENTE_HANDLER;

    private static CadastrarTecnicoHandler CADASTRAR_TECNICO_HANDLER;

   
    @FunctionalInterface
    public interface LoginHandler {
        boolean autenticar(String login, String senha) throws Exception;
    }

    @FunctionalInterface
    public interface DashboardDataProvider {
        List<OrdemDeServico> carregarOrdens() throws Exception;
    }

    @FunctionalInterface
    public interface EquipamentosProvider {
        List<Equipamento> listarEquipamentos() throws Exception;
    }

    @FunctionalInterface
    public interface OsProvider {
        List<OrdemDeServico> listarOsPorEquipamento(Equipamento equipamento) throws Exception;
    }

    @FunctionalInterface
    public interface OrcamentosAprovadosProvider {
        List<Orçamento> listarOrcamentosAprovados(Equipamento equipamento) throws Exception;
    }

    @FunctionalInterface
    public interface IniciarOsHandler {
        void iniciar(long osId, Orçamento orcamentoAprovado, Equipamento equipamento) throws Exception;
    }

    @FunctionalInterface
    public interface ConcluirOsHandler {
        void concluir(long osId, String observacoes, Equipamento equipamento, Orçamento orcamentoAprovado) throws Exception;
    }

    @FunctionalInterface
    public interface TecnicosProvider {
        List<Tecnico> listarTecnicos() throws Exception;
    }

    @FunctionalInterface
    public interface OrcamentosPorTecnicoProvider {
        List<Orçamento> listarPorTecnico(Tecnico tecnico) throws Exception;
    }

    @FunctionalInterface
    public interface CriarOrcamentoHandler {
        void criar(String descricao, BigDecimal valor, String pagamento, Tecnico tecnico) throws Exception;
    }

    @FunctionalInterface
    public interface AprovarOrcamentoHandler {
        void aprovar(long id, Tecnico tecnico) throws Exception;
    }

    @FunctionalInterface
    public interface ReprovarOrcamentoHandler {
        void reprovar(long id, Tecnico tecnico) throws Exception;
    }

    @FunctionalInterface
    public interface CadastrarTecnicoHandler {
        void cadastrar(String nome, String documento, java.time.LocalDate dataAssociacao) throws Exception;
    }

    @FunctionalInterface
    public interface ClientesProvider {
        List<Cliente> listarClientes() throws Exception;
    }

    @FunctionalInterface
    public interface SalvarClienteHandler {
        void salvar(String nome, String telefone, String email, String documento) throws Exception;
    }

    // ===== Setters de injeção =====
    public static void setLoginHandler(LoginHandler handler) { LOGIN_HANDLER = handler; }
    public static void setDashboardProvider(DashboardDataProvider provider) { DASHBOARD_PROVIDER = provider; }

    public static void setEquipamentosProvider(EquipamentosProvider p) { EQUIPAMENTOS_PROVIDER = p; }
    public static void setOsProvider(OsProvider p) { OS_PROVIDER = p; }
    public static void setOrcamentosAprovadosProvider(OrcamentosAprovadosProvider p) { ORCAMENTOS_APROVADOS_PROVIDER = p; }
    public static void setIniciarOsHandler(IniciarOsHandler h) { INICIAR_OS_HANDLER = h; }
    public static void setConcluirOsHandler(ConcluirOsHandler h) { CONCLUIR_OS_HANDLER = h; }

    public static void setTecnicosProvider(TecnicosProvider p) { TECNICOS_PROVIDER = p; }
    public static void setOrcamentosPorTecnicoProvider(OrcamentosPorTecnicoProvider p) { ORCAMENTOS_PROVIDER = p; }
    public static void setCriarOrcamentoHandler(CriarOrcamentoHandler h) { CRIAR_ORCAMENTO_HANDLER = h; }
    public static void setAprovarOrcamentoHandler(AprovarOrcamentoHandler h) { APROVAR_ORCAMENTO_HANDLER = h; }
    public static void setReprovarOrcamentoHandler(ReprovarOrcamentoHandler h) { REPROVAR_ORCAMENTO_HANDLER = h; }

    public static void setClientesProvider(ClientesProvider p) { CLIENTES_PROVIDER = p; }
    public static void setSalvarClienteHandler(SalvarClienteHandler h) { SALVAR_CLIENTE_HANDLER = h; }

    public static void setCadastrarTecnicoHandler(CadastrarTecnicoHandler h) { CADASTRAR_TECNICO_HANDLER = h; }

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
       // card.setMaxWidth(380);
        card.setStyle(card());

        Label logo = title("CONTRIMAQ", 30);
        Label sub = subtitle("GESTÃO DE SERVIÇOS");

        TextField user = input("Usuário");
        PasswordField pass = password("Senha");

        Button entrar = primary("Entrar");
        entrar.setOnAction(e -> {
            try {
                if (LOGIN_HANDLER == null) {
                    alert("Erro", "LoginHandler não configurado. INCIE PELO PROGRAMA.");
                    return;
                }

                boolean ok = LOGIN_HANDLER.autenticar(user.getText(), pass.getText());

                if (ok) {
                    Stage s = (Stage) entrar.getScene().getWindow();
                    s.setScene(app());
                } else {
                    alert("Erro", "Login inválido");
                }
            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
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
        m.setStyle("-fx-background-color:#0f172a;");

        Label logo = title("CONTRIMAQ", 22);
        logo.setStyle("-fx-text-fill:white;");

        m.getChildren().addAll(
            logo,
            menuBtn("📊 Dashboard", () -> setCenter(dashboard())),
            menuBtn("👤 Clientes", () -> setCenter(cliente())),
            menuBtn("🛠 Equipamentos", () -> setCenter(equipamento())),
            menuBtn("🧑‍🔧 Técnicos", () -> setCenter(tecnico())),
            menuBtn("📄 Ordens", () -> setCenter(ordemDeServico())),
            menuBtn("📄 Orçamento", () -> setCenter(orcamentos()))
        );

        return m;
    }

    private VBox dashboard() {

        VBox v = page();

        List<OrdemDeServico> lista = Collections.emptyList();

        try {
            if (DASHBOARD_PROVIDER != null) {
                lista = DASHBOARD_PROVIDER.carregarOrdens();
            }
        } catch (Exception ex) {
            alert("Erro", ex.getMessage());
        }

        v.getChildren().add(cards(lista));
        return v;
    }

    private VBox tecnico() {

        VBox v = page();

        Label titulo = title("Técnicos", 22);
        Label sub = subtitle("Cadastro e listagem de técnicos do sistema");

        TextField nome = input("Nome do técnico");
        TextField documento = input("Documento");

        Button salvar = primary("Salvar Técnico");

        ListView<Tecnico> lista = new ListView<>();
        lista.setPrefHeight(450);

        lista.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Tecnico t, boolean empty) {
                super.updateItem(t, empty);
                if (empty || t == null) {
                    setText(null);
                } else {
                    setText(
                        t.getNome() +
                        " • " + t.getDocumento() +
                        " • Desde: " + t.getDataAssociacao()
                    );
                }
            }
        });

        try {
            if (TECNICOS_PROVIDER != null) {
                lista.setItems(FXCollections.observableArrayList(TECNICOS_PROVIDER.listarTecnicos()));
            } else {
                lista.setItems(FXCollections.observableArrayList());
            }
        } catch (Exception ex) {
            alert("Erro", ex.getMessage());
            lista.setItems(FXCollections.observableArrayList());
        }

        salvar.setOnAction(e -> {
            try {
                if (CADASTRAR_TECNICO_HANDLER == null) return;

                CADASTRAR_TECNICO_HANDLER.cadastrar(
                    nome.getText(),
                    documento.getText(),
                    java.time.LocalDate.now()
                );

                if (TECNICOS_PROVIDER != null) {
                    lista.setItems(FXCollections.observableArrayList(TECNICOS_PROVIDER.listarTecnicos()));
                }

                nome.clear();
                documento.clear();

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        v.getChildren().addAll(titulo, sub, nome, documento, salvar, lista);
        return v;
    }

    private VBox orcamentos() {

        VBox v = page();

        Label titulo = title("Orçamentos", 22);
        Label sub = subtitle("Criação e aprovação de orçamentos por técnico");

        ComboBox<Tecnico> tecnicos = new ComboBox<>();
        tecnicos.setPromptText("Selecione o técnico");

        
        try {
            if (TECNICOS_PROVIDER != null) {
                tecnicos.setItems(FXCollections.observableArrayList(TECNICOS_PROVIDER.listarTecnicos()));
            } else {
                tecnicos.setItems(FXCollections.observableArrayList());
            }
        } catch (Exception ex) {
            alert("Erro", ex.getMessage());
            tecnicos.setItems(FXCollections.observableArrayList());
        }

        TextField peca = input("Peça / serviço");
        TextField valor = input("Valor");
        TextField pagamento = input("Tipo de pagamento");

        Button criar = primary("Criar orçamento");

        ListView<Orçamento> lista = new ListView<>();
        lista.setPrefHeight(350);

        tecnicos.setOnAction(e -> {
            Tecnico t = tecnicos.getValue();
            if (t == null) return;

            try {
                if (ORCAMENTOS_PROVIDER != null) {
                    lista.setItems(FXCollections.observableArrayList(ORCAMENTOS_PROVIDER.listarPorTecnico(t)));
                } else {
                    lista.setItems(FXCollections.observableArrayList());
                }
            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
                lista.setItems(FXCollections.observableArrayList());
            }
        });

        criar.setOnAction(e -> {
            try {
                if (CRIAR_ORCAMENTO_HANDLER == null) return;

                Tecnico t = tecnicos.getValue();
                if (t == null) throw new IllegalArgumentException("Selecione um técnico");

                CRIAR_ORCAMENTO_HANDLER.criar(
                    peca.getText(),
                    new BigDecimal(valor.getText()),
                    pagamento.getText(),
                    t
                );

                tecnicos.fireEvent(new ActionEvent());
                peca.clear();
                valor.clear();
                pagamento.clear();

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        lista.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Orçamento o, boolean empty) {
                super.updateItem(o, empty);
                setText(empty || o == null ? null : o.toString());
            }
        });

        Button aprovar = primary("Aprovar");
        Button reprovar = primary("Reprovar");

        aprovar.setDisable(true);
        reprovar.setDisable(true);

        lista.getSelectionModel().selectedItemProperty().addListener((o, a, n) -> {
            boolean pendente = n != null && n.estaPendente();
            aprovar.setDisable(!pendente || APROVAR_ORCAMENTO_HANDLER == null);
            reprovar.setDisable(!pendente || REPROVAR_ORCAMENTO_HANDLER == null);
        });

        aprovar.setOnAction(e -> {
            try {
                if (APROVAR_ORCAMENTO_HANDLER == null) return;

                Orçamento o = lista.getSelectionModel().getSelectedItem();
                Tecnico t = tecnicos.getValue();
                if (o == null) throw new IllegalArgumentException("Selecione um orçamento");
                if (t == null) throw new IllegalArgumentException("Selecione um técnico");

                APROVAR_ORCAMENTO_HANDLER.aprovar(o.getId(), t);
                tecnicos.fireEvent(new ActionEvent());

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        reprovar.setOnAction(e -> {
            try {
                if (REPROVAR_ORCAMENTO_HANDLER == null) return;

                Orçamento o = lista.getSelectionModel().getSelectedItem();
                Tecnico t = tecnicos.getValue();
                if (o == null) throw new IllegalArgumentException("Selecione um orçamento");
                if (t == null) throw new IllegalArgumentException("Selecione um técnico");

                REPROVAR_ORCAMENTO_HANDLER.reprovar(o.getId(), t);
                tecnicos.fireEvent(new ActionEvent());

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        v.getChildren().addAll(
            titulo, sub,
            tecnicos, peca, valor, pagamento,
            criar, lista, aprovar, reprovar
        );

        return v;
    }

    private VBox cliente() {

        VBox v = page();

        Label titulo = title("Clientes", 22);
        Label sub = subtitle("Cadastro e listagem de clientes do sistema");

        TextField nome = input("Nome");
        TextField telefone = input("Telefone");
        TextField email = input("Email");
        TextField documento = input("CPF / CNPJ");

        Button salvar = primary("Salvar Cliente");

        ListView<Cliente> lista = new ListView<>();
        lista.setPrefHeight(450);

        lista.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Cliente c, boolean empty) {
                super.updateItem(c, empty);
                if (empty || c == null) {
                    setText(null);
                } else {
                    setText(
                        c.getNome() +
                        " • " + c.getDocumento() +
                        (c.getTelefone() != null ? " • " + c.getTelefone() : "") +
                        (c.getEmail() != null ? " • " + c.getEmail() : "")
                    );
                }
            }
        });

        
        try {
            if (CLIENTES_PROVIDER != null) {
                lista.setItems(FXCollections.observableArrayList(CLIENTES_PROVIDER.listarClientes()));
            } else {
                lista.setItems(FXCollections.observableArrayList());
            }
        } catch (Exception ex) {
            alert("Erro", ex.getMessage());
            lista.setItems(FXCollections.observableArrayList());
        }

        salvar.setOnAction(e -> {
            try {
                if (SALVAR_CLIENTE_HANDLER == null) return;

                SALVAR_CLIENTE_HANDLER.salvar(
                    nome.getText(),
                    telefone.getText(),
                    email.getText(),
                    documento.getText()
                );

                if (CLIENTES_PROVIDER != null) {
                    lista.setItems(FXCollections.observableArrayList(CLIENTES_PROVIDER.listarClientes()));
                }

                nome.clear();
                telefone.clear();
                email.clear();
                documento.clear();

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        v.getChildren().addAll(
            titulo, sub,
            nome, telefone, email, documento,
            salvar, lista
        );

        return v;
    }

    private VBox equipamento() {
        
        VBox v = page();
        v.getChildren().addAll(
            input("Equipamento"),
            input("Modelo"),
            input("Documento do Cliente"),
            primary("Salvar Equipamento")
        );
        return v;
    }

    private VBox ordemDeServico() {

        VBox v = page();

        Label titulo = title("Ordens de Serviço", 22);
        Label sub = subtitle("Gerenciamento de Ordens de Serviço");

        ComboBox<Equipamento> equipamentos = new ComboBox<>();
        equipamentos.setPromptText("Selecione o equipamento");

        ListView<OrdemDeServico> listaOS = new ListView<>();
        listaOS.setPrefHeight(300);

        listaOS.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(OrdemDeServico os, boolean empty) {
                super.updateItem(os, empty);
                if (empty || os == null) setText(null);
                else {
                    setText(
                        "OS #" + os.getId() +
                        " | " + os.getStatus() +
                        " | Aberta em: " + os.getDataAbertura()
                    );
                }
            }
        });

        ComboBox<Orçamento> orcamentos = new ComboBox<>();
        orcamentos.setPromptText("Selecione o orçamento aprovado");

        orcamentos.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Orçamento o, boolean empty) {
                super.updateItem(o, empty);
                setText(empty || o == null ? null :
                    "R$ " + o.getValor() + " | " + o.getTecnicoResponsavel().getNome()
                );
            }
        });

        orcamentos.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Orçamento o, boolean empty) {
                super.updateItem(o, empty);
                setText(empty || o == null ? null : "R$ " + o.getValor());
            }
        });

        Button iniciar = primary("Iniciar OS");
        Button concluir = primary("Concluir OS");

        iniciar.setDisable(true);
        concluir.setDisable(true);

        
        try {
            if (EQUIPAMENTOS_PROVIDER != null) {
                equipamentos.setItems(FXCollections.observableArrayList(EQUIPAMENTOS_PROVIDER.listarEquipamentos()));
            } else {
                equipamentos.setItems(FXCollections.observableArrayList());
            }
        } catch (Exception ex) {
            alert("Erro", ex.getMessage());
            equipamentos.setItems(FXCollections.observableArrayList());
        }

        equipamentos.setOnAction(e -> {
            Equipamento eq = equipamentos.getValue();
            if (eq == null) return;

            try {
                if (OS_PROVIDER != null) {
                    listaOS.setItems(FXCollections.observableArrayList(OS_PROVIDER.listarOsPorEquipamento(eq)));
                } else {
                    listaOS.setItems(FXCollections.observableArrayList());
                }

                if (ORCAMENTOS_APROVADOS_PROVIDER != null) {
                    orcamentos.setItems(FXCollections.observableArrayList(ORCAMENTOS_APROVADOS_PROVIDER.listarOrcamentosAprovados(eq)));
                } else {
                    orcamentos.setItems(FXCollections.observableArrayList());
                }

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
                listaOS.setItems(FXCollections.observableArrayList());
                orcamentos.setItems(FXCollections.observableArrayList());
            }
        });

        listaOS.getSelectionModel().selectedItemProperty().addListener((o, a, n) -> {
            boolean podeIniciar = n != null && n.estaPendente() && INICIAR_OS_HANDLER != null;
            boolean podeConcluir = n != null && n.estaEmAndamento() && CONCLUIR_OS_HANDLER != null;

            iniciar.setDisable(!podeIniciar);
            concluir.setDisable(!podeConcluir);
        });

        iniciar.setOnAction(e -> {
            try {
                if (INICIAR_OS_HANDLER == null) return;

                OrdemDeServico os = listaOS.getSelectionModel().getSelectedItem();
                Equipamento eq = equipamentos.getValue();
                Orçamento orc = orcamentos.getValue();

                if (os == null) throw new IllegalArgumentException("Selecione uma OS");
                if (eq == null) throw new IllegalArgumentException("Selecione um equipamento");
                if (orc == null || !orc.estaAprovado())
                    throw new IllegalArgumentException("Selecione um orçamento aprovado");

                INICIAR_OS_HANDLER.iniciar(os.getId(), orc, eq);
                equipamentos.fireEvent(new ActionEvent());

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        concluir.setOnAction(e -> {
            try {
                if (CONCLUIR_OS_HANDLER == null) return;

                OrdemDeServico os = listaOS.getSelectionModel().getSelectedItem();
                Equipamento eq = equipamentos.getValue();

                if (os == null) throw new IllegalArgumentException("Selecione uma OS");
                if (eq == null) throw new IllegalArgumentException("Selecione um equipamento");

                String obs = "Observações técnicas";
                Orçamento orcAprovado = os.getOrcamentoAprovado();

                CONCLUIR_OS_HANDLER.concluir(os.getId(), obs, eq, orcAprovado);
                equipamentos.fireEvent(new ActionEvent());

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        v.getChildren().addAll(
            titulo, sub,
            equipamentos, listaOS, orcamentos,
            iniciar, concluir
        );

        return v;
    }

    // ===== Helpers UI =====
    private void setCenter(Node n) {
        fade(n);
        root.setCenter(n);
    }

    private void fade(Node n) {
        FadeTransition ft = new FadeTransition(Duration.millis(220), n);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    private VBox page() {
        VBox v = new VBox(20);
        v.setPadding(new Insets(40));
        return v;
    }

    private TextField input(String p) {
        TextField t = new TextField();
        t.setPromptText(p);
        t.setPrefHeight(44);
        t.setStyle(inputStyle());
        hoverFocus(t);
        return t;
    }

    private PasswordField password(String p) {
        PasswordField t = new PasswordField();
        t.setPromptText(p);
        t.setPrefHeight(44);
        t.setStyle(inputStyle());
        hoverFocus(t);
        return t;
    }

    private Button primary(String txt) {
        Button b = new Button(txt);
        b.setPrefHeight(46);
        b.setStyle(btn());
        hoverButton(b);
        return b;
    }

    private Button menuBtn(String txt, Runnable r) {
        Button b = new Button(txt);
        b.setOnAction(e -> r.run());
        b.setMaxWidth(Double.MAX_VALUE);
        b.setStyle(menuBtn());
        hoverMenu(b);
        return b;
    }

    private HBox cards(List<OrdemDeServico> l) {
        return new HBox(20,
            stat("Pendentes", StatusOrdemServico.PENDENTE, l),
            stat("Em andamento", StatusOrdemServico.EM_ANDAMENTO, l),
            stat("Concluídas", StatusOrdemServico.CONCLUIDA, l)
        );
    }

    private VBox stat(String t, StatusOrdemServico s, List<OrdemDeServico> l) {
        long v = l.stream().filter(o -> o.getStatus() == s).count();
        VBox b = new VBox(6, subtitle(t), title(String.valueOf(v), 28));
        b.setPadding(new Insets(25));
        b.setStyle(card());
        return b;
    }

    private void hoverButton(Button b) {
        b.setOnMouseEntered(e -> b.setStyle(btnHover()));
        b.setOnMouseExited(e -> b.setStyle(btn()));
    }

    private void hoverMenu(Button b) {
        b.setOnMouseEntered(e -> b.setStyle(menuBtnHover()));
        b.setOnMouseExited(e -> b.setStyle(menuBtn()));
    }

    private void hoverFocus(Control c) {
        c.focusedProperty().addListener((o, a, f) -> c.setStyle(f ? inputFocus() : inputStyle()));
    }

    private Label title(String t, int s) {
        Label l = new Label(t);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, s));
        return l;
    }

    private Label subtitle(String t) {
        Label l = new Label(t);
        l.setStyle("-fx-text-fill:#6b7280;");
        return l;
    }

    private void alert(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, m);
        a.setTitle(t);
        a.setHeaderText(null);
        a.showAndWait();
    }

    private String inputStyle() {
        return "-fx-background-radius:10;-fx-border-radius:10;-fx-border-color:#e5e7eb;-fx-padding:12;";
    }

    private String inputFocus() {
        return "-fx-background-radius:10;-fx-border-radius:10;-fx-border-color:#2563eb;-fx-padding:12;";
    }

    private String btn() {
        return "-fx-background-color:#2563eb;-fx-text-fill:white;-fx-font-weight:bold;-fx-background-radius:10;";
    }

    private String btnHover() {
        return "-fx-background-color:#1d4ed8;-fx-text-fill:white;-fx-font-weight:bold;-fx-background-radius:10;";
    }

    private String menuBtn() {
        return "-fx-background-color:transparent;-fx-text-fill:#cbd5f5;-fx-font-size:14;";
    }

    private String menuBtnHover() {
        return "-fx-background-color:#1e293b;-fx-text-fill:white;-fx-background-radius:8;";
    }

    private String card() {
        return "-fx-background-color:white;-fx-background-radius:16;";
    }

    
}
