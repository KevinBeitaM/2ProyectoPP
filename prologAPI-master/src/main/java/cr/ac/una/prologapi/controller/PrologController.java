package cr.ac.una.prologapi.controller;

import cr.ac.una.prologapi.dto.*;
import cr.ac.una.prologapi.service.PrologService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PrologController {

    @Autowired
    PrologService prologService;

    @GetMapping("/sum")
    public Integer sumGet(@RequestParam Integer a,
                          @RequestParam Integer b) {
        Integer result = prologService.suma(a.intValue(), b.intValue());
        return result;
    }


    // 1. GENERAR PLAN OPTIMIZADO

    @PostMapping("/planificar")
    public ResponseEntity<PlanificacionResponse> planificar(@RequestBody PlanificacionRequest request) {
        PlanificacionResponse response = prologService.generarPlan(request);
        return ResponseEntity.ok(response);
    }


    // 2. VERIFICAR VIABILIDAD



    @PostMapping("/verificar-viabilidad")
    public ResponseEntity<ViabilidadResponse> verificarViabilidad(@RequestBody ViabilidadRequest request) {
        ViabilidadResponse response = prologService.verificarViabilidad(request);
        return ResponseEntity.ok(response);
    }


    // 3. REPLANIFICAR



    @PostMapping("/replanificar")
    public ResponseEntity<PlanificacionResponse> replanificar(@RequestBody ReplanificacionRequest request) {
        PlanificacionResponse response = prologService.replanificar(request);
        return ResponseEntity.ok(response);
    }


    // 4. VALIDAR TAREAS



    @PostMapping("/validar-tareas")
    public ResponseEntity<Map<String, Object>> validarTareas(@RequestBody ValidarTareasRequest request) {
        try {
            boolean valido = prologService.validarTareas(request.getTareas());

            Map<String, Object> response = new HashMap<>();
            response.put("valido", valido);
            response.put("mensaje", valido ? "Todas las tareas son válidas" : "Hay tareas con datos inválidos");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("valido", false);
            error.put("mensaje", "Error al validar: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }


    // 5. ENDPOINT DE SALUD


    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "prologAPI");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
}