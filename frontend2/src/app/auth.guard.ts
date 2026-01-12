import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service'; // Asumiendo que está en src/app/

/**
 * Guardia de ruta para proteger rutas del dashboard.
 * Redirige a /login si el usuario no está autenticado (no hay token).
 */
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // 1. Verificar si la sesión está activa (si existe el token)
  if (authService.isLoggedIn()) {
    return true; // Permitir el acceso a la ruta
  } else {
    // 2. Si no está logueado, redirigir al login
    console.log('Acceso denegado. Redirigiendo a login...');
    return router.createUrlTree(['/login']); 
  }
};