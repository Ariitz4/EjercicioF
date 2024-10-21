module es.aritzherrero.ejerciciof {
    requires javafx.controls;
    requires javafx.fxml;


    opens es.aritzherrero.ejerciciof to javafx.fxml;
    exports es.aritzherrero.ejerciciof;
    opens es.aritzherrero.ejerciciof.controlador to javafx.fxml;
    opens es.aritzherrero.ejerciciof.modelo to javafx.fxml, javafx.base;
}