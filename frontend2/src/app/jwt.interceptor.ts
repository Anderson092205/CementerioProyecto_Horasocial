import { HttpInterceptorFn, HttpErrorResponse } from "@angular/common/http";
import { inject } from "@angular/core";
import { AuthService } from "./auth.service"; // Asume que su servicio de auth se llama así
import { catchError, throwError } from 'rxjs';
import { Router } from "@angular/router";

/**
 * Interceptor para adjuntar el Token JWT en el encabezado de las peticiones
 * y manejar globalmente los errores de autorización (401).
 */
export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
    
    const authService = inject(AuthService);
    const router = inject(Router);
    const token = authService.getToken(); 

    // 1. Lógica de Exclusión: No adjuntar el token a las rutas de login
    if (req.url.includes('auth') || req.url.includes('login')) {
        return next(req);
    }
    
    let clonedReq = req;

    // 2. Adjuntar el Token JWT
    if (token) {
        clonedReq = req.clone({
            setHeaders: {
                // Añade el encabezado estándar: Authorization: Bearer <token>
                Authorization: `Bearer ${token}` 
            }
        });
    }

    // 3. Manejo Global de Errores (401)
    return next(clonedReq).pipe(
      catchError((error: HttpErrorResponse) => {
        
        // Verificar si es un error de autorización (token expirado o inválido)
        if (error.status === 401 || error.status === 403) {
            
            console.error('Sesión no válida o caducada. Redirigiendo a login.');
            
            //  ACCIÓN CLAVE: Forzar el cierre de sesión y redirigir
            authService.logout();
            
            // Redirige al login para forzar una nueva autenticación
            router.navigate(['/login']);
            
            // Detenemos la propagación del error para que el componente no lo maneje
            return throwError(() => new Error('Sesión expirada. Redirigiendo a login.'));
        }
        
        // Para cualquier otro error (500, 404, etc.), simplemente propagarlo.
        return throwError(() => error);
      })
    );
};