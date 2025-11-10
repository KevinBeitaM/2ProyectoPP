package cr.ac.una.prologapi.dto;

import java.util.List;

public class PlanificacionResponse {
    private String estado;
    private List<TareaPlanificadaDTO> tareasPlanificadas;
    private String mensaje;
    private Double tiempoTotal;
    private Integer cantidadTareas;

    // Constructor vacío
    public PlanificacionResponse() {}

    // Constructor completo
    public PlanificacionResponse(String estado, List<TareaPlanificadaDTO> tareasPlanificadas,
                                 String mensaje, Double tiempoTotal, Integer cantidadTareas) {
        this.estado = estado;
        this.tareasPlanificadas = tareasPlanificadas;
        this.mensaje = mensaje;
        this.tiempoTotal = tiempoTotal;
        this.cantidadTareas = cantidadTareas;
    }

    // Getters y Setters
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<TareaPlanificadaDTO> getTareasPlanificadas() {
        return tareasPlanificadas;
    }

    public void setTareasPlanificadas(List<TareaPlanificadaDTO> tareasPlanificadas) {
        this.tareasPlanificadas = tareasPlanificadas;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Double getTiempoTotal() {
        return tiempoTotal;
    }

    public void setTiempoTotal(Double tiempoTotal) {
        this.tiempoTotal = tiempoTotal;
    }

    public Integer getCantidadTareas() {
        return cantidadTareas;
    }

    public void setCantidadTareas(Integer cantidadTareas) {
        this.cantidadTareas = cantidadTareas;
    }

    @Override
    public String toString() {
        return "PlanificacionResponse{" +
                "estado='" + estado + '\'' +
                ", tareasPlanificadas=" + tareasPlanificadas +
                ", mensaje='" + mensaje + '\'' +
                ", tiempoTotal=" + tiempoTotal +
                ", cantidadTareas=" + cantidadTareas +
                '}';
    }
}