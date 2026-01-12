import { Routes } from '@angular/router';
import { LoginComponent } from './login.component';
import { CementeriosComponent } from './dashboard/cementerios/cementerios.component';
import { DetalleCementerioComponent } from './dashboard/detalle-cementerio/detalle-cementerio.component';

// Importación del Guard que protege las rutas
import { authGuard } from './auth.guard'; 
import { CrearUsuarioComponent } from './gestion-usuarios/crear-usuario.component';

export const routes: Routes = [
  
  // 1. Ruta de Autenticación (Pública)
  { path: 'login', component: LoginComponent },
  
  // 2. Ruta del Listado (PROTEGIDA)
  { 
    path: 'cementerios', 
    component: CementeriosComponent,
    canActivate: [authGuard] // Aplicamos el Guard de autenticación
  },
  
  // 3. Ruta del Detalle (PROTEGIDA Y CON PARÁMETRO)
  { 
    path: 'detalle-cementerio/:id', 
    component: DetalleCementerioComponent,
    canActivate: [authGuard] // Aplicamos el Guard de autenticación
  },

  { 
    path: 'usuarios/crear', 
    component: CrearUsuarioComponent,
    canActivate: [authGuard],
    // Opcional: Si tu guard maneja roles, puedes pasarle el dato aquí:
    data: { role: 'INFORMATICA' } 
  },
  
  // 4. Redirección Inicial y Wildcard
  // La ruta vacía redirige a la ruta protegida, forzando la verificación del Guard.
  { path: '', redirectTo: '/cementerios', pathMatch: 'full' }, 
  
  // Cualquier otra URL no definida también redirige a la ruta protegida.
  { path: '**', redirectTo: '/cementerios' } 
];