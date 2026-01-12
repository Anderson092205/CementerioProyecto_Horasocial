package sv.gob.cementerios.cementeriosle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.gob.cementerios.cementeriosle.dto.CementerioDetalleDTO;
import sv.gob.cementerios.cementeriosle.dto.CementerioResponseDTO;
import sv.gob.cementerios.cementeriosle.service.CementerioService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cementerios")
@CrossOrigin(origins = "http://localhost:4200")
public class CementerioController {

    @Autowired
    private CementerioService cementerioService;

    // ==========================================================
    // 1. ENDPOINT DE LISTADO SEGURO (Manejo de Permisos)
    //    Ruta: GET /api/v1/cementerios
    // ==========================================================
    @GetMapping
    public ResponseEntity<List<CementerioResponseDTO>> obtenerCementeriosPorUsuario(
            // ⭐ CAMBIO CRÍTICO DE SEGURIDAD ⭐
            // Obtenemos los datos de la solicitud, donde fueron colocados por el filtro JWT.
            // Estos valores son seguros, pues provienen de un token firmado.
            @RequestAttribute("usuarioId") Integer usuarioId,
            @RequestAttribute("rol") String rol) {

        // Llama al servicio, que internamente filtra por ADMIN o por tabla de accesos
        List<CementerioResponseDTO> cementerios =
                // Usa el nombre de método correcto de su implementación de servicio
                cementerioService.obtenerCementeriosPorUsuario(usuarioId, rol);

        if (cementerios.isEmpty()) {
            return ResponseEntity.ok(cementerios);
        }

        return ResponseEntity.ok(cementerios);
    }

    // ==========================================================
    // 2. ENDPOINT DE DETALLE
    //    Ruta: GET /api/v1/cementerios/{id}
    // ==========================================================
    @GetMapping("/{id}")
    public ResponseEntity<CementerioDetalleDTO> obtenerDetalleCementerio(@PathVariable Integer id) {
        // En un sistema completamente seguro, esta función también debería verificar si
        // el usuario actual tiene permiso para ver este ID específico.
        CementerioDetalleDTO detalle = cementerioService.obtenerDetallePorId(id);
        return ResponseEntity.ok(detalle);
    }
}