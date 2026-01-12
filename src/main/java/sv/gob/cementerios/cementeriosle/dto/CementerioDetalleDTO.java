package sv.gob.cementerios.cementeriosle.dto;

import java.util.List;

public class CementerioDetalleDTO {

    private Integer idCementerio;
    private String nombreCementerio;
    private String tipoCementerio;

    // Resumen de Espacios
    private Long totalEspacios;
    private Long espaciosOcupados;
    private Long espaciosDisponibles;

    // Listas detalladas
    private List<DifuntoDTO> difuntos;
    private List<PropietarioDTO> propietarios;

    // --- Getters, Setters y Constructor (Omitidos por brevedad, se recomienda generarlos) ---
    // ...
    public CementerioDetalleDTO() {}

    public Integer getIdCementerio() { return idCementerio; }
    public void setIdCementerio(Integer idCementerio) { this.idCementerio = idCementerio; }
    public String getNombreCementerio() { return nombreCementerio; }
    public void setNombreCementerio(String nombreCementerio) { this.nombreCementerio = nombreCementerio; }
    public String getTipoCementerio() { return tipoCementerio; }
    public void setTipoCementerio(String tipoCementerio) { this.tipoCementerio = tipoCementerio; }
    public Long getTotalEspacios() { return totalEspacios; }
    public void setTotalEspacios(Long totalEspacios) { this.totalEspacios = totalEspacios; }
    public Long getEspaciosOcupados() { return espaciosOcupados; }
    public void setEspaciosOcupados(Long espaciosOcupados) { this.espaciosOcupados = espaciosOcupados; }
    public Long getEspaciosDisponibles() { return espaciosDisponibles; }
    public void setEspaciosDisponibles(Long espaciosDisponibles) { this.espaciosDisponibles = espaciosDisponibles; }
    public List<DifuntoDTO> getDifuntos() { return difuntos; }
    public void setDifuntos(List<DifuntoDTO> difuntos) { this.difuntos = difuntos; }
    public List<PropietarioDTO> getPropietarios() { return propietarios; }
    public void setPropietarios(List<PropietarioDTO> propietarios) { this.propietarios = propietarios; }
}