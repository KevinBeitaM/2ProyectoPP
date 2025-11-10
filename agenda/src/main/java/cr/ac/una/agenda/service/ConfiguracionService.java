package cr.ac.una.agenda.service;

import cr.ac.una.agenda.dto.ConfiguracionDTO;
import cr.ac.una.agenda.entity.Configuracion;
import cr.ac.una.agenda.repository.ConfiguracionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConfiguracionService {

    @Autowired
    private ConfiguracionRepository configuracionRepository;

    private static final Long CONFIG_ID = 1L;

    /**
     * Obtiene la configuración actual (o crea una por defecto si no existe)
     */
    public ConfiguracionDTO obtenerConfiguracion() {
        Configuracion config = configuracionRepository.findById(CONFIG_ID)
                .orElseGet(() -> {
                    Configuracion nueva = new Configuracion();
                    nueva.setId(CONFIG_ID);
                    nueva.setTiempoDisponibleDefault(8.0);
                    nueva.setHoraInicioDefault(8.0);
                    nueva.setClimaDefault("cualquiera");
                    return configuracionRepository.save(nueva);
                });
        return new ConfiguracionDTO(config);
    }

    /**
     * Actualiza la configuración
     */
    public ConfiguracionDTO actualizarConfiguracion(ConfiguracionDTO dto) {
        Configuracion config = configuracionRepository.findById(CONFIG_ID)
                .orElse(new Configuracion());

        config.setId(CONFIG_ID);
        config.setTiempoDisponibleDefault(dto.getTiempoDisponibleDefault());
        config.setHoraInicioDefault(dto.getHoraInicioDefault());
        config.setClimaDefault(dto.getClimaDefault());

        config = configuracionRepository.save(config);
        return new ConfiguracionDTO(config);
    }
}