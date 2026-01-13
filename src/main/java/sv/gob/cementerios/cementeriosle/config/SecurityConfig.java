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
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        // Se agregó PATCH para el cambio de clave
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/usuarios/actualizar-password-temporal").permitAll()

                        // Solo Informática crea usuarios
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/crear").hasRole("INFORMATICA")

                                // 1. CONSULTA y los demás pueden ver (GET) todos los cementerios
                                .requestMatchers(HttpMethod.GET, "/api/v1/cementerios/**")
                                .hasAnyRole("ADMIN", "INFORMATICA", "CONSULTA", "OPERADOR")

                                // 2. Solo ADMIN e INFORMATICA pueden Crear, Editar o Borrar (POST, PUT, DELETE)
                                .requestMatchers(HttpMethod.POST, "/api/v1/cementerios/**").hasAnyRole("ADMIN", "INFORMATICA")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/cementerios/**").hasAnyRole("ADMIN", "INFORMATICA")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/cementerios/**").hasAnyRole("ADMIN", "INFORMATICA")


                        .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}