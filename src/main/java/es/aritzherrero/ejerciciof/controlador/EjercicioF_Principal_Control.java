package es.aritzherrero.ejerciciof.controlador;

import java.io.*;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Scanner;

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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;

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

    @FXML
    private TextField txtFiltrar;


    static ObservableList<Persona> listaPersonas;
    static ObservableList<Persona> listaFiltrada;
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
        // Inicializa las listas observables de personass.
        listaPersonas = FXCollections.observableArrayList();
        listaFiltrada = FXCollections.observableArrayList();
        // Configura las columnas de la tabla para enlazar con los campos de Persona.
        tblcNombre.setCellValueFactory(new PropertyValueFactory<Persona, String>("nombre"));
        tblcApellidos.setCellValueFactory(new PropertyValueFactory<Persona, String>("apellidos"));
        tblcEdad.setCellValueFactory(new PropertyValueFactory<Persona, Integer>("edad"));

        // Asigna la lista observable a la tabla.
        tblvTabla.setItems(listaFiltrada);
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
        actualizarTablaCompleta();
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
            listaFiltrada.remove(new Persona(sNombreEliminado, sApellidosEliminado, nEdadEliminado));
            ventanaAlerta("I", "Persona eliminada correctamente");
            actualizarTablaCompleta();
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
            actualizarTablaCompleta();
        } catch (NullPointerException e) {
            // Si no hay selección, muestra una alerta.
            ventanaAlerta("E", "Seleccione un registro de la tabla. Si no lo hay, añada uno.");
        }
    }


    /**
     * Procedimiento para filtrar por nombre la tabla.
     * Cada vez que se inserte/elimine un caracter de txtFiltrar se actualiza.
     * @param event
     */
    @FXML
    void filtrarTabla(KeyEvent event) {
        String sFiltro = txtFiltrar.getText().toLowerCase(); // Convertir a minúsculas

        listaFiltrada.clear(); // Limpiar la lista filtrada
        for (Persona p : listaPersonas) {
            if (p.getNombre().toLowerCase().contains(sFiltro)) { // Comparar en minúsculas
                listaFiltrada.add(p);
            }
        }

        tblvTabla.setItems(listaFiltrada); // Actualizar la tabla
    }

    /**
     * Procedimiento para exportar los datos de la tabla a un csv.
     * @param event
     */
    @FXML
    public void exportarDatos(ActionEvent event) {
        StringBuilder sb = new StringBuilder();

        // Agregar encabezados
        sb.append("Nombre,Apellido,Edad\n"); // Ajusta según los campos que necesites

        // Obtener los datos de la tabla
        ObservableList<Persona> personas = tblvTabla.getItems();
        for (Persona persona : personas) {
            sb.append(persona.getNombre()).append(",").append(persona.getApellidos()).append(",").append(persona.getEdad()).append("\n");
        }

        // Escribir en un archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Persona.csv"))) {
            writer.write(sb.toString());
            ventanaAlerta("C", "Datos exportados correctamente."); // Alerta de éxito
        } catch (IOException e) {
            e.printStackTrace(); // Manejo de excepciones
            ventanaAlerta("E", "Error al exportar los datos.");
        }
    }

    /**
     * Procedimiento para importar los datos de un csv a la tabla.
     * @param event
     */
    @FXML
    void importarDatos(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try (Scanner sc = new Scanner(file)) {
                if (sc.hasNextLine()) {
                    // Leer la primera línea para quitar el encabezado
                    sc.nextLine();
                }
                while (sc.hasNextLine()) {
                    String sLinea = sc.nextLine();
                    String[] arrLinea = sLinea.split(",");
                    if (arrLinea.length < 3) {
                        throw new IOException("La línea no tiene suficientes datos"); // Lanzar excepción si la línea no tiene suficientes datos
                    }
                    Persona p = new Persona(arrLinea[0], arrLinea[1], Integer.parseInt(arrLinea[2]));
                    if (!comprobarPersona(p)) {
                        throw new IOException("La persona tiene datos inválidos"); // Lanzar excepción si la persona tiene datos inválidos
                    }
                    if (!listaPersonas.contains(p)) {
                        listaPersonas.add(p);
                    }
                }
                actualizarTablaCompleta(); // Asegúrate de que la tabla se actualiza después de importar
                ventanaAlerta("I", "CSV importado correctamente");
            } catch (FileNotFoundException e) {
                ventanaAlerta("E", "No se encontró el archivo seleccionado");
            } catch (IOException e) {
                ventanaAlerta("E", e.getMessage()); // Usa el mensaje de la excepción
            } catch (NumberFormatException e) {
                ventanaAlerta("E", "La edad debe ser un número válido.");
            }
        } else {
            ventanaAlerta("E", "No se ha seleccionado ningún archivo.");
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
            flwPanel = FXMLLoader.load(HelloApplication.class.getResource("ejerciciof_nuevapersona.fxml"));
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

    /**
     * Procedimiento para comprobar las personas de la importación
     * @param p
     * @return
     */
    boolean comprobarPersona(Persona p) {
        boolean correcto = true;

        if (p.getNombre()=="")correcto=false;
        if (p.getApellidos()=="")correcto=false;
        if (p.getEdad()==0)correcto=false;

        return correcto;
    }

    /**
     * Actualiza la tabla para cuando no hay filtros.
     */
    void actualizarTablaCompleta() {
        tblvTabla.setItems(listaPersonas);
    }
}
