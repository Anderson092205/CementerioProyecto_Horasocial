package sv.gob.cementerios.cementeriosle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sv.gob.cementerios.cementeriosle.model.Difunto;

import java.util.List;

@Repository
public interface DifuntoRepository extends JpaRepository<Difunto, Integer> {

    /** Busca todos los difuntos por el ID del cementerio. */
    List<Difunto> findByIdCementerio(Integer idCementerio);
}