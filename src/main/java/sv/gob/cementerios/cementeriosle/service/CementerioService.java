package sv.gob.cementerios.cementeriosle.service;

import sv.gob.cementerios.cementeriosle.dto.CementerioDetalleDTO;
import sv.gob.cementerios.cementeriosle.dto.CementerioResponseDTO;
import java.util.List;

public interface CementerioService {

    List<CementerioResponseDTO> listarTodos();

    // El que ya tienes para el listado con seguridad
    List<CementerioResponseDTO> obtenerCementeriosPorUsuario(Integer usuarioId, String rolUsuario);

    // Detalle
    CementerioDetalleDTO obtenerDetallePorId(Integer idCementerio);
}