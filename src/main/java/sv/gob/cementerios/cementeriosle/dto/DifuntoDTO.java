package sv.gob.cementerios.cementeriosle.dto;

import java.time.LocalDate;

public class DifuntoDTO {
    private Integer idDifunto;
    private String nombre;
    private LocalDate fechaDefuncion;
    private String ubicacion; // Lote, Fila, Posición

    // --- Getters, Setters y Constructor (Omitidos por brevedad) ---
    // ...
    public DifuntoDTO() {}

    public Integer getIdDifunto() { return idDifunto; }
    public void setIdDifunto(Integer idDifunto) { this.idDifunto = idDifunto; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public LocalDate getFechaDefuncion() { return fechaDefuncion; }
    public void setFechaDefuncion(LocalDate fechaDefuncion) { this.fechaDefuncion = fechaDefuncion; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
}