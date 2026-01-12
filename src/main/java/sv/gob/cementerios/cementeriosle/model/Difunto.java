package sv.gob.cementerios.cementeriosle.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "difunto")
public class Difunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_difunto")
    private Integer idDifunto;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "fecha_defuncion")
    private LocalDate fechaDefuncion;

    // NOTA: La columna 'ubicacion' en tu esquema no tiene FK, la usaremos para filtrar por cementerio
    @Column(name = "id_cementerio")
    private Integer idCementerio;

    // --- Getters y Setters (Omitidos por brevedad) ---
    // ...
    public Difunto() {}

    public Integer getIdDifunto() { return idDifunto; }
    public void setIdDifunto(Integer idDifunto) { this.idDifunto = idDifunto; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public LocalDate getFechaDefuncion() { return fechaDefuncion; }
    public void setFechaDefuncion(LocalDate fechaDefuncion) { this.fechaDefuncion = fechaDefuncion; }
    public Integer getIdCementerio() { return idCementerio; }
    public void setIdCementerio(Integer idCementerio) { this.idCementerio = idCementerio; }
}