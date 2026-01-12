package sv.gob.cementerios.cementeriosle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sv.gob.cementerios.cementeriosle.model.Cementerio;

import java.util.List;

public interface CementerioRepository extends JpaRepository<Cementerio, Integer> {

    // 1. Consulta para filtrar cementerios por el ID del usuario
    // Une Cementerio con AccesoCementerio usando el ID de Usuario para filtrar.
    @Query("SELECT c FROM Cementerio c JOIN AccesoCementerio ac ON c.idCementerio = ac.cementerio.idCementerio WHERE ac.usuario.idUsuario = :usuarioId AND ac.puedeVer = true")
    List<Cementerio> findCementeriosByUsuarioId(@Param("usuarioId") Integer usuarioId);

    // 2. Consulta para contar el total de espacios en un cementerio específico (SQL Nativo)
    @Query(value = "SELECT COUNT(e.id_espacio) FROM cementerio AS c " +
            "JOIN lote AS l ON c.id_cementerio = l.id_cementerio " +
            "JOIN fila AS f ON l.id_lote = f.id_lote " +
            "JOIN espacio AS e ON f.id_fila = e.id_fila " +
            "WHERE c.id_cementerio = :idCementerio", nativeQuery = true)
    Long contarTotalEspaciosPorCementerio(@Param("idCementerio") Integer idCementerio);

    // 3. Consulta para contar los espacios OCUPADOS (con beneficiario) (SQL Nativo)
    @Query(value = "SELECT COUNT(e.id_espacio) FROM cementerio AS c " +
            "JOIN lote AS l ON c.id_cementerio = l.id_cementerio " +
            "JOIN fila AS f ON l.id_lote = f.id_lote " +
            "JOIN espacio AS e ON f.id_fila = e.id_fila " +
            "JOIN beneficiario AS b ON e.id_espacio = b.id_espacio " +
            "WHERE c.id_cementerio = :idCementerio", nativeQuery = true)
    Long contarEspaciosOcupadosPorCementerio(@Param("idCementerio") Integer idCementerio);

}