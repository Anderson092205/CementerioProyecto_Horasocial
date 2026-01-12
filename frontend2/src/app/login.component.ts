import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService, AuthResponse } from './auth.service'; 
import { FormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common'; 
import { HttpClientModule } from '@angular/common/http'; 
import { catchError } from 'rxjs/operators'; // Necesario si usa throwError en el login

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule, HttpClientModule], 
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  // Variables del formulario
  correo = '';
  password = '';
  errorMessage: string = '';
  rememberMe: boolean = false; 
  cargando: boolean = false;

  // Constantes para localStorage
  private readonly REMEMBER_KEY = 'remember_username';
  private readonly REMEMBER_ME_STATE_KEY = 'remember_me_state'; 

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    // 1. Redirección si ya está logeado
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/cementerios']); 
    }

    // 2. LÓGICA DE RECORDAR: Cargar el estado de la casilla
    const savedRememberState = localStorage.getItem(this.REMEMBER_ME_STATE_KEY);
    this.rememberMe = savedRememberState === 'true'; 

    // 3. LÓGICA DE RECORDAR: Cargar el nombre de usuario SOLO si 'rememberMe' es true
    if (this.rememberMe) {
      const savedUsername = localStorage.getItem(this.REMEMBER_KEY);
      if (savedUsername) {
        this.correo = savedUsername;
      } else {
        this.rememberMe = false;
      }
    }
  }
  
    // Mantiene la función de cambio para guardar el estado del checkbox
    onRememberMeChange(): void {
        localStorage.setItem(this.REMEMBER_ME_STATE_KEY, this.rememberMe ? 'true' : 'false');
        // Si se desmarca, limpiamos el campo de correo para mejor UX
        if (!this.rememberMe) {
            this.correo = '';
            localStorage.removeItem(this.REMEMBER_KEY);
        }
    }

  onLogin(): void {
    this.errorMessage = ''; 
    this.cargando = true;

    // ⭐ LÓGICA DE GUARDADO FINAL antes de llamar a la API
    if (this.rememberMe) {
        // Guardar el estado de la casilla Y el correo actual
        localStorage.setItem(this.REMEMBER_ME_STATE_KEY, 'true');
        localStorage.setItem(this.REMEMBER_KEY, this.correo.trim());
    } else {
        // Si no está marcado, asegurar que se elimine el correo
        localStorage.setItem(this.REMEMBER_ME_STATE_KEY, 'false');
        localStorage.removeItem(this.REMEMBER_KEY);
    }

    // 5. Llamada al servicio de autenticación
    this.authService.login(this.correo, this.password).subscribe({
      next: (response: AuthResponse) => {
        // Usa el método setAuthData del servicio para guardar el token, ID y Rol
        this.authService.setAuthData(response.token, response.id, response.rol);
        this.cargando = false;
        
        this.router.navigate(['/cementerios']); 
      },
      error: (err) => {
        this.errorMessage = 'Credenciales no válidas o el servidor no responde.';
        console.error('Error de autenticación:', err);
        this.cargando = false;
      }
    });
  }
}