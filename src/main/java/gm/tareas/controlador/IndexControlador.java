package gm.tareas.controlador;

import gm.tareas.modelo.Tarea;
import gm.tareas.servicio.TareaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

@Component
public class IndexControlador implements Initializable { //implementamos la interfaz initianizable

    private static final Logger logger = LoggerFactory.getLogger(IndexControlador.class); // por si queremos mandar info a consola

    @Autowired
    private TareaService tareaServicio;

    @FXML
    private TableView<Tarea> tareaTabla;

    @FXML
    private TableColumn<Tarea,Integer> idTareaColumna;

    @FXML
    private TableColumn<Tarea,String> nombreTareaColumna;

    @FXML
    private TableColumn<Tarea,String> responsableColumna;

    @FXML
    private TableColumn<Tarea,String> estatusColumna;

    private final ObservableList<Tarea> tareaLista = FXCollections.observableArrayList(); //para inicializar la lista de tareas

    @FXML
    private TextField nombreTareaTexto;

    @FXML
    private TextField responsableTexto;

    @FXML
    private TextField estatusTexto;

    private Integer idTareaInterno; //para diferenciar el caso de agregar y de modificar

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tareaTabla.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); //para que se seleccione solo un elemento de la tabla
        configurarColumnas();
        //cargamos la info de la bd
        listarTareas();

    }

    private void configurarColumnas() {
        //mandamos a llamar al metodo y le pasamos el nombre del atributo
        idTareaColumna.setCellValueFactory(new PropertyValueFactory<>("idTarea"));
        nombreTareaColumna.setCellValueFactory(new PropertyValueFactory<>("nombreTarea"));
        responsableColumna.setCellValueFactory(new PropertyValueFactory<>("responsable"));
        estatusColumna.setCellValueFactory(new PropertyValueFactory<>("estatus"));
    }

    private void listarTareas() {
        tareaLista.clear();//la lista de tareas es de tipo observable
        tareaLista.addAll(tareaServicio.listarTareas());//nos regresa todos los objetos de tipo tarea
        tareaTabla.setItems(tareaLista);
    }

    public void agregarTarea() {
        if(nombreTareaTexto.getText().isEmpty()) {
            mostrarMensaje("Error de Validacion","Debe proporcionar una tarea");
            nombreTareaTexto.requestFocus();
            return;
        }else{
            var tarea = new Tarea();
            recolectarDatosFormulario(tarea);
            tarea.setIdTarea(null);//nos aseguramos de que sea una nueva tarea
            tareaServicio.guardarTarea(tarea);
            mostrarMensaje("Informacion","Tarea guardada con exito");
            limpiarFormulario();
            listarTareas();

        }
    }

    public void cargarTareaFormulario() {
        var tarea = tareaTabla.getSelectionModel().getSelectedItem();//nos trae la tarea seleccionada
        if(tarea != null) { //verificamos que realmente se haya seleccionado un registro
            //traemos los valores
            idTareaInterno = tarea.getIdTarea();
            nombreTareaTexto.setText(tarea.getNombreTarea());
            responsableTexto.setText(tarea.getResponsable());
            estatusTexto.setText(tarea.getEstatus());
        }
    }

    public void limpiarFormularioBoton(){
        limpiarFormulario();
    }

    public void modificarTarea() {
        if(idTareaInterno == null) {
            mostrarMensaje("Error de Validacion","Debe seleccionar una tarea");
            return;
        }
        if(nombreTareaTexto.getText().isEmpty()) {
            mostrarMensaje("Error de Validacion","Debe proporcionar una tarea");
            nombreTareaTexto.requestFocus();
            return; //retornamos para que el usuario pueda capturar esta informacion
        }else{
            var tarea = new Tarea();
            recolectarDatosFormulario(tarea);
            tareaServicio.guardarTarea(tarea);
            mostrarMensaje("Informacion","Tarea editada con exito");
            limpiarFormulario();
            listarTareas();
        }

    }
    private void limpiarFormulario() {
        //mandamos a llamar el metodo clear
        idTareaInterno = null;
        nombreTareaTexto.clear();
        responsableTexto.clear();
        estatusTexto.clear();
    }


    public void eliminarTarea() {
        if(nombreTareaTexto.getText().isEmpty()) {
            mostrarMensaje("Error de Validacion","Debe seleccionar una tarea");
            nombreTareaTexto.requestFocus();
            return;
        }else{
            tareaServicio.eliminarTarea(idTareaInterno);
            mostrarMensaje("Informacion","Tarea eliminada con exito");
            limpiarFormulario();
            listarTareas();
        }
    }

    private void recolectarDatosFormulario(Tarea tarea) {
        //recuperamos los valores escritos por el usuario
        if (idTareaInterno != null){
            tarea.setIdTarea(idTareaInterno);
        }
            tarea.setNombreTarea(nombreTareaTexto.getText());
            tarea.setResponsable(responsableTexto.getText());
            tarea.setEstatus(estatusTexto.getText());

    }

    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();//mostramos la alerta a traves de este metodo con javafx
    }

}
