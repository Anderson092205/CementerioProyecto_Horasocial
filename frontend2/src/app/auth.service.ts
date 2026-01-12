import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

// Claves de almacenamiento
const TOKEN_KEY = 'jwt_token';
const USER_ID_KEY = 'user_id';
const USER_ROLE_KEY = 'user_rol';

// Interfaz esperada para el login
export interface AuthResponse {
    token: string;
    id: number; // Mapea a idUsuario
    rol: string; // Mapea al nombre del rol (e.g., "ADMIN", "CONSULTA")
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // AJUSTA esta URL si es necesario.
  private baseUrl = 'http://localhost:8080/api/auth'; 
  
  constructor(private http: HttpClient, private router: Router) { }

  // Llama al endpoint de login
  login(correo: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, { correo, password });
  }

  /**
   * ⭐ CAMBIO CLAVE: Usa sessionStorage en lugar de localStorage.
   * Los datos se eliminarán automáticamente al cerrar la pestaña o el navegador.
   */
  setAuthData(token: string, userId: number, role: string): void {
    // Utilizamos sessionStorage para una sesión temporal
    sessionStorage.setItem(TOKEN_KEY, token);
    sessionStorage.setItem(USER_ID_KEY, userId.toString());
    sessionStorage.setItem(USER_ROLE_KEY, role);
  }

  // Obtiene el ID del usuario como número (o null)
  getUserId(): number | null {
    // Leer de sessionStorage
    const id = sessionStorage.getItem(USER_ID_KEY);
    return id ? parseInt(id, 10) : null;
  }

  // Obtiene el Rol del usuario como string (o null)
  getUserRole(): string | null {
    // Leer de sessionStorage
    return sessionStorage.getItem(USER_ROLE_KEY);
  }
  
  getToken(): string | null {
    // Leer de sessionStorage
    return sessionStorage.getItem(TOKEN_KEY);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  // El logout limpia todos los datos y redirige
  logout(): void {
    // Limpiar sessionStorage
    sessionStorage.removeItem(TOKEN_KEY);
    sessionStorage.removeItem(USER_ID_KEY);
    sessionStorage.removeItem(USER_ROLE_KEY);
    

    this.router.navigate(['/login']);
  }
}