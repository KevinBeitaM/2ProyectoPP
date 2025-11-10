package cr.ac.una.prologapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TareaDTO {
    private String id;
    private String nombre;
    private String prioridad; // "alta", "media", "baja"

    @JsonProperty("tiempo")
    private Double tiempoEstimado; // en horas

    private List<String> climas; // ["soleado", "nublado", "lluvioso", "cualquiera"]
    private List<String> dependencias;

    // Constructor vacío
    public TareaDTO() {}

    // Constructor completo
    public TareaDTO(String id, String nombre, String prioridad, Double tiempoEstimado,
                    List<String> climas, List<String> dependencias) {
        this.id = id;
        this.nombre = nombre;
        this.prioridad = prioridad;
        this.tiempoEstimado = tiempoEstimado;
        this.climas = climas;
        this.dependencias = dependencias;
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

    @JsonProperty("tiempo")
    public void setTiempo(Double tiempo) {
        this.tiempoEstimado = tiempo;
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

    @Override
    public String toString() {
        return "TareaDTO{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", prioridad='" + prioridad + '\'' +
                ", tiempoEstimado=" + tiempoEstimado +
                ", climas=" + climas +
                ", dependencias=" + dependencias +
                '}';
    }
}