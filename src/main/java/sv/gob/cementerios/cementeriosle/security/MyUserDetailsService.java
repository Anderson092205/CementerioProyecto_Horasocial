package sv.gob.cementerios.cementeriosle.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sv.gob.cementerios.cementeriosle.model.Usuario;
import sv.gob.cementerios.cementeriosle.repository.UsuarioRepository;

import java.util.Collections;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        // Usa findByCorreo(String correo) de tu JpaRepository
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));

        // Crea el objeto UserDetails que Spring Security usa para la autenticación
        return new User(
                usuario.getCorreo(),
                usuario.getContrasena(), // Aquí obtiene el hash de la DB
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre()))
        );
    }
}