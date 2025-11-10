package cr.ac.una.agenda.controller;

import cr.ac.una.agenda.dto.TareaDTO;
import cr.ac.una.agenda.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tareas")
@CrossOrigin(origins = "*")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @PostMapping
    public ResponseEntity<?> crearTarea(@RequestBody TareaDTO tareaDTO) {
        try {
            TareaDTO nuevaTarea = tareaService.crearTarea(tareaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaTarea);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping
    public ResponseEntity<List<TareaDTO>> listarTareas() {
        return ResponseEntity.ok(tareaService.listarTareas());
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<TareaDTO>> listarTareasPendientes() {
        return ResponseEntity.ok(tareaService.listarTareasPendientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerTarea(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(tareaService.obtenerTarea(id));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarTarea(@PathVariable Long id,
                                             @RequestBody TareaDTO tareaDTO) {
        try {
            return ResponseEntity.ok(tareaService.actualizarTarea(id, tareaDTO));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarTarea(@PathVariable Long id) {
        try {
            tareaService.eliminarTarea(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Tarea eliminada exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PatchMapping("/{id}/completar")
    public ResponseEntity<?> marcarComoCompletada(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(tareaService.marcarComoCompletada(id));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}