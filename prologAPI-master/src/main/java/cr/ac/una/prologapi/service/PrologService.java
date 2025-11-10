package cr.ac.una.prologapi.service;

import cr.ac.una.prologapi.dto.*;
import cr.ac.una.prologapi.exception.PrologException;
import org.jpl7.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PrologService {

    private static final String PROLOG_FILE_PATH = "/opt/app/prolog/operaciones.pl";
    private static boolean prologConsultado = false;

    public PrologService() {
        consultarProlog();
    }

    public int suma(int a, int b){
        String filePath = "consult('/opt/app/prolog/operaciones.pl')";
        System.out.println("Entra");
        Query consultQuery = new Query(filePath);
        System.out.println(filePath + " " + (consultQuery.hasSolution() ? "éxito" : "fallo"));

        // Crear variables para los argumentos del predicado
        Term x = new org.jpl7.Integer(a);
        Term y = new org.jpl7.Integer(b);
        Variable z = new Variable("Z");  // Variable de salida

        // Construir la consulta: suma(5, 3, Z)
        Query query = new Query("suma", new Term[]{x, y, z});

        // Ejecutar la consulta y obtener el resultado
        if (query.hasSolution()) {
            Term result = query.oneSolution().get("Z");
            System.out.println("Resultado de la suma: " + result);
            return result.intValue();
        } else {
            System.out.println("No se encontró solución.");
            return 0;
        }

    }

    private void consultarProlog() {
        if (!prologConsultado) {
            try {
                String consultQuery = "consult('" + PROLOG_FILE_PATH + "')";
                Query query = new Query(consultQuery);

                if (query.hasSolution()) {
                    System.out.println("✅ Archivo Prolog cargado exitosamente: " + PROLOG_FILE_PATH);
                    prologConsultado = true;
                } else {
                    throw new PrologException("❌ No se pudo cargar el archivo Prolog: " + PROLOG_FILE_PATH);
                }
            } catch (Exception e) {
                throw new PrologException("❌ Error al consultar archivo Prolog", e);
            }
        }
    }

    // ==========================================================
    // 1. GENERAR PLAN OPTIMIZADO
    // ==========================================================
    public PlanificacionResponse generarPlan(PlanificacionRequest request) {
        try {
            System.out.println("📥 [PrologAPI] Request recibido:");
            System.out.println("  Clima: " + request.getClima());
            System.out.println("  Tiempo disponible: " + request.getTiempoDisponible());
            System.out.println("  Hora inicio: " + request.getHoraInicio());
            System.out.println("  Tareas recibidas: " + request.getTareas());

            validarPlanificacionRequest(request);

            Term tareasTerm = convertirTareasATerminos(request.getTareas());
            Term tiempoDisponibleTerm = new org.jpl7.Float(request.getTiempoDisponible());
            Term climaTerm = new Atom(request.getClima().toLowerCase());
            Term horaInicioTerm = new org.jpl7.Float(request.getHoraInicio());

            Variable planVar = new Variable("Plan");
            Variable estadoVar = new Variable("Estado");

            Query query = new Query("generar_plan", new Term[]{
                    tareasTerm,
                    tiempoDisponibleTerm,
                    climaTerm,
                    horaInicioTerm,
                    planVar,
                    estadoVar
            });

            if (query.hasSolution()) {
                Map<String, Term> solution = query.oneSolution();

                Term planTerm = solution.get("Plan");
                Term estadoTerm = solution.get("Estado");

                String estado = estadoTerm.name();
                List<TareaPlanificadaDTO> tareasPlanificadas = parsearPlanProlog(planTerm);

                Double tiempoTotal = calcularTiempoTotal(tareasPlanificadas);
                String mensaje = generarMensajePlan(estado, tareasPlanificadas.size(), request.getTareas().size());

                return new PlanificacionResponse(
                        estado,
                        tareasPlanificadas,
                        mensaje,
                        tiempoTotal,
                        tareasPlanificadas.size()
                );
            } else {
                return new PlanificacionResponse(
                        "imposible",
                        new ArrayList<>(),
                        "No se pudo generar un plan. Verifique dependencias circulares o datos inválidos.",
                        0.0,
                        0
                );
            }
        } catch (Exception e) {
            throw new PrologException("Error al generar plan: " + e.getMessage(), e);
        }
    }

    // ==========================================================
    // 2. VERIFICAR VIABILIDAD (mejorado)
    // ==========================================================
    public ViabilidadResponse verificarViabilidad(ViabilidadRequest request) {
        try {
            validarViabilidadRequest(request);

            Term tareasTerm = convertirTareasATerminos(request.getTareas());
            Term tiempoDisponibleTerm = new org.jpl7.Float(request.getTiempoDisponible());
            Term climaTerm = new Atom(request.getClima().toLowerCase());
            Variable resultadoVar = new Variable("Resultado");

            Query qViab = new Query("verificar_viabilidad", new Term[]{
                    tareasTerm, tiempoDisponibleTerm, climaTerm, resultadoVar
            });

            String resultado;
            if (!qViab.hasSolution()) {
                return new ViabilidadResponse(
                        "imposible",
                        "No se pudo determinar la viabilidad",
                        0.0,
                        request.getTiempoDisponible(),
                        0,
                        request.getTareas().size()
                );
            } else {
                resultado = qViab.oneSolution().get("Resultado").name();
            }

            // Ejecutar generar_plan para saber cuántas tareas caben con el clima actual
            Variable planVar = new Variable("Plan");
            Variable estadoVar = new Variable("Estado");
            Term horaInicioTerm = new org.jpl7.Float(8.0);

            Query qPlan = new Query("generar_plan", new Term[]{
                    tareasTerm, tiempoDisponibleTerm, climaTerm, horaInicioTerm, planVar, estadoVar
            });

            int tareasPosibles = 0;
            double tiempoTotalNecesario = 0.0;

            if (qPlan.hasSolution()) {
                Map<String, Term> sol = qPlan.oneSolution();
                Term planTerm = sol.get("Plan");
                List<TareaPlanificadaDTO> plan = parsearPlanProlog(planTerm);
                tareasPosibles = plan.size();
                tiempoTotalNecesario = plan.stream()
                        .mapToDouble(TareaPlanificadaDTO::getTiempoEstimado)
                        .sum();
            }

            // Si no hay plan, estimar manualmente tareas que caben en el tiempo disponible
            if (tareasPosibles == 0) {
                double acumulado = 0.0;
                for (TareaDTO t : request.getTareas()) {
                    // solo contar tareas compatibles con el clima
                    if (t.getClimas().contains("cualquiera") ||
                            t.getClimas().contains(request.getClima().toLowerCase())) {
                        if (acumulado + t.getTiempoEstimado() <= request.getTiempoDisponible()) {
                            acumulado += t.getTiempoEstimado();
                            tareasPosibles++;
                        }
                    }
                }
            }

            int total = request.getTareas().size();

            // Ajustar mensaje y resultado
            if (tareasPosibles == 0) resultado = "imposible";
            else if (tareasPosibles < total && !"imposible".equals(resultado)) resultado = "parcial";
            else if (tareasPosibles == total) resultado = "viable";

            String mensaje = generarMensajeViabilidad(resultado, tiempoTotalNecesario, request.getTiempoDisponible());

            return new ViabilidadResponse(
                    resultado,
                    mensaje,
                    tiempoTotalNecesario,
                    request.getTiempoDisponible(),
                    tareasPosibles,
                    total
            );

        } catch (Exception e) {
            throw new PrologException("Error al verificar viabilidad: " + e.getMessage(), e);
        }
    }

    // ==========================================================
    // 3. REPLANIFICAR TAREAS
    // ==========================================================
    public PlanificacionResponse replanificar(ReplanificacionRequest request) {
        try {
            validarReplanificacionRequest(request);

            Term tareasOriginalesTerm = convertirTareasATerminos(request.getTareasOriginales());
            Term tareasCompletadasTerm = convertirListaStringsATerminos(request.getTareasCompletadas());
            Term tiempoDisponibleTerm = new org.jpl7.Float(request.getTiempoDisponible());
            Term climaTerm = new Atom(request.getClima().toLowerCase());
            Term horaInicioTerm = new org.jpl7.Float(request.getHoraInicio());

            Variable nuevoPlanVar = new Variable("NuevoPlan");
            Variable estadoVar = new Variable("Estado");

            Query query = new Query("replanificar", new Term[]{
                    tareasOriginalesTerm,
                    tareasCompletadasTerm,
                    tiempoDisponibleTerm,
                    climaTerm,
                    horaInicioTerm,
                    nuevoPlanVar,
                    estadoVar
            });

            if (query.hasSolution()) {
                Map<String, Term> solution = query.oneSolution();

                Term planTerm = solution.get("NuevoPlan");
                Term estadoTerm = solution.get("Estado");

                String estado = estadoTerm.name();
                List<TareaPlanificadaDTO> tareasPlanificadas = parsearPlanProlog(planTerm);

                Double tiempoTotal = calcularTiempoTotal(tareasPlanificadas);
                String mensaje = "Plan reajustado exitosamente";

                return new PlanificacionResponse(
                        estado,
                        tareasPlanificadas,
                        mensaje,
                        tiempoTotal,
                        tareasPlanificadas.size()
                );
            } else {
                return new PlanificacionResponse(
                        "imposible",
                        new ArrayList<>(),
                        "No se pudo replanificar las tareas restantes",
                        0.0,
                        0
                );
            }
        } catch (Exception e) {
            throw new PrologException("Error al replanificar: " + e.getMessage(), e);
        }
    }

    // ==========================================================
    // 4. VALIDAR LISTA DE TAREAS
    // ==========================================================
    public boolean validarTareas(List<TareaDTO> tareas) {
        try {
            Term tareasTerm = convertirTareasATerminos(tareas);
            Query query = new Query("validar_lista_tareas", new Term[]{tareasTerm});
            return query.hasSolution();
        } catch (Exception e) {
            return false;
        }
    }

    // ==========================================================
    // MÉTODOS AUXILIARES
    // ==========================================================

    private Term convertirTareasATerminos(List<TareaDTO> tareas) {
        System.out.println("🔧 [PrologAPI] Convertir tareas a términos:");
        for (TareaDTO tarea : tareas) {
            System.out.println("    → " + tarea.getNombre() + " | tiempo=" + tarea.getTiempoEstimado());
        }

        Term[] tareasArray = new Term[tareas.size()];
        for (int i = 0; i < tareas.size(); i++) {
            TareaDTO tarea = tareas.get(i);

            Term idTerm = new Atom("id" + tarea.getId().toString());

            String nombreNormalizado = tarea.getNombre()
                    .toLowerCase()
                    .replace(" ", "_")
                    .replaceAll("[^a-z0-9_]", "");

            Term nombreTerm = new Atom(nombreNormalizado);
            Term prioridadTerm = new Atom(tarea.getPrioridad().toLowerCase());
            Term tiempoTerm = new org.jpl7.Float(tarea.getTiempoEstimado());
            Term climasTerm = convertirListaStringsATerminos(tarea.getClimas());

            List<String> depsConPrefijo = new ArrayList<>();
            if (tarea.getDependencias() != null) {
                for (String dep : tarea.getDependencias()) {
                    depsConPrefijo.add("id" + dep.toString());
                }
            }
            Term dependenciasTerm = convertirListaStringsATerminos(depsConPrefijo);

            tareasArray[i] = new Compound("tarea", new Term[]{
                    idTerm, nombreTerm, prioridadTerm, tiempoTerm, climasTerm, dependenciasTerm
            });
        }

        return Util.termArrayToList(tareasArray);
    }

    private Term convertirListaStringsATerminos(List<?> lista) {
        if (lista == null || lista.isEmpty()) {
            return Util.termArrayToList(new Term[]{});
        }

        Term[] array = new Term[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            Object valorObj = lista.get(i);
            String valorStr = String.valueOf(valorObj);
            array[i] = new Atom(valorStr);
        }
        return Util.termArrayToList(array);
    }

    private List<TareaPlanificadaDTO> parsearPlanProlog(Term planTerm) {
        List<TareaPlanificadaDTO> tareas = new ArrayList<>();
        Term[] tareasArray = Util.listToTermArray(planTerm);

        for (Term tareaTerm : tareasArray) {
            if (tareaTerm.isCompound() && tareaTerm.name().equals("tarea_planificada")) {
                Term[] args = tareaTerm.args();
                String id = args[0].name();

                String nombreProlog = args[1].name();
                String nombreLegible = nombreProlog.replace("_", " ");
                String[] palabras = nombreLegible.split(" ");
                StringBuilder nombreFinal = new StringBuilder();
                for (String palabra : palabras) {
                    if (!palabra.isEmpty()) {
                        nombreFinal.append(Character.toUpperCase(palabra.charAt(0)))
                                .append(palabra.substring(1))
                                .append(" ");
                    }
                }
                String nombre = nombreFinal.toString().trim();

                String prioridad = args[2].name();
                Double duracion = Double.parseDouble(args[3].toString());
                List<String> climas = parsearListaStrings(args[4]);
                List<String> dependencias = parsearListaStrings(args[5]);
                Double horaInicio = Double.parseDouble(args[6].toString());
                Double horaFin = Double.parseDouble(args[7].toString());

                tareas.add(new TareaPlanificadaDTO(
                        id, nombre, prioridad, duracion, climas, dependencias, horaInicio, horaFin
                ));
            }
        }
        return tareas;
    }

    private List<String> parsearListaStrings(Term listaTerm) {
        List<String> lista = new ArrayList<>();
        if (listaTerm.name().equals("[]")) return lista;

        Term[] array = Util.listToTermArray(listaTerm);
        for (Term term : array) lista.add(term.name());
        return lista;
    }

    private Double calcularTiempoTotal(List<TareaPlanificadaDTO> tareas) {
        return tareas.stream().mapToDouble(TareaPlanificadaDTO::getTiempoEstimado).sum();
    }

    private String generarMensajePlan(String estado, int tareasPlanificadas, int tareasTotales) {
        switch (estado) {
            case "completo":
                return "Plan generado exitosamente. Todas las tareas fueron planificadas.";
            case "parcial":
                return String.format("Plan parcial generado. Se planificaron %d de %d tareas.",
                        tareasPlanificadas, tareasTotales);
            case "imposible":
                return "No se pudo generar un plan viable con las restricciones actuales.";
            default:
                return "Estado desconocido";
        }
    }

    private String generarMensajeViabilidad(String resultado, Double tiempoTotal, Double tiempoDisponible) {
        switch (resultado) {
            case "viable":
                return String.format("Todas las tareas son viables. Tiempo necesario: %.2f horas de %.2f disponibles.",
                        tiempoTotal, tiempoDisponible);
            case "parcial":
                return String.format("Solo algunas tareas son viables. Tiempo necesario: %.2f horas, disponible: %.2f horas.",
                        tiempoTotal, tiempoDisponible);
            case "imposible":
                return "No es posible completar ninguna tarea con las restricciones actuales.";
            default:
                return "Resultado desconocido";
        }
    }

    // ==========================================================
    // VALIDACIONES DE REQUESTS
    // ==========================================================
    private void validarPlanificacionRequest(PlanificacionRequest request) {
        if (request.getTareas() == null || request.getTareas().isEmpty())
            throw new IllegalArgumentException("La lista de tareas no puede estar vacía");
        if (request.getTiempoDisponible() == null || request.getTiempoDisponible() <= 0)
            throw new IllegalArgumentException("El tiempo disponible debe ser mayor a 0");
        if (request.getClima() == null || request.getClima().trim().isEmpty())
            throw new IllegalArgumentException("El clima actual no puede estar vacío");
        if (request.getHoraInicio() == null || request.getHoraInicio() < 0 || request.getHoraInicio() > 24)
            throw new IllegalArgumentException("La hora de inicio debe estar entre 0 y 24");
    }

    private void validarViabilidadRequest(ViabilidadRequest request) {
        if (request.getTareas() == null || request.getTareas().isEmpty())
            throw new IllegalArgumentException("La lista de tareas no puede estar vacía");
        if (request.getTiempoDisponible() == null || request.getTiempoDisponible() <= 0)
            throw new IllegalArgumentException("El tiempo disponible debe ser mayor a 0");
        if (request.getClima() == null || request.getClima().trim().isEmpty())
            throw new IllegalArgumentException("El clima actual no puede estar vacío");
    }

    private void validarReplanificacionRequest(ReplanificacionRequest request) {
        if (request.getTareasOriginales() == null || request.getTareasOriginales().isEmpty())
            throw new IllegalArgumentException("La lista de tareas originales no puede estar vacía");
        if (request.getTareasCompletadas() == null)
            throw new IllegalArgumentException("La lista de tareas completadas no puede ser nula");
        if (request.getTiempoDisponible() == null || request.getTiempoDisponible() <= 0)
            throw new IllegalArgumentException("El tiempo disponible debe ser mayor a 0");
        if (request.getClima() == null || request.getClima().trim().isEmpty())
            throw new IllegalArgumentException("El clima actual no puede estar vacío");
        if (request.getHoraInicio() == null || request.getHoraInicio() < 0 || request.getHoraInicio() > 24)
            throw new IllegalArgumentException("La hora de inicio debe estar entre 0 y 24");
    }
}
