package cr.ac.una.agenda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViabilidadDTO {
    private String resultado;
    private String mensaje;
    private Double tiempoTotal;
    private Double tiempoDisponible;
    private Integer tareasPosibles;
    private Integer tareasTotales;
}