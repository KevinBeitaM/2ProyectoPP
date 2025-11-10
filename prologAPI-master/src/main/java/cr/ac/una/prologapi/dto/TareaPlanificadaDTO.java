package cr.ac.una.prologapi.dto;

import java.util.List;

public class TareaPlanificadaDTO {
    private String id;
    private String nombre;
    private String prioridad;
    private Double tiempoEstimado;
    private List<String> climas;
    private List<String> dependencias;
    private Double horaInicio;
    private Double horaFin;

    // Constructor vacío
    public TareaPlanificadaDTO() {}

    // Constructor completo
    public TareaPlanificadaDTO(String id, String nombre, String prioridad,
                               Double tiempoEstimado, List<String> climas,
                               List<String> dependencias, Double horaInicio, Double horaFin) {
        this.id = id;
        this.nombre = nombre;
        this.prioridad = prioridad;
        this.tiempoEstimado = tiempoEstimado;
        this.climas = climas;
        this.dependencias = dependencias;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public Double getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(Double tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    public List<String> getClimas() {
        return climas;
    }

    public void setClimas(List<String> climas) {
        this.climas = climas;
    }

    public List<String> getDependencias() {
        return dependencias;
    }

    public void setDependencias(List<String> dependencias) {
        this.dependencias = dependencias;
    }

    public Double getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Double horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Double getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Double horaFin) {
        this.horaFin = horaFin;
    }

    @Override
    public String toString() {
        return "TareaPlanificadaDTO{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", prioridad='" + prioridad + '\'' +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                '}';
    }
}