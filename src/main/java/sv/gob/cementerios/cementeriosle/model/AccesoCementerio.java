package sv.gob.cementerios.cementeriosle.model;

import jakarta.persistence.*;
import lombok.Data;
// Si no usas Lombok, añade getters y setters manualmente

@Entity
@Table(name = "acceso_cementerio")
@Data
public class AccesoCementerio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAcceso;

    // Relación con la tabla 'usuario'
    // id_usuario es la clave foránea en la tabla acceso_cementerio
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

    // Si no usas @Data de Lombok, añade los siguientes métodos:
    /*
    public Integer getIdAcceso() { return idAcceso; }
    public void setIdAcceso(Integer idAcceso) { this.idAcceso = idAcceso; }
    // ... y el resto de Getters/Setters
    */
}