import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

// Claves de almacenamiento
const TOKEN_KEY = 'jwt_token';
const USER_ID_KEY = 'user_id';
const USER_ROLE_KEY = 'user_rol';

/**
 * Interfaz esperada para el login.
 * Se agregó 'esTemporal' para manejar el flujo de cambio de contraseña obligatorio.
 */
export interface AuthResponse {
    token: string;
    id: number;         // Mapea a idUsuario
    rol: string;        // Mapea al nombre del rol (e.g., "ADMIN", "CONSULTA")
    esTemporal: boolean; // ⭐ CORRECCIÓN: Evita el error "la propiedad no existe"
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    // URL base para el microservicio de autenticación
    private baseUrl = 'http://localhost:8080/api/auth'; 
    
    constructor(private http: HttpClient, private router: Router) { }

    /**
     * Envía las credenciales al backend para autenticación.
     */
    login(correo: string, password: string): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.baseUrl}/login`, { correo, password });
    }

    /**
     * Guarda los datos de sesión en sessionStorage.
     * Los datos persisten mientras la pestaña del navegador esté abierta.
     */
    setAuthData(token: string, userId: number, role: string): void {
        sessionStorage.setItem(TOKEN_KEY, token);
        sessionStorage.setItem(USER_ID_KEY, userId.toString());
        sessionStorage.setItem(USER_ROLE_KEY, role);
    }

    /**
     * Recupera el ID del usuario actual.
     */
    getUserId(): number | null {
        const id = sessionStorage.getItem(USER_ID_KEY);
        return id ? parseInt(id, 10) : null;
    }

    /**
     * Recupera el rol del usuario actual.
     */
    getUserRole(): string | null {
        return sessionStorage.getItem(USER_ROLE_KEY);
    }
    
    /**
     * Recupera el Token JWT.
     */
    getToken(): string | null {
        return sessionStorage.getItem(TOKEN_KEY);
    }

    /**
     * Verifica si existe una sesión activa basándose en la presencia del token.
     */
    isLoggedIn(): boolean {
        return !!this.getToken();
    }

    /**
     * Limpia todos los datos de sesión y redirige al usuario al Login.
     */
    logout(): void {
        // Limpiar datos de autenticación
        sessionStorage.removeItem(TOKEN_KEY);
        sessionStorage.removeItem(USER_ID_KEY);
        sessionStorage.removeItem(USER_ROLE_KEY);
        
        // Limpiar rastro del correo usado para el flujo puente
        sessionStorage.removeItem('correo_para_cambio'); 

        this.router.navigate(['/login']);
    }
}