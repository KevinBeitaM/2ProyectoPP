package cr.ac.una.agenda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanDTO {
    private String estado;
    private List<TareaPlanificadaDTO> tareasPlanificadas;
    private String mensaje;
    private Double tiempoTotal;
    private Integer cantidadTareas;
}