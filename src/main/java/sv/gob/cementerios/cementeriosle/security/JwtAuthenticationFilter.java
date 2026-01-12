package sv.gob.cementerios.cementeriosle.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Reemplace los @Autowired de las líneas 22 y 25 por esto:

    private final JwtUtil jwtUtil;
    private final UserDetailsService myUserDetailsService;

    // Constructor para la inyección de dependencias
    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService myUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.myUserDetailsService = myUserDetailsService;
    }

    /**
     * Helper para extraer el JWT del encabezado Authorization: Bearer <token>
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Retorna la cadena después de "Bearer "
        }
        return null;
    }

    /**
     * Lógica principal del filtro ejecutada en cada solicitud.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtUtil.validateToken(jwt)) {

                // 1. Extracción y carga estándar del UserDetails
                String username = jwtUtil.extractUsername(jwt);
                UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

                // 2. Establece la autenticación en el contexto de Spring Security
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // ==========================================================
                // ⭐ 3. EXPOSICIÓN DE CLAIMS DE NEGOCIO PARA EL CONTROLADOR ⭐
                // ==========================================================
                // El filtro lee el ID y el Rol del token y lo adjunta al Request
                // para que el CementerioController lo pueda leer con @RequestAttribute.
                request.setAttribute("usuarioId", jwtUtil.extractUserId(jwt));
                request.setAttribute("rol", jwtUtil.extractRol(jwt));
            }
        } catch (Exception ex) {
            // Se registra el error, pero el filtro debe continuar para permitir
            // que las solicitudes no protegidas sigan su curso normal.
            logger.error("No se pudo establecer la autenticación del usuario. Token inválido, expirado o error al cargar el usuario.", ex);
        }

        // Continúa la cadena de filtros
        filterChain.doFilter(request, response);
    }
}