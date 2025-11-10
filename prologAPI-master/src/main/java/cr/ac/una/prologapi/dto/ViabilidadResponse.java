package cr.ac.una.prologapi.dto;

public class ViabilidadResponse {
    private String resultado; // "viable", "parcial", "imposible"
    private String mensaje;
    private Double tiempoTotal;
    private Double tiempoDisponible;
    private Integer tareasPosibles;
    private Integer tareasTotales;

    // Constructor vacío
    public ViabilidadResponse() {}

    // Constructor completo
    public ViabilidadResponse(String resultado, String mensaje, Double tiempoTotal,
                              Double tiempoDisponible, Integer tareasPosibles, Integer tareasTotales) {
        this.resultado = resultado;
        this.mensaje = mensaje;
        this.tiempoTotal = tiempoTotal;
        this.tiempoDisponible = tiempoDisponible;
        this.tareasPosibles = tareasPosibles;
        this.tareasTotales = tareasTotales;
    }

    // Getters y Setters
    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
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

    public Double getTiempoDisponible() {
        return tiempoDisponible;
    }

    public void setTiempoDisponible(Double tiempoDisponible) {
        this.tiempoDisponible = tiempoDisponible;
    }

    public Integer getTareasPosibles() {
        return tareasPosibles;
    }

    public void setTareasPosibles(Integer tareasPosibles) {
        this.tareasPosibles = tareasPosibles;
    }

    public Integer getTareasTotales() {
        return tareasTotales;
    }

    public void setTareasTotales(Integer tareasTotales) {
        this.tareasTotales = tareasTotales;
    }

    @Override
    public String toString() {
        return "ViabilidadResponse{" +
                "resultado='" + resultado + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", tiempoTotal=" + tiempoTotal +
                ", tiempoDisponible=" + tiempoDisponible +
                ", tareasPosibles=" + tareasPosibles +
                ", tareasTotales=" + tareasTotales +
                '}';
    }
}