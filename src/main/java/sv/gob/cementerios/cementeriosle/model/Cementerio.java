package sv.gob.cementerios.cementeriosle.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@Entity
@Table(name = "cementerio")
public class Cementerio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cementerio")
    private Integer idCementerio;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "tipo", length = 20)
    private String tipo; // Publico, Privado, Cenizas

    @Column(name = "id_municipio")
    private Integer idMunicipio;

    public Cementerio() {
    }

    public Integer getIdCementerio() {
        return idCementerio;
    }

    public void setIdCementerio(Integer idCementerio) {
        this.idCementerio = idCementerio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }
}