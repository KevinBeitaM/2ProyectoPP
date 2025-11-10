package cr.ac.una.agenda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "configuracion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configuracion {

    @Id
    private Long id = 1L;  // Siempre usamos ID 1 (singleton)

    @Column(nullable = false)
    private Double tiempoDisponibleDefault = 8.0;  // 8 horas por defecto

    @Column(nullable = false)
    private Double horaInicioDefault = 8.0;  // 8:00 AM por defecto

    @Column(nullable = false)
    private String climaDefault = "cualquiera";
}