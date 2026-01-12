package sv.gob.cementerios.cementeriosle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.gob.cementerios.cementeriosle.model.AccesoCementerio;

import java.util.List;

public interface AccesoCementerioRepository extends JpaRepository<AccesoCementerio, Integer> {

    /**
     * Busca todos los registros de acceso para un usuario específico por su ID.
     */
    List<AccesoCementerio> findByUsuarioIdUsuario(Integer idUsuario);
}