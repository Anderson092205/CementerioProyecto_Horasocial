package sv.gob.cementerios.cementeriosle.model;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Table(name = "rol")
@Data
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRol;

    private String nombre;
}