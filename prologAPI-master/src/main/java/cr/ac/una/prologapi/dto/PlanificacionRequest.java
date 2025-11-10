package cr.ac.una.prologapi.dto;

import java.util.List;

public class PlanificacionRequest {
    private List<TareaDTO> tareas;
    private Double tiempoDisponible; // en horas
    private String clima; // "soleado", "nublado", "lluvioso", "cualquiera"
    private Double horaInicio;

    // Constructor vacío
    public PlanificacionRequest() {}

    // Constructor completo
    public PlanificacionRequest(List<TareaDTO> tareas, Double tiempoDisponible,
                                String clima, Double horaInicio) {
        this.tareas = tareas;
        this.tiempoDisponible = tiempoDisponible;
        this.clima = clima;
        this.horaInicio = horaInicio;
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

    public Double getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Double horaInicio) {
        this.horaInicio = horaInicio;
    }

    @Override
    public String toString() {
        return "PlanificacionRequest{" +
                "tareas=" + tareas +
                ", tiempoDisponible=" + tiempoDisponible +
                ", clima='" + clima + '\'' +
                ", horaInicio=" + horaInicio +
                '}';
    }
}