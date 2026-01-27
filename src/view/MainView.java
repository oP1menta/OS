package view;

import Controller.*;
import Exception.InvalidArgumentException;
import Classe.*;
import dominio.enums.StatusOrdemServico;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
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

import java.util.List;

public class MainView extends Application {

    private BorderPane root;

    private final UsuarioController usuarioController = new UsuarioController();
    private final ClienteController clienteController = new ClienteController();
    private final EquipamentoController equipamentoController = new EquipamentoController();
    private final OrdemDeServicoController osController = new OrdemDeServicoController();

    @Override
    public void start(Stage stage) {
        stage.setTitle("CONTRIMAQ • Service Desk");
        stage.setScene(login());
        stage.show();
    }

     //LOGIN 

    private Scene login() {

        VBox card = new VBox(18);
        card.setPadding(new Insets(40));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(380);
        card.setStyle(card());

        Label logo = title("CONTRIMAQ", 30);
        Label sub = subtitle("Service Management");

        TextField user = input("Usuário");
        PasswordField pass = password("Senha");

        Button entrar = primary("Entrar");
        entrar.setOnAction(e -> {
            try {
                if (usuarioController.login(user.getText(), pass.getText())) {
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

    // APP 

    private Scene app() throws InvalidArgumentException {

        root = new BorderPane();
        root.setStyle("-fx-background-color:#f2f4f8;");

        root.setLeft(menu());
        setCenter(dashboard());

        return new Scene(root, 1400, 850);
    }

   //MENU

    private VBox menu() {

        VBox m = new VBox(20);
        m.setPadding(new Insets(30));
        m.setPrefWidth(260);
        m.setStyle("-fx-background-color:#0f172a;");

        Label logo = title("CONTRIMAQ", 22);
        logo.setStyle("-fx-text-fill:white;");

        m.getChildren().addAll(
                logo,
                menuBtn("📊 Dashboard", () -> {
					try {
						setCenter(dashboard());
					} catch (InvalidArgumentException e) {
						e.printStackTrace();
					}
				}),
                menuBtn("👤 Clientes", () -> setCenter(cliente())),
                menuBtn("🛠 Equipamentos", () -> setCenter(equipamento())),
                menuBtn("📄 Ordens", () -> setCenter(ordens()))
        );

        return m;
    }

   //dASH

    private VBox dashboard() throws InvalidArgumentException {
        VBox v = page();
        List<OrdemDeServico> l = osController.listarTodas();
        v.getChildren().add(cards(l));
        return v;
    }

    private VBox cliente() {

        VBox v = page();

        TextField nome = input("Nome");
        TextField telefone = input("Telefone");
        TextField email = input("Email");
        TextField documento = input("CPF / CNPJ");

        ListView<String> lista = new ListView<>();
        lista.setPrefHeight(250);

        Button salvar = primary("Salvar Cliente");
        salvar.setOnAction(e -> {
            try {
                if (documento.getText().length() == 11) {
                    clienteController.cadastrarClienteFisico(
                            nome.getText(),
                            telefone.getText(),
                            email.getText(),
                            documento.getText()
                    );
                } else {
                    clienteController.cadastrarClienteJuridico(
                            nome.getText(),
                            telefone.getText(),
                            email.getText(),
                            documento.getText()
                    );
                }

                alert("Sucesso", "Cliente salvo com sucesso.");
                atualizarListaClientes(lista);

                nome.clear();
                telefone.clear();
                email.clear();
                documento.clear();

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        atualizarListaClientes(lista);

        v.getChildren().addAll(
                title("Clientes", 26),
                nome,
                telefone,
                email,
                documento,
                salvar,
                subtitle("Clientes cadastrados"),
                lista
        );

        return v;
    }

    private void atualizarListaClientes(ListView<String> lista) {
        lista.getItems().clear();
        List<Cliente> clientes = clienteController.listarClientesPorNome("");
        for (Cliente c : clientes) {
            lista.getItems().add(
                    c.getNome() + " • " + c.getDocumento()
            );
        }
    }


    private VBox equipamento() {

        VBox v = page();

        TextField nome = input("Equipamento");
        TextField modelo = input("Modelo");
        TextField documentoCliente = input("Documento do Cliente");

        ListView<String> lista = new ListView<>();
        lista.setPrefHeight(250);

        Button salvar = primary("Salvar Equipamento");
        salvar.setOnAction(e -> {
            try {
                equipamentoController.cadastrarEquipamento(
                        nome.getText(),
                        modelo.getText(),
                        documentoCliente.getText()
                );

                alert("Sucesso", "Equipamento salvo com sucesso.");
                atualizarListaEquipamentos(lista, documentoCliente.getText());

                nome.clear();
                modelo.clear();

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        v.getChildren().addAll(
                title("Equipamentos", 26),
                nome,
                modelo,
                documentoCliente,
                salvar,
                subtitle("Equipamentos do cliente"),
                lista
        );

        return v;
    }

    private void atualizarListaEquipamentos(ListView<String> lista, String documentoCliente) {
        lista.getItems().clear();
        try {
            List<Equipamento> equipamentos =
                    equipamentoController.listarEquipamentosPorCliente(documentoCliente);

            for (Equipamento e : equipamentos) {
                lista.getItems().add(
                        e.getNome() + " • " + e.getModelo()
                );
            }
        } catch (Exception e) {
            alert("Erro", e.getMessage());
        }
    }


    private VBox ordens() {
        VBox v = page();
        ComboBox<Equipamento> eq = new ComboBox<>();
        eq.setPrefHeight(44);
        eq.setStyle(inputStyle());
        v.getChildren().addAll(
                input("Documento do Cliente"),
                eq,
                area("Descrição do problema"),
                primary("Abrir Ordem")
        );
        return v;
    }

    /* =================  ================= */

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

    private TextArea area(String p) {
        TextArea t = new TextArea();
        t.setPromptText(p);
        t.setPrefHeight(120);
        t.setStyle(inputStyle());
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
        VBox b = new VBox(6,
                subtitle(t),
                title(String.valueOf(v), 28)
        );
        b.setPadding(new Insets(25));
        b.setStyle(card());
        return b;
    }

    /* ================= STYLE ================= */

    private void hoverButton(Button b) {
        b.setOnMouseEntered(e -> b.setStyle(btnHover()));
        b.setOnMouseExited(e -> b.setStyle(btn()));
    }

    private void hoverMenu(Button b) {
        b.setOnMouseEntered(e -> b.setStyle(menuBtnHover()));
        b.setOnMouseExited(e -> b.setStyle(menuBtn()));
    }

    private void hoverFocus(Control c) {
        c.focusedProperty().addListener((o, a, f) ->
                c.setStyle(f ? inputFocus() : inputStyle()));
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

    // CSS INLINE

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

    public static void main(String[] args) {
        launch();
    }
}
