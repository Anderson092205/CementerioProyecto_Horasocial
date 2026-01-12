package sv.gob.cementerios.cementeriosle.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.gob.cementerios.cementeriosle.dto.CementerioDetalleDTO;
import sv.gob.cementerios.cementeriosle.dto.CementerioResponseDTO;
import sv.gob.cementerios.cementeriosle.dto.DifuntoDTO;
import sv.gob.cementerios.cementeriosle.dto.PropietarioDTO;
import sv.gob.cementerios.cementeriosle.model.AccesoCementerio;
import sv.gob.cementerios.cementeriosle.model.Cementerio;
import sv.gob.cementerios.cementeriosle.model.Difunto;
import sv.gob.cementerios.cementeriosle.model.Propietario;
import sv.gob.cementerios.cementeriosle.repository.AccesoCementerioRepository;
import sv.gob.cementerios.cementeriosle.repository.CementerioRepository;
import sv.gob.cementerios.cementeriosle.repository.DifuntoRepository;
import sv.gob.cementerios.cementeriosle.repository.PropietarioRepository;
import sv.gob.cementerios.cementeriosle.service.CementerioService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CementerioServiceImpl implements CementerioService {

    @Autowired private CementerioRepository cementerioRepository;
    @Autowired private DifuntoRepository difuntoRepository;
    @Autowired private PropietarioRepository propietarioRepository;
    @Autowired private AccesoCementerioRepository accesoCementerioRepository; // Repositorio de permisos

    // ==========================================================
    // 1. MÉTODO DASHBOARD (APLICACIÓN DEL FILTRO DE PERMISOS SEGURO)
    // ==========================================================
    @Override
    @Transactional(readOnly = true)
    public List<CementerioResponseDTO> obtenerCementeriosPorUsuario(Integer usuarioId, String rolUsuario) {

        // ⭐ Lógica de Autorización Basada en Rol (Usando datos seguros del JWT) ⭐

        // 1. Rol "ADMIN": Puede ver todos los cementerios
        if ("ADMIN".equals(rolUsuario)) {
            List<Cementerio> cementerios = cementerioRepository.findAll();
            return cementerios.stream()
                    .map(this::mapearADTO)
                    .collect(Collectors.toList());
        }

        // 2. Otros Roles (OPERADOR, CONSULTA, etc.): Filtrar por tabla de acceso

        // a. Obtener los registros de acceso que dan permiso de ver al usuario
        List<AccesoCementerio> accesos = accesoCementerioRepository.findByUsuarioIdUsuario(usuarioId);

        if (accesos.isEmpty()) {
            return Collections.emptyList(); // Retorna una lista vacía si no tiene ningún registro de acceso
        }

        // b. Extraer solo los IDs de los Cementerios a los que tiene permiso activo
        List<Integer> cementerioIds = accesos.stream()
                .filter(AccesoCementerio::getPuedeVer)
                .map(acceso -> acceso.getCementerio().getIdCementerio())
                .collect(Collectors.toList());

        if (cementerioIds.isEmpty()) {
            return Collections.emptyList(); // Retorna una lista vacía si los permisos son nulos o están inactivos
        }

        // c. Busca las entidades Cementerio usando la lista de IDs obtenida
        List<Cementerio> cementerios = cementerioRepository.findAllById(cementerioIds);

        // d. Mapea la lista de Entidades a una lista de DTOs
        return cementerios.stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    // ==========================================================
    // 2. MÉTODO DETALLE (Mantiene la lógica existente)
    // ==========================================================
    @Override
    @Transactional(readOnly = true)
    public CementerioDetalleDTO obtenerDetallePorId(Integer idCementerio) {
        // En una implementación de seguridad completa, se debería añadir aquí la
        // verificación de permisos: ¿El usuario actual (leído del JWT) tiene permiso
        // para ver este idCementerio?
        // Si la respuesta es NO, lanzar una excepción de Acceso Denegado.

        Cementerio cementerio = cementerioRepository.findById(idCementerio)
                .orElseThrow(() -> new EntityNotFoundException("Cementerio no encontrado con ID: " + idCementerio));

        CementerioDetalleDTO detalleDTO = new CementerioDetalleDTO();
        detalleDTO.setIdCementerio(cementerio.getIdCementerio());
        detalleDTO.setNombreCementerio(cementerio.getNombre());
        detalleDTO.setTipoCementerio(cementerio.getTipo());

        // Obtener CONTEO REAL de Espacios
        // Se asume que estos métodos existen en su CementerioRepository
        Long total = cementerioRepository.contarTotalEspaciosPorCementerio(idCementerio);
        Long ocupados = cementerioRepository.contarEspaciosOcupadosPorCementerio(idCementerio);
        Long disponibles = total - ocupados;

        detalleDTO.setTotalEspacios(total);
        detalleDTO.setEspaciosOcupados(ocupados);
        detalleDTO.setEspaciosDisponibles(disponibles);

        // Obtener Difuntos
        List<Difunto> difuntos = difuntoRepository.findByIdCementerio(idCementerio);
        List<DifuntoDTO> difuntoDTOs = difuntos.stream()
                .map(this::mapearDifuntoADTO)
                .collect(Collectors.toList());
        detalleDTO.setDifuntos(difuntoDTOs);

        // Obtener Propietarios
        List<Propietario> propietarios = propietarioRepository.findPropietariosByCementerioId(idCementerio);
        List<PropietarioDTO> propietarioDTOs = propietarios.stream()
                .map(p -> mapearPropietarioADTO(p, 0L))
                .collect(Collectors.toList());
        detalleDTO.setPropietarios(propietarioDTOs);

        return detalleDTO;
    }

    // --- Métodos de Mapeo (Auxiliares) ---
    private CementerioResponseDTO mapearADTO(Cementerio cementerio) {
        CementerioResponseDTO dto = new CementerioResponseDTO();
        dto.setId(cementerio.getIdCementerio());
        dto.setNombre(cementerio.getNombre());
        dto.setTipo(cementerio.getTipo());
        return dto;
    }

    // ... (Mantén tus otros métodos de mapeo: mapearDifuntoADTO y mapearPropietarioADTO)
    private DifuntoDTO mapearDifuntoADTO(Difunto difunto) {
        DifuntoDTO dto = new DifuntoDTO();
        dto.setIdDifunto(difunto.getIdDifunto());
        dto.setNombre(difunto.getNombre());
        dto.setFechaDefuncion(difunto.getFechaDefuncion());
        dto.setUbicacion("Ubicación pendiente de detalle (Lote/Fila/Espacio)");
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