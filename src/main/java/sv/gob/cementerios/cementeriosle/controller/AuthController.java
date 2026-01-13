package sv.gob.cementerios.cementeriosle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sv.gob.cementerios.cementeriosle.dto.AuthRequest;
import sv.gob.cementerios.cementeriosle.dto.AuthResponse;
import sv.gob.cementerios.cementeriosle.model.Usuario;
import sv.gob.cementerios.cementeriosle.repository.UsuarioRepository;
import sv.gob.cementerios.cementeriosle.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        try {
            // 1. Intenta autenticar al usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getCorreo(), authRequest.getPassword())
            );

            // 2. Obtiene los detalles de seguridad
            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // 3. BUSCAR LA ENTIDAD REAL DEL USUARIO (Usando el correo)
            Usuario usuario = usuarioRepository.findByCorreo(userDetails.getUsername())
                    .orElseThrow(() -> new BadCredentialsException("Usuario no encontrado en la base de datos después de autenticación exitosa."));

            // 4. Genera el token JWT
            final String jwt = jwtUtil.generateTokenWithClaims(
                    userDetails,
                    usuario.getIdUsuario(),
                    usuario.getRol().getNombre() // ⭐ USANDO getNombre() para seguridad
            );


            // 5. Devuelve la respuesta COMPLETA incluyendo la bandera temporal
            AuthResponse response = new AuthResponse(
                    jwt,
                    usuario.getIdUsuario(),
                    usuario.getRol().getNombre(),
                    usuario.getEsTemporal() // <--- Enviamos el estado real de la DB
            );
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            // Credenciales incorrectas (401)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales de usuario o contraseña incorrectas.");
        } catch (Exception e) {
            // Cualquier otra excepción
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor: " + e.getMessage());
        }
    }
}