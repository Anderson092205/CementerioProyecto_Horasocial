import { Routes } from '@angular/router';
import { LoginComponent } from './login.component';
import { CementeriosComponent } from './dashboard/cementerios/cementerios.component';
import { DetalleCementerioComponent } from './dashboard/detalle-cementerio/detalle-cementerio.component';
import { authGuard } from './auth.guard'; 
import { CrearUsuarioComponent } from './gestion-usuarios/crear-usuario.component';

// 1. Importa tu nuevo componente desde la carpeta gestión-usuarios
import { ActualizarPasswordComponent } from './gestion-usuarios/actualizar-password.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  
  // 2. RUTA PUENTE (Pública): Para que el usuario cambie su clave temporal
  { path: 'actualizar-password', component: ActualizarPasswordComponent },

  { 
    path: 'cementerios', 
    component: CementeriosComponent,
    canActivate: [authGuard] 
  },
  
  { 
    path: 'detalle-cementerio/:id', 
    component: DetalleCementerioComponent,
    canActivate: [authGuard] 
  },

  { 
    path: 'usuarios/crear', 
    component: CrearUsuarioComponent,
    canActivate: [authGuard]
  },
  
  { path: '', redirectTo: '/login', pathMatch: 'full' }, 
  { path: '**', redirectTo: '/login' } 
];