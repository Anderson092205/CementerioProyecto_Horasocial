package sv.gob.cementerios.cementeriosle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.gob.cementerios.cementeriosle.dto.UsuarioRegistroDTO;
import sv.gob.cementerios.cementeriosle.model.*;
import sv.gob.cementerios.cementeriosle.repository.*;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RolRepository rolRepository;
    @Autowired private CementerioRepository cementerioRepository;
    @Autowired private AccesoCementerioRepository accesoCementerioRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario guardarUsuario(UsuarioRegistroDTO dto) {
        if (usuarioRepository.findByCorreo(dto.getCorreo()).isPresent()) {
            throw new RuntimeException("Error: El correo electrónico ya existe.");
        }

        Rol rol = rolRepository.findById(dto.getIdRol())
                .orElseThrow(() -> new RuntimeException("Error: El Rol no existe."));

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setCorreo(dto.getCorreo());
        usuario.setTelefono(dto.getTelefono());
        usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        usuario.setEsTemporal(true);
        usuario.setActivo(true);
        usuario.setRol(rol);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        if (!rol.getNombre().equalsIgnoreCase("ADMIN") && dto.getIdCementerios() != null) {
            for (Integer idCem : dto.getIdCementerios()) {
                Cementerio cementerio = cementerioRepository.findById(idCem)
                        .orElseThrow(() -> new RuntimeException("Error: El Cementerio no existe."));

                AccesoCementerio acceso = new AccesoCementerio();
                acceso.setUsuario(usuarioGuardado);
                acceso.setCementerio(cementerio);
                acceso.setPuedeVer(true);
                accesoCementerioRepository.save(acceso);
            }
        }
        return usuarioGuardado;
    }

    @Transactional
    public void actualizarPasswordTemporal(String correo, String nuevaPassword) {
        // 1. Busca al usuario por correo
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Encripta la nueva clave
        usuario.setContrasena(passwordEncoder.encode(nuevaPassword));

        // 3. Quita el estado de "temporal"
        usuario.setEsTemporal(false);

        // 4. Guarda los cambios
        usuarioRepository.save(usuario);
    }
}