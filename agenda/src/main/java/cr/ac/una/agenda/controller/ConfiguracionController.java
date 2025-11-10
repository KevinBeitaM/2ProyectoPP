package cr.ac.una.agenda.controller;

import cr.ac.una.agenda.dto.ConfiguracionDTO;
import cr.ac.una.agenda.service.ConfiguracionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configuracion")
@CrossOrigin(origins = "*")
public class ConfiguracionController {

    @Autowired
    private ConfiguracionService configuracionService;

    /**
     * Obtener configuración actual
     */
    @GetMapping
    public ResponseEntity<ConfiguracionDTO> obtenerConfiguracion() {
        return ResponseEntity.ok(configuracionService.obtenerConfiguracion());
    }

    /**
     * Actualizar configuración
     */
    @PutMapping
    public ResponseEntity<ConfiguracionDTO> actualizarConfiguracion(
            @RequestBody ConfiguracionDTO dto) {
        try {
            ConfiguracionDTO actualizada = configuracionService.actualizarConfiguracion(dto);
            return ResponseEntity.ok(actualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}