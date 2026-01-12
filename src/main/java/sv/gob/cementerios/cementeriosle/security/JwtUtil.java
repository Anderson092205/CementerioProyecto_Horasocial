package sv.gob.cementerios.cementeriosle.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // 🚨 ATENCIÓN: Esta clave debe estar definida en application.properties
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // Genera la clave de firma
    private Key getSigningKey() {
        // Asegura que la clave sea lo suficientemente larga y segura (más de 256 bits)
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // ================= GENERACIÓN DEL TOKEN =================

    public String generateTokenWithClaims(
            UserDetails userDetails,
            Integer idUsuario,
            String rol) {

        Map<String, Object> claims = new HashMap<>();

        // ⭐ CLAIMS PERSONALIZADOS DE NEGOCIO ⭐
        claims.put("userId", idUsuario);
        claims.put("rol", rol);

        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // Correo del usuario
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // Token expira en 10 horas
                // 1000 milisegundos * 60 segundos = 60,000 milisegundos (1 minuto)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 5))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ================= VALIDACIÓN Y EXTRACCIÓN =================

    // Extrae todos los claims
    private Claims extractAllClaims(String token) {
        // Usa getSigningKey() para validar la firma
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 1. Extrae el nombre de usuario (el 'subject')
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 2. Valida si el token es válido
    public boolean validateToken(String token) {
        try {
            // Intenta parsear y validar la firma/expiración
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // El token no es válido por expiración, firma incorrecta, etc.
            System.err.println("JWT validation failed: " + e.getMessage());
            return false;
        }
    }
    // ================= EXTRACTORES DE CLAIMS PERSONALIZADOS =================

    // Extrae el ID del usuario del token
    public Integer extractUserId(String token) {
        // Busca el claim "userId" y lo resuelve como Integer
        return extractClaim(token, claims -> claims.get("userId", Integer.class));
    }

    // Extrae el rol del usuario del token
    public String extractRol(String token) {
        // Busca el claim "rol" y lo resuelve como String
        return extractClaim(token, claims -> claims.get("rol", String.class));
    }
}