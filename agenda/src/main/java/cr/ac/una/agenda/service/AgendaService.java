package cr.ac.una.agenda.service;

import cr.ac.una.agenda.dto.PlanDTO;
import cr.ac.una.agenda.dto.TareaDTO;
import cr.ac.una.agenda.dto.ViabilidadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AgendaService {

    @Autowired
    private PrologClientService prologClientService;

    @Autowired
    private TareaService tareaService;

    /**
     * Genera un plan optimizado llamando a apiProlog
     */
    public PlanDTO generarPlanOptimizado(double tiempoDisponible, String climaActual, double horaInicio) {

        List<TareaDTO> tareasPendientes = tareaService.listarTareasPendientes();

        if (tareasPendientes.isEmpty()) {
            return new PlanDTO("completo", new ArrayList<>(), "No hay tareas pendientes", 0.0, 0);
        }

        // 🔹 Mapear nombre -> tareaId para dependencias
        Map<String, String> nombreAId = tareasPendientes.stream()
                .collect(Collectors.toMap(
                        t -> t.getNombre().toLowerCase().trim(),
                        TareaDTO::getTareaId,
                        (a, b) -> a
                ));

        // 🔹 Convertir tareas a formato esperado por Prolog (con dependencias mapeadas a IDs)
        List<Map<String, Object>> tareasProlog = tareasPendientes.stream().map(t -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", t.getTareaId());
            m.put("nombre", t.getNombre());
            m.put("prioridad", t.getPrioridad());
            m.put("tiempo", t.getTiempoEstimado());

            List<String> climas = (t.getClimas() == null || t.getClimas().isEmpty())
                    ? List.of("cualquiera")
                    : t.getClimas();
            m.put("climas", climas);

            List<String> depsIds = (t.getDependencias() == null) ? List.of() :
                    t.getDependencias().stream()
                            .map(d -> nombreAId.getOrDefault(d.toLowerCase().trim(), d))
                            .collect(Collectors.toList());
            m.put("dependencias", depsIds);
            return m;
        }).collect(Collectors.toList());

        Map<String, Object> request = new HashMap<>();
        request.put("tareas", tareasProlog);
        request.put("tiempoDisponible", tiempoDisponible);
        request.put("clima", climaActual);
        request.put("horaInicio", horaInicio);

        // ✅ Llamada directa a PrologAPI
        PlanDTO plan = prologClientService.generarPlan(request);
        System.out.println("✅ [AgendaService] Plan recibido del PrologAPI: " + plan);
        return plan;
    }

    /**
     * Verifica la viabilidad de completar las tareas
     */
    public ViabilidadDTO verificarViabilidad(double tiempoDisponible, String climaActual) {
        List<TareaDTO> tareasPendientes = tareaService.listarTareasPendientes();

        if (tareasPendientes.isEmpty()) {
            return new ViabilidadDTO("viable", "No hay tareas pendientes", 0.0, tiempoDisponible, 0, 0);
        }

        Map<String, String> nombreAId = tareasPendientes.stream()
                .collect(Collectors.toMap(
                        t -> t.getNombre().toLowerCase().trim(),
                        TareaDTO::getTareaId,
                        (a, b) -> a
                ));

        List<Map<String, Object>> tareasProlog = tareasPendientes.stream().map(t -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", t.getTareaId());
            m.put("nombre", t.getNombre());
            m.put("prioridad", t.getPrioridad());
            m.put("tiempo", t.getTiempoEstimado());

            List<String> climas = (t.getClimas() == null || t.getClimas().isEmpty())
                    ? List.of("cualquiera")
                    : t.getClimas();
            m.put("climas", climas);

            List<String> depsIds = (t.getDependencias() == null) ? List.of() :
                    t.getDependencias().stream()
                            .map(d -> nombreAId.getOrDefault(d.toLowerCase().trim(), d))
                            .collect(Collectors.toList());
            m.put("dependencias", depsIds);
            return m;
        }).collect(Collectors.toList());

        Map<String, Object> request = new HashMap<>();
        request.put("tareas", tareasProlog);
        request.put("tiempoDisponible", tiempoDisponible);
        request.put("clima", climaActual);

        ViabilidadDTO viabilidad = prologClientService.verificarViabilidad(request);
        System.out.println("✅ [AgendaService] Viabilidad recibida del PrologAPI: " + viabilidad);
        return viabilidad;
    }
}