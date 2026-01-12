package sv.gob.cementerios.cementeriosle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.gob.cementerios.cementeriosle.dto.UsuarioRegistroDTO;
import sv.gob.cementerios.cementeriosle.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody UsuarioRegistroDTO dto) {
        try {
            return ResponseEntity.ok(usuarioService.guardarUsuario(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}