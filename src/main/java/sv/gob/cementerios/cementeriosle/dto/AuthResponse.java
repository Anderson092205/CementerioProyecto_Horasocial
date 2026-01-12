package sv.gob.cementerios.cementeriosle.dto;

public class AuthResponse {

    private final String token;
    private final Integer id;       // Tipo de ID que coincide con tu repositorio (Integer)
    private final String rol;

    // Constructor que usa todos los campos
    public AuthResponse(String token, Integer id, String rol) {
        this.token = token;
        this.id = id;
        this.rol = rol;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public Integer getId() {
        return id;
    }

    public String getRol() {
        return rol;
    }
}