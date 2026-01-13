package sv.gob.cementerios.cementeriosle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.gob.cementerios.cementeriosle.dto.UsuarioRegistroDTO;
import sv.gob.cementerios.cementeriosle.service.UsuarioService;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Método para crear usuarios (existente)
    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody UsuarioRegistroDTO dto) {
        return ResponseEntity.ok(usuarioService.guardarUsuario(dto));
    }

    // ⭐ MÉTODO UNIFICADO PARA EVITAR EL BeanCreationException
    @PatchMapping("/actualizar-password-temporal")
    public ResponseEntity<?> actualizarPassword(@RequestBody Map<String, String> request) {
        String correo = request.get("correo");
        String nuevaPassword = request.get("nuevaPassword");

        usuarioService.actualizarPasswordTemporal(correo, nuevaPassword);

        return ResponseEntity.ok(Map.of("message", "Contraseña actualizada exitosamente"));
    }
}