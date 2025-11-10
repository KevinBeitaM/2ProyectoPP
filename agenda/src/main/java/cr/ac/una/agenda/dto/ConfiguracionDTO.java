package cr.ac.una.agenda.dto;

import cr.ac.una.agenda.entity.Configuracion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionDTO {
    private Double tiempoDisponibleDefault;
    private Double horaInicioDefault;
    private String climaDefault;

    public ConfiguracionDTO(Configuracion config) {
        this.tiempoDisponibleDefault = config.getTiempoDisponibleDefault();
        this.horaInicioDefault = config.getHoraInicioDefault();
        this.climaDefault = config.getClimaDefault();
    }
}