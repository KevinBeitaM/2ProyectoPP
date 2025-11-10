package cr.ac.una.agenda.service;

import cr.ac.una.agenda.dto.TareaDTO;
import cr.ac.una.agenda.entity.Tarea;
import cr.ac.una.agenda.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    public TareaDTO crearTarea(TareaDTO tareaDTO) {
        if (tareaRepository.existsByTareaId(tareaDTO.getTareaId())) {
            throw new RuntimeException("Ya existe una tarea con el ID: " + tareaDTO.getTareaId());
        }

        Tarea tarea = convertirDTOAEntidad(tareaDTO);
        tarea = tareaRepository.save(tarea);
        return new TareaDTO(tarea);
    }

    public TareaDTO actualizarTarea(Long id, TareaDTO tareaDTO) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        tarea.setNombre(tareaDTO.getNombre());
        tarea.setPrioridad(Tarea.Prioridad.valueOf(tareaDTO.getPrioridad().toUpperCase()));
        tarea.setTiempoEstimado(tareaDTO.getTiempoEstimado());

        tarea.setClimas(tareaDTO.getClimas().stream()
                .map(c -> Tarea.Clima.valueOf(c.toUpperCase()))
                .collect(Collectors.toList()));

        tarea.setDependencias(tareaDTO.getDependencias());
        tarea.setFechaLimite(tareaDTO.getFechaLimite());  // ⭐ NUEVO

        tarea = tareaRepository.save(tarea);
        return new TareaDTO(tarea);
    }

    public void eliminarTarea(Long id) {
        tareaRepository.deleteById(id);
    }

    public TareaDTO obtenerTarea(Long id) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        return new TareaDTO(tarea);
    }

    public List<TareaDTO> listarTareas() {
        return tareaRepository.findAll().stream()
                .map(TareaDTO::new)
                .collect(Collectors.toList());
    }

    public List<TareaDTO> listarTareasPendientes() {
        return tareaRepository.findByCompletada(false).stream()
                .map(TareaDTO::new)
                .collect(Collectors.toList());
    }

    public TareaDTO marcarComoCompletada(Long id) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        tarea.setCompletada(true);
        tarea.setFechaCompletada(LocalDateTime.now());
        tarea = tareaRepository.save(tarea);
        return new TareaDTO(tarea);
    }

    private Tarea convertirDTOAEntidad(TareaDTO dto) {
        Tarea tarea = new Tarea();
        tarea.setTareaId(dto.getTareaId());
        tarea.setNombre(dto.getNombre());
        tarea.setPrioridad(Tarea.Prioridad.valueOf(dto.getPrioridad().toUpperCase()));
        tarea.setTiempoEstimado(dto.getTiempoEstimado());

        tarea.setClimas(dto.getClimas().stream()
                .map(c -> Tarea.Clima.valueOf(c.toUpperCase()))
                .collect(Collectors.toList()));

        tarea.setDependencias(dto.getDependencias() != null ? dto.getDependencias() : List.of());
        tarea.setCompletada(false);
        tarea.setFechaLimite(dto.getFechaLimite());  // ⭐ NUEVO

        return tarea;
    }
}