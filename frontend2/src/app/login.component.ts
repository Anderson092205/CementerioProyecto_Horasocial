import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService, AuthResponse } from './auth.service'; 
import { FormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common'; 
import { HttpClientModule } from '@angular/common/http'; 

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
  
  onRememberMeChange(): void {
    localStorage.setItem(this.REMEMBER_ME_STATE_KEY, this.rememberMe ? 'true' : 'false');
    if (!this.rememberMe) {
      this.correo = '';
      localStorage.removeItem(this.REMEMBER_KEY);
    }
  }

  onLogin(): void {
    this.errorMessage = ''; 
    this.cargando = true;

    // Lógica de guardado de "Recordar Usuario"
    if (this.rememberMe) {
      localStorage.setItem(this.REMEMBER_ME_STATE_KEY, 'true');
      localStorage.setItem(this.REMEMBER_KEY, this.correo.trim());
    } else {
      localStorage.setItem(this.REMEMBER_ME_STATE_KEY, 'false');
      localStorage.removeItem(this.REMEMBER_KEY);
    }

    // Llamada al servicio de autenticación
    this.authService.login(this.correo, this.password).subscribe({
      next: (response: AuthResponse) => {
        this.cargando = false;
        
        // ⭐ LÓGICA DE FLUJO PUENTE:
        // Verificamos si la contraseña es temporal antes de dejarlo entrar al dashboard
        if (response.esTemporal) {
          // Guardamos el correo en sessionStorage para que el componente de actualización lo use
          sessionStorage.setItem('correo_para_cambio', this.correo.trim());
          
          // Redirigimos a la pantalla de cambio de clave (Ruta pública)
          this.router.navigate(['/actualizar-password']);
        } else {
          // Flujo normal: Si la clave NO es temporal, guardamos sesión y entramos
          this.authService.setAuthData(response.token, response.id, response.rol);
          this.router.navigate(['/cementerios']); 
        }
      },
      error: (err) => {
        // Manejo de error 401 (Credenciales inválidas)
        this.errorMessage = 'Credenciales no válidas o el servidor no responde.';
        console.error('Error de autenticación:', err);
        this.cargando = false;
      }
    });
  }
}