package es.aritzherrero.ejerciciof.controlador;

import es.aritzherrero.ejerciciof.modelo.Persona;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador para la ventana de "Nueva Persona" en la aplicación.
 * Permite gestionar la creación de nuevas instancias de la clase Persona
 * y cerrar la ventana modal correspondiente.
 */
public class EjercicioF_NuevaPersona_Control implements Initializable {

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnGuardar;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtEdad;

    @FXML
    private TextField txtNombre;

    String camposNulos;

    /**
     * Procedimiento que se ejecuta cuando se presiona el botón "Cancelar".
     * Cierra la ventana modal actual.
     *
     * @param event El evento de acción asociado al botón.
     */
    @FXML
    void cancelarVentana(ActionEvent event) {
        // Obtiene la ventana actual y la cierra.
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Procedimiento que se ejecuta cuando se presiona el botón "Guardar".
     * Intenta validar los datos ingresados y, si son correctos, guarda
     * una nueva persona. Si la persona ya existe o los datos son inválidos,
     * se muestra un mensaje de alerta.
     *
     * @param event El evento de acción asociado al botón.
     */
    @FXML
    void guardarPersona(ActionEvent event) {
        // Si no hay nombre en la persona actual, se agrega una nueva.
        if (EjercicioF_Principal_Control.p.getNombre().equals("")) {
            aniadir();
        } else {
            // Si ya existe, se modifica.
            modificar();
        }
        // Cierra la ventana después de guardar o modificar.
        cancelarVentana(event);
    }

    /**
     * Procedimiento para añadir una nueva persona después de validar los datos.
     */
    void aniadir() {
        String camposNulos = ""; // Reinicia la variable para campos nulos
        try {
            // Verifica que los campos no estén vacíos
            if (txtNombre.getText().equals("")) {
                camposNulos = "El campo nombre es obligatorio\n";
            }
            if (txtApellidos.getText().equals("")) {
                camposNulos += "El campo apellidos es obligatorio\n";
            }
            if (txtEdad.getText().isEmpty()) {
                camposNulos += "El campo edad es obligatorio";
            }
            // Lanza excepción si hay campos nulos
            if (!camposNulos.isEmpty()) {
                throw new NullPointerException();
            }
            // Verifica que la edad sea un número mayor que cero
            if (Integer.parseInt(txtEdad.getText().toString()) < 1) {
                throw new NumberFormatException();
            }

            // Crear persona
            String nombre = txtNombre.getText();
            String apellidos = txtApellidos.getText();
            Integer edad = Integer.parseInt(txtEdad.getText());
            Persona p = new Persona(nombre, apellidos, edad);
            // Verifica si la persona ya existe antes de agregarla
            if (!EjercicioF_Principal_Control.listaPersonas.contains(p)) {
                EjercicioF_Principal_Control.listaPersonas.add(p);
                EjercicioF_Principal_Control.ventanaAlerta("I", "Persona añadida correctamente");
            } else {
                EjercicioF_Principal_Control.ventanaAlerta("E", "La persona ya existe");
            }
        } catch (NullPointerException e) {
            // Muestra alerta si hay campos vacíos
            EjercicioF_Principal_Control.ventanaAlerta("E", camposNulos);
        } catch (NumberFormatException e) {
            // Muestra alerta si la edad no es un número válido
            EjercicioF_Principal_Control.ventanaAlerta("E", "El valor de edad debe ser un número mayor que cero");
        }
    }

    /**
     * Procedimiento para modificar una persona existente después de validar los datos.
     */
    void modificar() {
        camposNulos = ""; // Reinicia la variable para campos nulos
        try {
            // Crear persona auxiliar para comprobar si ya existe
            Persona pAux = new Persona(txtNombre.getText(), txtApellidos.getText(), Integer.parseInt(txtEdad.getText()));
            // Verifica si la persona ya existe antes de modificarla
            if (!EjercicioF_Principal_Control.listaPersonas.contains(pAux)) {
                // Modifica la persona actual
                EjercicioF_Principal_Control.listaPersonas.remove(EjercicioF_Principal_Control.p);
                EjercicioF_Principal_Control.listaPersonas.add(pAux);
                EjercicioF_Principal_Control.ventanaAlerta("I", "Persona modificada correctamente");
            } else {
                EjercicioF_Principal_Control.ventanaAlerta("E", "Persona existente");
            }

        } catch (NullPointerException e) {
            // Muestra alerta si hay campos vacíos
            EjercicioF_Principal_Control.ventanaAlerta("E", camposNulos);
        }
    }

    /**
     * Procedimiento que se ejecuta al inicializar la ventana.
     * Carga los datos de la persona si ya existen.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Si ya hay apellidos, se cargan los datos en los campos de texto
        if (!EjercicioF_Principal_Control.p.getApellidos().isEmpty()) {
            txtNombre.setText(EjercicioF_Principal_Control.p.getNombre());
            txtApellidos.setText(EjercicioF_Principal_Control.p.getApellidos());
            txtEdad.setText(EjercicioF_Principal_Control.p.getEdad() + "");
        }
    }
}
