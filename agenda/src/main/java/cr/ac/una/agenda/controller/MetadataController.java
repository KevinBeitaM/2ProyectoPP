package cr.ac.una.agenda.controller;

import cr.ac.una.agenda.entity.Tarea;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/metadata")
@CrossOrigin(origins = "*")
public class MetadataController {

    /**
     * Obtener lista de prioridades disponibles
     */
    @GetMapping("/prioridades")
    public ResponseEntity<List<String>> obtenerPrioridades() {
        List<String> prioridades = Arrays.stream(Tarea.Prioridad.values())
                .map(p -> p.name().toLowerCase())
                .collect(Collectors.toList());
        return ResponseEntity.ok(prioridades);
    }

    /**
     * Obtener lista de climas disponibles
     */
    @GetMapping("/climas")
    public ResponseEntity<List<String>> obtenerClimas() {
        List<String> climas = Arrays.stream(Tarea.Clima.values())
                .map(c -> c.name().toLowerCase())
                .collect(Collectors.toList());
        return ResponseEntity.ok(climas);
    }
}