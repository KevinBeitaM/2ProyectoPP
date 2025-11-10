package cr.ac.una.agenda.controller;

import cr.ac.una.agenda.dto.ConfiguracionDTO;
import cr.ac.una.agenda.dto.PlanDTO;
import cr.ac.una.agenda.dto.ViabilidadDTO;
import cr.ac.una.agenda.service.AgendaService;
import cr.ac.una.agenda.service.ConfiguracionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agenda")
@CrossOrigin(origins = "*")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;

    @Autowired
    private ConfiguracionService configuracionService;

    /**
     * Generar plan optimizado
     * Si no se proporcionan parámetros, usa la configuración guardada
     */
    @GetMapping("/plan")
    public ResponseEntity<PlanDTO> generarPlan(
            @RequestParam(required = false) Double tiempoDisponible,
            @RequestParam(required = false) String clima,
            @RequestParam(required = false) Double horaInicio) {

        try {
            // Obtener configuración por defecto si no se proporcionan parámetros
            ConfiguracionDTO config = configuracionService.obtenerConfiguracion();

            Double tiempoFinal = (tiempoDisponible != null)
                    ? tiempoDisponible
                    : config.getTiempoDisponibleDefault();

            String climaFinal = (clima != null)
                    ? clima
                    : config.getClimaDefault();

            Double horaFinal = (horaInicio != null)
                    ? horaInicio
                    : config.getHoraInicioDefault();

            PlanDTO plan = agendaService.generarPlanOptimizado(
                    tiempoFinal,
                    climaFinal,
                    horaFinal
            );
            return ResponseEntity.ok(plan);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Verificar viabilidad
     */
    @GetMapping("/viabilidad")
    public ResponseEntity<ViabilidadDTO> verificarViabilidad(
            @RequestParam(required = false) Double tiempoDisponible,
            @RequestParam(required = false) String clima) {

        try {
            ConfiguracionDTO config = configuracionService.obtenerConfiguracion();

            Double tiempoFinal = (tiempoDisponible != null)
                    ? tiempoDisponible
                    : config.getTiempoDisponibleDefault();

            String climaFinal = (clima != null)
                    ? clima
                    : config.getClimaDefault();

            ViabilidadDTO viabilidad = agendaService.verificarViabilidad(
                    tiempoFinal,
                    climaFinal
            );
            return ResponseEntity.ok(viabilidad);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}