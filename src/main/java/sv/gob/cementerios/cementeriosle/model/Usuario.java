package sv.gob.cementerios.cementeriosle.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    @Column(length = 8)
    private String telefono;

    @Column(name = "contraseña", nullable = false, length = 255)
    private String contrasena;

    @Column(name = "es_temporal", nullable = false)
    private Boolean esTemporal = true;

    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol", foreignKey = @ForeignKey(name = "FK_usuario_rol"))
    private Rol rol;
}