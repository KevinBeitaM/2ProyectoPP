package cr.ac.una.agenda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tareas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String tareaId;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridad prioridad;

    @Column(nullable = false)
    private Double tiempoEstimado;

    @ElementCollection
    @CollectionTable(name = "tarea_climas", joinColumns = @JoinColumn(name = "tarea_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "clima")
    private List<Clima> climas = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "tarea_dependencias", joinColumns = @JoinColumn(name = "tarea_id"))
    @Column(name = "dependencia_id")
    private List<String> dependencias = new ArrayList<>();

    @Column(nullable = false)
    private Boolean completada = false;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_completada")
    private LocalDateTime fechaCompletada;

    // ⭐ NUEVO CAMPO
    @Column(name = "fecha_limite")
    private LocalDateTime fechaLimite;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    public enum Prioridad {
        ALTA, MEDIA, BAJA
    }

    public enum Clima {
        SOLEADO, NUBLADO, LLUVIOSO, CUALQUIERA
    }
}