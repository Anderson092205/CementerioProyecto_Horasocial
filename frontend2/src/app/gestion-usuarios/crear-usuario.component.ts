import { Component, OnInit } from '@angular/core';
import { UsuarioService } from '../services/usuario.service';
import { CementerioService } from '../services/cementerio.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-crear-usuario',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './crear-usuario.component.html',
  styleUrls: ['./crear-usuario.component.css']
})
export class CrearUsuarioComponent implements OnInit {
  
  // Modelo que se envía al Backend
  nuevoUsuario = {
    nombre: '',
    correo: '',
    telefono: '',
    contrasena: '', 
    idRol: 2,           // Por defecto Operador
    idCementerios: [] as number[]  // Cambio a arreglo de números para checkboxes
  };

  listaCementerios: any[] = []; 
  mensajeExito: string | null = null;
  mensajeError: string | null = null;

  constructor(
    private usuarioService: UsuarioService,
    private cementerioService: CementerioService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const rol = this.authService.getUserRole();
    if (rol !== 'INFORMATICA') {
      this.router.navigate(['/cementerios']);
    }
    this.cargarCementerios();
  }

  cargarCementerios(): void {
    this.cementerioService.getListaCompleta().subscribe({
      next: (data: any[]) => { // Tipado para corregir error de imagen
        this.listaCementerios = data;
      },
      error: (err: any) => {
        console.error('Error al cargar la lista de cementerios', err);
        this.mensajeError = 'No se pudo cargar la lista de cementerios.';
      }
    });
  }

  // Maneja la selección de múltiples cementerios
  onCheckboxChange(event: any, id: number): void {
    const isChecked = (event.target as HTMLInputElement).checked;

    if (isChecked) {
      // Agregar el ID si se marca el checkbox
      this.nuevoUsuario.idCementerios.push(id);
    } else {
      // Eliminar el ID si se desmarca
      this.nuevoUsuario.idCementerios = this.nuevoUsuario.idCementerios.filter(item => item !== id);
    }
  }

  aplicarMascara(event: any): void {
    let valor = event.target.value.replace(/\D/g, '');
    if (valor.length > 4) {
      valor = valor.substring(0, 4) + '-' + valor.substring(4, 8);
    }
    this.nuevoUsuario.telefono = valor;
    event.target.value = valor;
  }

  generarPassword(): void {
    const caracteres = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*';
    let pass = '';
    for (let i = 0; i < 10; i++) {
      pass += caracteres.charAt(Math.floor(Math.random() * caracteres.length));
    }
    this.nuevoUsuario.contrasena = pass;
    this.mensajeError = null;
  }

  copiarPassword(): void {
    if (this.nuevoUsuario.contrasena) {
      navigator.clipboard.writeText(this.nuevoUsuario.contrasena);
    }
  }

  onSubmit(): void {
    this.mensajeExito = null;
    this.mensajeError = null;

    // Validación extra: Si no es ADMIN (supongamos ID 1), debe elegir al menos un cementerio
    if (this.nuevoUsuario.idRol != 1 && this.nuevoUsuario.idCementerios.length === 0) {
      this.mensajeError = 'Debe seleccionar al menos un cementerio para el operador.';
      return;
    }

    this.usuarioService.registrar(this.nuevoUsuario).subscribe({
      next: (res) => {
        this.mensajeExito = '¡Usuario registrado correctamente!';
        setTimeout(() => this.router.navigate(['/cementerios']), 2500);
      },
      error: (err: any) => {
        this.mensajeError = err.error?.message || 'Error: El correo electrónico ya está registrado.';
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/cementerios']);
  }
}