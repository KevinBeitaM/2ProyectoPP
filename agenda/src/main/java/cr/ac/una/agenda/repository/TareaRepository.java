package cr.ac.una.agenda.repository;

import cr.ac.una.agenda.entity.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {

    Optional<Tarea> findByTareaId(String tareaId);

    List<Tarea> findByCompletada(Boolean completada);

    @Query("SELECT t FROM Tarea t WHERE t.completada = false ORDER BY t.prioridad ASC, t.fechaCreacion ASC")
    List<Tarea> findTareasPendientesOrdenadas();

    boolean existsByTareaId(String tareaId);
}