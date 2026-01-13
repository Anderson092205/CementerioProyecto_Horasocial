package sv.gob.cementerios.cementeriosle.dto;

public class AuthResponse {

    private final String token;
    private final Integer id;       // Tipo de ID que coincide con tu repositorio (Integer)
    private final String rol;
    private final Boolean esTemporal;

    // Constructor que usa todos los campos
    public AuthResponse(String token, Integer id, String rol, Boolean esTemporal) {
        this.token = token;
        this.id = id;
        this.rol = rol;
        this.esTemporal = esTemporal;
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

    public Boolean getEsTemporal() { return esTemporal; }
}