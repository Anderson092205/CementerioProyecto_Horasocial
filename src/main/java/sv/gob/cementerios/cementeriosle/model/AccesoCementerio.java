package sv.gob.cementerios.cementeriosle.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "acceso_cementerio")
@Data
public class AccesoCementerio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_acceso")
    private Integer idAcceso;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    // Relación con la tabla 'cementerio'
    // id_cementerio es la clave foránea en la tabla acceso_cementerio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cementerio", nullable = false)
    private Cementerio cementerio;

    @Column(name = "puede_ver", nullable = false)
    private Boolean puedeVer;

}