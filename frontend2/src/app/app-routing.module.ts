import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './login.component';
import { CementeriosComponent } from './dashboard/cementerios/cementerios.component';
import { DetalleCementerioComponent } from './dashboard/detalle-cementerio/detalle-cementerio.component'; 

import { authGuard } from './auth.guard'; // 🚨 1. IMPORTAR EL GUARD

const routes: Routes = [
  // 1. Ruta de Autenticación (Pública)
  { path: 'login', component: LoginComponent },

  // 2. Ruta del Listado General (PROTEGIDA)
  { 
    path: 'cementerios', 
    component: CementeriosComponent,
    canActivate: [authGuard] // 🚨 2. APLICAR EL GUARD
  }, 
  
  // 3. Ruta del Detalle con Parámetro ID (PROTEGIDA)
  { 
    path: 'detalle-cementerio/:id', 
    component: DetalleCementerioComponent,
    canActivate: [authGuard] // 🚨 2. APLICAR EL GUARD
  }, 

  // 4. Redirección Inicial y Wildcard
  { path: '', redirectTo: '/cementerios', pathMatch: 'full' }, 
  { path: '**', redirectTo: '/cementerios' } 
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }