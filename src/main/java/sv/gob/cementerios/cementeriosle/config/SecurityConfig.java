package sv.gob.cementerios.cementeriosle.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import sv.gob.cementerios.cementeriosle.security.JwtAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Inyectamos el filtro. Spring lo encuentra porque tiene @Component en su clase.
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // Define que BCrypt se usará para codificar/verificar contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Configuración CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Asegúrate de que esta URL sea la correcta para tu proyecto Angular
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilita CSRF para APIs REST
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Aplica la configuración CORS

                .sessionManagement(session -> session
                        // Spring Security no creará ni usará sesiones HTTP (es Stateless, para JWT)
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Configuración de Autorización
                .authorizeHttpRequests(auth -> auth
                        // Ruta de Login (Auth) es pública
                        .requestMatchers("/api/auth/login").permitAll()

                        // esto es para dar acceso a informatica a solo crear Usuarios
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/**").hasRole("INFORMATICA")
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/**").hasRole("INFORMATICA")

                        // para que ambos puedan visualizar lo mismo

                        .requestMatchers("/api/v1/cementerios/**").hasAnyRole("INFORMATICA", "ADMIN")
                        .anyRequest().authenticated()
                );

        // INSERCIÓN DEL FILTRO JWT
        // Se añade antes del filtro estándar de autenticación de usuario y contraseña
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}