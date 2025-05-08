package gm.tareas.servicio;

import gm.tareas.modelo.Tarea;

import java.util.List;

public interface ITareaService {

    public List<Tarea> listarTareas();

    //buscar por id
    public Tarea buscarTareaPorId (int idTarea);

    //guardar tarea
    public void guardarTarea (Tarea tarea);

    //editar tarea
    public void editarTarea(Tarea tarea);

    //eliminar tarea

    public void eliminarTarea (int idTarea);
}
