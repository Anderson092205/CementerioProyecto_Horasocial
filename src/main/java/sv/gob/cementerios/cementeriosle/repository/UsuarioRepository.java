package sv.gob.cementerios.cementeriosle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.gob.cementerios.cementeriosle.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Método clave para buscar usuario por correo (username)
    Optional<Usuario> findByCorreo(String correo);
}