package sv.gob.cementerios.cementeriosle.dto;

public class PropietarioDTO {
    private Integer idPropietario;
    private String nombre;
    private String telefono;
    private String correo;
    private Long totalLotes; // Conteo de lotes que posee

    // --- Getters, Setters y Constructor (Omitidos por brevedad) ---
    // ...
    public PropietarioDTO() {}

    public Integer getIdPropietario() { return idPropietario; }
    public void setIdPropietario(Integer idPropietario) { this.idPropietario = idPropietario; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public Long getTotalLotes() { return totalLotes; }
    public void setTotalLotes(Long totalLotes) { this.totalLotes = totalLotes; }
}