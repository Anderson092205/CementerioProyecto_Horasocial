package sv.gob.cementerios.cementeriosle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sv.gob.cementerios.cementeriosle.model.Propietario;

import java.util.List;

@Repository
public interface PropietarioRepository extends JpaRepository<Propietario, Integer> {

    // Consulta nativa (o JPQL compleja) para obtener propietarios por cementerio
    // Usaremos JOINs a través de las tablas intermedias (parcela_propietario -> lote)
    @Query(value = "SELECT p.*, COUNT(pp.id_lote) as totalLotes " +
            "FROM propietario p " +
            "JOIN parcela_propietario pp ON p.id_propietario = pp.id_propietario " +
            "JOIN lote l ON pp.id_lote = l.id_lote " +
            "WHERE l.id_cementerio = :idCementerio " +
            "GROUP BY p.id_propietario, p.nombre, p.telefono, p.correo",
            nativeQuery = true)
    List<Propietario> findPropietariosByCementerioId(@Param("idCementerio") Integer idCementerio);
}