package sv.gob.cementerios.cementeriosle.dto;

import lombok.Data;

@Data
public class UsuarioRegistroDTO {
    private String nombre;
    private String correo;
    private String telefono;
    private String contrasena;
    private Integer idRol;
}