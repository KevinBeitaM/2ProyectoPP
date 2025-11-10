package cr.ac.una.agenda.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import cr.ac.una.agenda.dto.PlanDTO;
import cr.ac.una.agenda.dto.ViabilidadDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Service
public class PrologClientService {

    private final WebClient webClient;
    private static final String PROLOG_SERVICE_URL = "http://prologapi:8080/api";
    private final ObjectMapper mapper = new ObjectMapper();

    public PrologClientService() {
        this.webClient = WebClient.builder()
                .baseUrl(PROLOG_SERVICE_URL)
                .build();
    }

    /**
     * Llamada al endpoint /api/planificar de apiProlog
     * Devuelve un PlanDTO con las tareas planificadas correctamente mapeadas
     */
    public PlanDTO generarPlan(Map<String, Object> request) {
        try {
            System.out.println("[Agenda → PrologAPI] Request: " + request);

            // 🔹 Recibimos el JSON crudo
            String jsonResponse = webClient.post()
                    .uri("/planificar")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("[Agenda ← PrologAPI] Raw JSON: " + jsonResponse);

            // 🔹 Convertimos el JSON directamente al DTO
            PlanDTO response = mapper.readValue(jsonResponse, PlanDTO.class);
            return response;

        } catch (WebClientResponseException e) {
            System.err.println("⚠️ Error HTTP desde PrologAPI: " + e.getStatusCode()
                    + " - " + e.getResponseBodyAsString());
            throw new RuntimeException("Error HTTP desde PrologAPI: " + e.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al conectar con PrologAPI: " + e.getMessage());
        }
    }

    /**
     * Llamada al endpoint /api/verificar-viabilidad de apiProlog
     * Devuelve un ViabilidadDTO con el resultado del análisis
     */
    public ViabilidadDTO verificarViabilidad(Map<String, Object> request) {
        try {
            System.out.println("[Agenda → PrologAPI] Request (viabilidad): " + request);

            String jsonResponse = webClient.post()
                    .uri("/verificar-viabilidad")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("[Agenda ← PrologAPI] Raw JSON: " + jsonResponse);

            ViabilidadDTO response = mapper.readValue(jsonResponse, ViabilidadDTO.class);
            return response;

        } catch (WebClientResponseException e) {
            System.err.println("⚠️ Error HTTP desde PrologAPI: " + e.getStatusCode()
                    + " - " + e.getResponseBodyAsString());
            throw new RuntimeException("Error HTTP desde PrologAPI: " + e.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al conectar con PrologAPI: " + e.getMessage());
        }
    }

    /**
     * Test de conexión con apiProlog
     */
    public Integer testSuma(int a, int b) {
        try {
            Map<String, Object> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/sum")
                            .queryParam("a", a)
                            .queryParam("b", b)
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            System.out.println("[Agenda ← PrologAPI] Test suma: " + response);
            return (Integer) response.get("resultado");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al probar conexión con PrologAPI: " + e.getMessage());
        }
    }
}
