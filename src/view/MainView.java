package view;
import Controller.*;
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
import java.sql.SQLException;
import java.util.List;

public class MainView extends Application {

    private BorderPane root;
    private final TecnicoController tecnicoController = new TecnicoController();
    private final UsuarioController usuarioController = new UsuarioController();
    private final ClienteController clienteController = new ClienteController();
    private final EquipamentoController equipamentoController = new EquipamentoController();
    private final OrdemDeServicoController osController = new OrdemDeServicoController();
    private final OrçamentoController orcamentoController = new OrçamentoController();
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
                menuBtn("📊 Dashboard", () -> {
                    setCenter(dashboard());
                }),
                menuBtn("👤 Clientes", () -> setCenter(cliente())),
                menuBtn("🛠 Equipamentos", () -> setCenter(equipamento())),
                menuBtn("🧑‍🔧 Técnicos", () -> setCenter(tecnico())),
                menuBtn("📄 Ordens", () -> {
					try {
						setCenter(ordemDeServico());
					} catch (SQLException e) {
				
						e.printStackTrace();
					}
				}),
                menuBtn("📄 Orçamento", () -> setCenter(orcamentos()))
        );

        return m;
    }


    private VBox dashboard() {

        VBox v = page();

        List<OrdemDeServico> lista = osController.listarTodas();

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

      
        lista.setItems(
                FXCollections.observableArrayList(
                        tecnicoController.listarTodos()
                )
        );

        salvar.setOnAction(e -> {
            try {
                tecnicoController.cadastrar(
                        nome.getText(),
                        documento.getText(),
                        java.time.LocalDate.now()
                );

                lista.setItems(
                        FXCollections.observableArrayList(
                                tecnicoController.listarTodos()
                        )
                );

                nome.clear();
                documento.clear();
              

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        v.getChildren().addAll(
                titulo,
                sub,
                nome,
                documento,
                salvar,
                lista
        );

        return v;
    }
    
    private VBox orcamentos() {

        VBox v = page();

        Label titulo = title("Orçamentos", 22);
        Label sub = subtitle("Criação e aprovação de orçamentos por técnico");

        //Seleção do tecnico
        ComboBox<Tecnico> tecnicos = new ComboBox<>();
        tecnicos.setPromptText("Selecione o técnico");

        tecnicos.setItems(
                FXCollections.observableArrayList(
                        tecnicoController.listarTodos()
                )
        );

        //Inputs do Orça
        TextField peca = input("Peça / serviço");
        TextField valor = input("Valor");
        TextField pagamento = input("Tipo de pagamento");

        Button criar = primary("Criar orçamento");

        
        ListView<Orçamento> lista = new ListView<>();
        lista.setPrefHeight(350);

        // Atualiza lista ao trocar técnico
        tecnicos.setOnAction(e -> {
            Tecnico t = tecnicos.getValue();
            if (t != null) {
                lista.setItems(
                        FXCollections.observableArrayList(
                                orcamentoController.listarPorTecnico(t)
                        )
                );
            }
        });

        criar.setOnAction(e -> {
            try {

                Tecnico t = tecnicos.getValue();
                if (t == null)
                    throw new IllegalArgumentException("Selecione um técnico");

                orcamentoController.criar(
                        peca.getText(),
                        new BigDecimal(valor.getText()),
                        pagamento.getText(),
                        t
                );

                lista.setItems(
                        FXCollections.observableArrayList(
                                orcamentoController.listarPorTecnico(t)
                        )
                );

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
                if (empty || o == null) {
                    setText(null);
                } else {
                    setText( o.toString()
                    );
                }
            }
        });

        
        Button aprovar = primary("Aprovar");
        Button reprovar = primary("Reprovar");

        aprovar.setDisable(true);
        reprovar.setDisable(true);

        lista.getSelectionModel().selectedItemProperty().addListener((o, a, n) -> {
            boolean ativo = n != null && n.estaPendente();
            aprovar.setDisable(!ativo);
            reprovar.setDisable(!ativo);
        });

        aprovar.setOnAction(e -> {
            try {
                Orçamento o = lista.getSelectionModel().getSelectedItem();
                Tecnico t = tecnicos.getValue();

                orcamentoController.aprovar(o.getId(), t);

                lista.setItems(
                        FXCollections.observableArrayList(
                                orcamentoController.listarPorTecnico(t)
                        )
                );

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        reprovar.setOnAction(e -> {
            try {
                Orçamento o = lista.getSelectionModel().getSelectedItem();
                Tecnico t = tecnicos.getValue();
                

                
                orcamentoController.reprovar(o.getId(),  t);

                lista.setItems(
                        FXCollections.observableArrayList(
                                orcamentoController.listarPorTecnico(t)
                        )
                );

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        v.getChildren().addAll(
                titulo,
                sub,
                tecnicos,
                peca,
                valor,
                pagamento,
                criar,
                lista,
                aprovar,
                reprovar
        );

        return v;
    }
    
    private VBox cliente() {
        VBox v = page();
        
        v.getChildren().addAll(
                input("Nome"),
                input("Telefone"),
                input("Email"),
                input("CPF / CNPJ"),
                primary("Salvar Cliente")
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

    private VBox ordemDeServico() throws SQLException {

        VBox v = page();

        Label titulo = title("Ordens de Serviço", 22);
        Label sub = subtitle("Gerenciamento de Ordens de Serviço");

        
        ComboBox<Equipamento> equipamentos = new ComboBox<>();
        equipamentos.setPromptText("Selecione o equipamento");

        equipamentos.setItems(
                FXCollections.observableArrayList(
                        equipamentoController.listarTodosEquipamentos()
                )
        );

        
        ListView<OrdemDeServico> listaOS = new ListView<>();
        listaOS.setPrefHeight(300);

        equipamentos.setOnAction(e -> {
            Equipamento eq = equipamentos.getValue();
            if (eq != null) {
                listaOS.setItems(
                        FXCollections.observableArrayList(
                                osController.listarPorEquipamento(eq, null)
                        )
                );
            }
        });

        listaOS.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(OrdemDeServico os, boolean empty) {
                super.updateItem(os, empty);
                if (empty || os == null) {
                    setText(null);
                } else {
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
                setText(empty || o == null ? null :
                        "R$ " + o.getValor()
                );
            }
        });

        
        Button iniciar = primary("Iniciar OS");
        Button concluir = primary("Concluir OS");

        iniciar.setDisable(true);
        concluir.setDisable(true);

        listaOS.getSelectionModel().selectedItemProperty().addListener((o, a, n) -> {
            iniciar.setDisable(n == null || !n.estaPendente());
            concluir.setDisable(n == null || !n.estaEmAndamento());
        });

        iniciar.setOnAction(e -> {
            try {
                OrdemDeServico os = listaOS.getSelectionModel().getSelectedItem();
                Orçamento orc = orcamentos.getValue();

                if (orc == null || !orc.estaAprovado())
                    throw new IllegalArgumentException("Selecione um orçamento aprovado");

                osController.iniciar(
                        os.getId(),
                        orc,
                        os.getEquipamento()
                );

                equipamentos.fireEvent(
                        new ActionEvent()
                );

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        concluir.setOnAction(e -> {
            try {
                OrdemDeServico os = listaOS.getSelectionModel().getSelectedItem();
                String obs = "Observações técnicas";

                osController.concluir(
                        os.getId(),
                        obs,
                        os.getEquipamento(),
                        os.getOrcamentoAprovado()
                );

                equipamentos.fireEvent(
                        new ActionEvent()
                );

            } catch (Exception ex) {
                alert("Erro", ex.getMessage());
            }
        });

        v.getChildren().addAll(
                titulo,
                sub,
                equipamentos,
                listaOS,
                orcamentos,
                iniciar,
                concluir
        );

        return v;
    }

    

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
