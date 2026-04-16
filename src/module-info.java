module OJAVA {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.base;
    requires com.github.librepdf.openpdf;
	requires java.desktop;

    // Esta linha permite que o JavaFX acesse seus modelos (Cliente, OS, etc.)
    opens Classe to javafx.base;
    
    // Abre os outros pacotes necessários
    opens view to javafx.graphics, javafx.fxml;
    opens Controller to javafx.base;
    
    exports view;
    exports Classe;
    exports Controller;
    exports BancoDeDados;
    
}
