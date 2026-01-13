package sv.gob.cementerios.cementeriosle.dto;

import lombok.Data;
import java.util.List;

@Data
public class UsuarioRegistroDTO {
    private String nombre;
    private String correo;
    private String telefono;
    private String contrasena;
    private Integer idRol;
    private List<Integer> idCementerios;
}