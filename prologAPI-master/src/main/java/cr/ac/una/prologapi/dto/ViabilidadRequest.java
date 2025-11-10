package cr.ac.una.prologapi.dto;

import java.util.List;

public class ViabilidadRequest {
    private List<TareaDTO> tareas;
    private Double tiempoDisponible;
    private String clima;

    // Constructor vacío
    public ViabilidadRequest() {}

    // Constructor completo
    public ViabilidadRequest(List<TareaDTO> tareas, Double tiempoDisponible, String clima) {
        this.tareas = tareas;
        this.tiempoDisponible = tiempoDisponible;
        this.clima = clima;
    }

    // Getters y Setters
    public List<TareaDTO> getTareas() {
        return tareas;
    }

    public void setTareas(List<TareaDTO> tareas) {
        this.tareas = tareas;
    }

    public Double getTiempoDisponible() {
        return tiempoDisponible;
    }

    public void setTiempoDisponible(Double tiempoDisponible) {
        this.tiempoDisponible = tiempoDisponible;
    }

    public String getClima() {
        return clima;
    }

    public void setClima(String clima) {
        this.clima = clima;
    }

    @Override
    public String toString() {
        return "ViabilidadRequest{" +
                "tareas=" + tareas +
                ", tiempoDisponible=" + tiempoDisponible +
                ", clima='" + clima + '\'' +
                '}';
    }
}