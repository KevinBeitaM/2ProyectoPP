package cr.ac.una.prologapi.dto;

import java.util.List;

public class ValidarTareasRequest {
    private List<TareaDTO> tareas;

    // Constructor vacío
    public ValidarTareasRequest() {}

    // Constructor completo
    public ValidarTareasRequest(List<TareaDTO> tareas) {
        this.tareas = tareas;
    }

    // Getters y Setters
    public List<TareaDTO> getTareas() {
        return tareas;
    }

    public void setTareas(List<TareaDTO> tareas) {
        this.tareas = tareas;
    }

    @Override
    public String toString() {
        return "ValidarTareasRequest{" +
                "tareas=" + tareas +
                '}';
    }
}