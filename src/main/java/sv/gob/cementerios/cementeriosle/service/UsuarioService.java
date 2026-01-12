package sv.gob.cementerios.cementeriosle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sv.gob.cementerios.cementeriosle.dto.UsuarioRegistroDTO;
import sv.gob.cementerios.cementeriosle.model.Rol;
import sv.gob.cementerios.cementeriosle.model.Usuario;
import sv.gob.cementerios.cementeriosle.repository.RolRepository;
import sv.gob.cementerios.cementeriosle.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario guardarUsuario(UsuarioRegistroDTO dto) {
        // 1. Validar si el correo ya existe
        if (usuarioRepository.findByCorreo(dto.getCorreo()).isPresent()) {
            throw new RuntimeException("Error: El correo electrónico ya está registrado.");
        }

        // 2. Buscar el rol solicitado
        Rol rol = rolRepository.findById(dto.getIdRol())
                .orElseThrow(() -> new RuntimeException("Error: El Rol no existe."));

        // 3. Crear y mapear la entidad
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setCorreo(dto.getCorreo());
        usuario.setTelefono(dto.getTelefono());
        usuario.setEsTemporal(true);
        usuario.setActivo(true);
        usuario.setRol(rol);

        // 4. IMPORTANTE: Encriptar la contraseña antes de guardar
        usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));

        return usuarioRepository.save(usuario);
    }
}