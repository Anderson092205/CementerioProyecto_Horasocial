package sv.gob.cementerios.cementeriosle.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String correo;
    private String password;
}