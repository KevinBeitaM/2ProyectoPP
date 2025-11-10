package cr.ac.una.agenda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TareaPlanificadaDTO {
    private String id;
    private String nombre;
    private String prioridad;
    private Double tiempoEstimado;
    private List<String> climas;
    private List<String> dependencias;
    private Double horaInicio;
    private Double horaFin;
}