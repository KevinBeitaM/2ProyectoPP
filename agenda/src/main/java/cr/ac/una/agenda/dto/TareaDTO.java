package cr.ac.una.agenda.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import cr.ac.una.agenda.entity.Tarea;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TareaDTO {
    private Long id;
    private String tareaId;
    private String nombre;
    private String prioridad;

    @JsonProperty("tiempo")
    private Double tiempoEstimado;

    private List<String> climas;
    private List<String> dependencias;
    private Boolean completada;
    private LocalDateTime fechaLimite;

    public TareaDTO(Tarea tarea) {
        this.id = tarea.getId();
        this.tareaId = tarea.getTareaId();
        this.nombre = tarea.getNombre();
        this.prioridad = tarea.getPrioridad().name().toLowerCase();
        this.tiempoEstimado = tarea.getTiempoEstimado();
        this.climas = tarea.getClimas().stream()
                .map(c -> c.name().toLowerCase())
                .collect(Collectors.toList());
        this.dependencias = tarea.getDependencias();
        this.completada = tarea.getCompletada();
        this.fechaLimite = tarea.getFechaLimite();
    }

    /**
     * Convierte este DTO a un Map genérico que pueda enviarse al PrologAPI.
     */
    public Map<String, Object> toMapForProlog() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.tareaId);
        map.put("nombre", this.nombre);
        map.put("prioridad", this.prioridad);
        map.put("tiempo", this.tiempoEstimado);

        // ✅ Enviar lista de climas, no string suelto
        map.put("climas", this.climas != null && !this.climas.isEmpty()
                ? this.climas
                : List.of("cualquiera"));

        map.put("dependencias", this.dependencias != null ? this.dependencias : new ArrayList<>());

        return map;
    }
}