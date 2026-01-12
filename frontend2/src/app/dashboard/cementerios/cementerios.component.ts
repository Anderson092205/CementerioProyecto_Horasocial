import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; 
import { HttpClientModule } from '@angular/common/http';
import { RouterModule, Router } from '@angular/router'; 
import { FormsModule } from '@angular/forms';

import { CementerioService } from '../../services/cementerio.service'; 
import { Cementerio } from '../../models/cementerio.interface'; 
import { AuthService } from '../../auth.service';
import { LanguageService } from '../../services/language.service'; 

@Component({
  selector: 'app-cementerios',
  standalone: true,
  imports: [
    CommonModule, 
    HttpClientModule,
    RouterModule,
    FormsModule
  ],
  templateUrl: './cementerios.component.html',
  styleUrls: ['./cementerios.component.css']
})
export class CementeriosComponent implements OnInit {

  cementerios: Cementerio[] = []; 
  cargando: boolean = true;
  errorCarga: string | null = null;
  
  // Variables de control y estado
  lang = 'es'; 
  isSidebarOpen = false; 
  userRol: string | null = null; 

  constructor(
    private cementerioService: CementerioService,
    private authService: AuthService,
    private router: Router,
    public languageService: LanguageService
  ) { }

  ngOnInit(): void {
    // 1. Suscripción al idioma
    this.languageService.currentLang$.subscribe(l => this.lang = l);
    
    // 2. Obtener el rol del usuario para el menú lateral
    this.userRol = this.authService.getUserRole(); 
    
    // 3. Carga de datos desde el servicio
    this.cargarCementeriosFiltrados();
  }

  toggleSidebar(): void {
    this.isSidebarOpen = !this.isSidebarOpen;
  }

  cargarCementeriosFiltrados(): void {
    this.cargando = true;
    this.cementerioService.obtenerCementeriosPorUsuario().subscribe({
      next: (data) => {
        this.cementerios = data;
        this.cargando = false;
        this.errorCarga = null;
      },
      error: (err) => {
        console.error('Error al cargar cementerios:', err);
        this.errorCarga = err.status === 403 
          ? 'No tiene permisos para ver esta lista.' 
          : 'Error al cargar la lista de cementerios.';
        this.cargando = false;
      }
    });
  }
  
  cerrarSesion(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  cambiarIdioma(): void {
    const nuevo = this.lang === 'es' ? 'en' : 'es';
    this.languageService.setLanguage(nuevo);
  }

  // Helper para clases de bordes (opcional si lo usas en el HTML)
  getCardClass(tipo: string): string {
    if (tipo?.toLowerCase().includes('general')) return 'border-blue-500';
    if (tipo?.toLowerCase().includes('jardin')) return 'border-green-500';
    return 'border-gray-300';
  }

  t(key: string) {
    return this.languageService.translate(key);
  }
}