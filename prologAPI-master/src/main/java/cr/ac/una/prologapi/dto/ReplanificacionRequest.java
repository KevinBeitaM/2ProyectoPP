package cr.ac.una.prologapi.dto;

import java.util.List;

public class ReplanificacionRequest {
    private List<TareaDTO> tareasOriginales;
    private List<String> tareasCompletadas;
    private Double tiempoDisponible;
    private String clima;
    private Double horaInicio;

    // Constructor vacío
    public ReplanificacionRequest() {}

    // Constructor completo
    public ReplanificacionRequest(List<TareaDTO> tareasOriginales, List<String> tareasCompletadas,
                                  Double tiempoDisponible, String clima, Double horaInicio) {
        this.tareasOriginales = tareasOriginales;
        this.tareasCompletadas = tareasCompletadas;
        this.tiempoDisponible = tiempoDisponible;
        this.clima = clima;
        this.horaInicio = horaInicio;
    }

    // Getters y Setters
    public List<TareaDTO> getTareasOriginales() {
        return tareasOriginales;
    }

    public void setTareasOriginales(List<TareaDTO> tareasOriginales) {
        this.tareasOriginales = tareasOriginales;
    }

    public List<String> getTareasCompletadas() {
        return tareasCompletadas;
    }

    public void setTareasCompletadas(List<String> tareasCompletadas) {
        this.tareasCompletadas = tareasCompletadas;
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
        return "ReplanificacionRequest{" +
                "tareasOriginales=" + tareasOriginales +
                ", tareasCompletadas=" + tareasCompletadas +
                ", tiempoDisponible=" + tiempoDisponible +
                ", clima='" + clima + '\'' +
                ", horaInicio=" + horaInicio +
                '}';
    }
}