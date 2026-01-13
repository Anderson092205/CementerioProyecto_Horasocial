package sv.gob.cementerios.cementeriosle.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.gob.cementerios.cementeriosle.dto.*;
import sv.gob.cementerios.cementeriosle.model.*;
import sv.gob.cementerios.cementeriosle.repository.*;
import sv.gob.cementerios.cementeriosle.service.CementerioService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CementerioServiceImpl implements CementerioService {

    @Autowired private CementerioRepository cementerioRepository;
    @Autowired private DifuntoRepository difuntoRepository;
    @Autowired private PropietarioRepository propietarioRepository;
    @Autowired private AccesoCementerioRepository accesoCementerioRepository;

    // ==========================================================
    // 1. MÉTODO DASHBOARD (FILTRADO SEGÚN TU REQUERIMIENTO)
    // ==========================================================
    @Override
    @Transactional(readOnly = true)
    public List<CementerioResponseDTO> obtenerCementeriosPorUsuario(Integer usuarioId, String rolUsuario) {

        // ⭐ REGLA: ADMIN, INFORMATICA y CONSULTA ven todo.
        // Solo el OPERADOR (u otros) filtran por tabla de acceso.
        if ("ADMIN".equals(rolUsuario) || "INFORMATICA".equals(rolUsuario) || "CONSULTA".equals(rolUsuario)) {
            return cementerioRepository.findAll().stream()
                    .map(this::mapearADTO)
                    .collect(Collectors.toList());
        }

        // --- LÓGICA PARA OPERADOR ---

        // a. Buscamos los accesos en la tabla intermedia
        List<AccesoCementerio> accesos = accesoCementerioRepository.findByUsuarioIdUsuario(usuarioId);

        if (accesos.isEmpty()) {
            return Collections.emptyList();
        }

        // b. Extraer IDs de cementerios permitidos
        List<Integer> cementerioIds = accesos.stream()
                .filter(AccesoCementerio::getPuedeVer)
                .map(acceso -> acceso.getCementerio().getIdCementerio())
                .collect(Collectors.toList());

        if (cementerioIds.isEmpty()) {
            return Collections.emptyList();
        }

        // c. Retornar solo los cementerios asignados
        return cementerioRepository.findAllById(cementerioIds).stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    // ==========================================================
    // 2. MÉTODO DETALLE (CON PROTECCIÓN ADICIONAL)
    // ==========================================================
    @Override
    @Transactional(readOnly = true)
    public CementerioDetalleDTO obtenerDetallePorId(Integer idCementerio) {

        // Buscamos la entidad base
        Cementerio cementerio = cementerioRepository.findById(idCementerio)
                .orElseThrow(() -> new EntityNotFoundException("Cementerio no encontrado con ID: " + idCementerio));

        CementerioDetalleDTO detalleDTO = new CementerioDetalleDTO();
        detalleDTO.setIdCementerio(cementerio.getIdCementerio());
        detalleDTO.setNombreCementerio(cementerio.getNombre());
        detalleDTO.setTipoCementerio(cementerio.getTipo());

        // Conteo de espacios (Asegúrate de tener estos métodos en tu Repository)
        Long total = cementerioRepository.contarTotalEspaciosPorCementerio(idCementerio);
        Long ocupados = cementerioRepository.contarEspaciosOcupadosPorCementerio(idCementerio);
        detalleDTO.setTotalEspacios(total);
        detalleDTO.setEspaciosOcupados(ocupados);
        detalleDTO.setEspaciosDisponibles(total - ocupados);

        // Mapeo de Difuntos
        detalleDTO.setDifuntos(difuntoRepository.findByIdCementerio(idCementerio).stream()
                .map(this::mapearDifuntoADTO)
                .collect(Collectors.toList()));

        // Mapeo de Propietarios
        detalleDTO.setPropietarios(propietarioRepository.findPropietariosByCementerioId(idCementerio).stream()
                .map(p -> mapearPropietarioADTO(p, 0L))
                .collect(Collectors.toList()));

        return detalleDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CementerioResponseDTO> listarTodos() {
        return cementerioRepository.findAll().stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    // --- MÉTODOS AUXILIARES ---
    private CementerioResponseDTO mapearADTO(Cementerio cementerio) {
        CementerioResponseDTO dto = new CementerioResponseDTO();
        dto.setId(cementerio.getIdCementerio());
        dto.setNombre(cementerio.getNombre());
        dto.setTipo(cementerio.getTipo());
        return dto;
    }

    private DifuntoDTO mapearDifuntoADTO(Difunto difunto) {
        DifuntoDTO dto = new DifuntoDTO();
        dto.setIdDifunto(difunto.getIdDifunto());
        dto.setNombre(difunto.getNombre());
        dto.setFechaDefuncion(difunto.getFechaDefuncion());
        dto.setUbicacion("Consultar detalle de lote");
        return dto;
    }

    private PropietarioDTO mapearPropietarioADTO(Propietario propietario, Long totalLotes) {
        PropietarioDTO dto = new PropietarioDTO();
        dto.setIdPropietario(propietario.getIdPropietario());
        dto.setNombre(propietario.getNombre());
        dto.setTelefono(propietario.getTelefono());
        dto.setCorreo(propietario.getCorreo());
        dto.setTotalLotes(totalLotes);
        return dto;
    }


}