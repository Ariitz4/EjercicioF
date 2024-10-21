package es.aritzherrero.ejerciciof.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import es.aritzherrero.ejerciciof.HelloApplication;
import es.aritzherrero.ejerciciof.modelo.Persona;
import es.aritzherrero.ejerciciof.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 * Controlador principal del ejercicio F, que gestiona la ventana principal y
 * la tabla de personas.
 */
public class EjercicioF_Principal_Control implements Initializable {

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnGuardar;

    @FXML
    private TableView<Persona> tblvTabla;

    @FXML
    private TableColumn<Persona, String> tblcApellidos;

    @FXML
    private TableColumn<Persona, Integer> tblcEdad;

    @FXML
    private TableColumn<Persona, String> tblcNombre;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtEdad;

    @FXML
    private TextField txtNombre;


    static ObservableList<Persona> listaPersonas;
    static Persona p = new Persona("", "", 0);

    /**
     * Procedimiento que se llama cuando se inicializa la vista.
     * Configura las columnas de la tabla y enlaza los datos.
     *
     * @param arg0 URL de inicialización.
     * @param arg1 Recurso de inicialización.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Inicializa la lista observable de personas.
        listaPersonas = FXCollections.observableArrayList();

        // Configura las columnas de la tabla para enlazar con los campos de Persona.
        tblcNombre.setCellValueFactory(new PropertyValueFactory<Persona, String>("nombre"));
        tblcApellidos.setCellValueFactory(new PropertyValueFactory<Persona, String>("apellidos"));
        tblcEdad.setCellValueFactory(new PropertyValueFactory<Persona, Integer>("edad"));

        // Asigna la lista observable a la tabla.
        tblvTabla.setItems(listaPersonas);
    }

    /**
     * Procedimiento que prepara el formulario para agregar una nueva persona.
     *
     * @param event
     */
    @FXML
    void agregarPersona(ActionEvent event) {
        // Inicializa los valores de la persona.
        p.setNombre("");
        p.setApellidos("");
        p.setEdad(0);
        // Crea una nueva ventana auxiliar para agregar la persona.
        crearVentanaAux();
    }

    /**
     * Procedimiento para eliminar la persona seleccionada de la tabla.
     *
     * @param event
     */
    @FXML
    void eliminarPersona(ActionEvent event) {
        try {
            // Obtiene los datos de la persona seleccionada.
            String sNombreEliminado = tblvTabla.getSelectionModel().getSelectedItem().getNombre();
            String sApellidosEliminado = tblvTabla.getSelectionModel().getSelectedItem().getApellidos();
            Integer nEdadEliminado = tblvTabla.getSelectionModel().getSelectedItem().getEdad();

            // Elimina la persona de la lista.
            listaPersonas.remove(new Persona(sNombreEliminado, sApellidosEliminado, nEdadEliminado));
            ventanaAlerta("I", "Persona eliminada correctamente");
        } catch (NullPointerException e) {
            // Si no hay selección, muestra una alerta.
            ventanaAlerta("E", "Seleccione un registro de la tabla. Si no lo hay, añada uno.");
        }
    }

    /**
     * Procedimiento para modificar los datos de la persona seleccionada.
     *
     * @param event
     */
    @FXML
    void modificarPersona(ActionEvent event) {
        try {
            // Obtiene los datos de la persona seleccionada y los carga en el objeto persona.
            p.setNombre(tblvTabla.getSelectionModel().getSelectedItem().getNombre());
            p.setApellidos(tblvTabla.getSelectionModel().getSelectedItem().getApellidos());
            p.setEdad(tblvTabla.getSelectionModel().getSelectedItem().getEdad());
            // Crea una ventana auxiliar para editar los datos de la persona.
            crearVentanaAux();
        } catch (NullPointerException e) {
            // Si no hay selección, muestra una alerta.
            ventanaAlerta("E", "Seleccione un registro de la tabla. Si no lo hay, añada uno.");
        }
    }

    /**
     * Procedimiento que crea una ventana auxiliar para añadir o modificar una persona.
     */
    void crearVentanaAux() {
        Stage escena = new Stage();
        escena.setTitle("NUEVA PERSONA");
        FlowPane flwPanel;

        try {
            // Carga el archivo FXML para la nueva persona.
            flwPanel = FXMLLoader.load(HelloApplication.class.getResource("ejerciciof_NuevaPersona.fxml"));
            Scene scene = new Scene(flwPanel, 600, 300);
            escena.setScene(scene);

            // Configura las dimensiones mínimas de la ventana.
            escena.setMinHeight(300);
            escena.setMinWidth(600);
            escena.show();
        } catch (IOException e) {
            // Si hay un error al cargar la vista, muestra el error.
            System.out.println("No ha sido posible abrir la ventana");
            e.printStackTrace();
        }
    }

    /**
     * Muestra una alerta según el tipo (error o confirmación) con un mensaje específico.
     *
     * @param tipoAlerta El tipo de alerta ("E" para error, "C" para confirmación).
     * @param mensaje El mensaje que se mostrará en la alerta.
     */
    static void ventanaAlerta(String tipoAlerta, String mensaje) {
        Alert alert = null;

        // Configura el tipo de alerta según el parámetro tipoAlerta.
        switch (tipoAlerta) {
            case ("E"):
                alert = new Alert(Alert.AlertType.ERROR);
                break;
            case ("C"):
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                break;
        }

        // Asigna el mensaje a la alerta y la muestra.
        if (alert != null) {
            alert.setContentText(mensaje);
            alert.showAndWait();
        }
    }
}
