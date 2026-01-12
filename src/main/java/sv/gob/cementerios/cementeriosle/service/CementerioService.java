package sv.gob.cementerios.cementeriosle.service;

import sv.gob.cementerios.cementeriosle.dto.CementerioDetalleDTO;
import sv.gob.cementerios.cementeriosle.dto.CementerioResponseDTO;

import java.util.List;

public interface CementerioService {

    // 🚨 Este es el método nuevo/reemplazado
    List<CementerioResponseDTO> obtenerCementeriosPorUsuario(Integer usuarioId, String rolUsuario);

    // Este método ya existía
    CementerioDetalleDTO obtenerDetallePorId(Integer idCementerio);

}