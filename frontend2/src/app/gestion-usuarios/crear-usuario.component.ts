import { Component, OnInit } from '@angular/core';
import { UsuarioService } from '../services/usuario.service';
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
  
  nuevoUsuario = {
    nombre: '',
    correo: '',
    telefono: '',
    contrasena: '', 
    idRol: 2 
  };

  mensajeExito: string | null = null;
  mensajeError: string | null = null;

  constructor(
    private usuarioService: UsuarioService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const rol = this.authService.getUserRole();
    if (rol !== 'INFORMATICA') {
      this.router.navigate(['/cementerios']);
    }
  }

  aplicarMascara(event: any) {
    let valor = event.target.value.replace(/\D/g, '');
    if (valor.length > 4) {
      valor = valor.substring(0, 4) + '-' + valor.substring(4, 8);
    }
    this.nuevoUsuario.telefono = valor;
    event.target.value = valor;
  }

  generarPassword() {
    const caracteres = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*';
    let pass = '';
    for (let i = 0; i < 10; i++) {
      pass += caracteres.charAt(Math.floor(Math.random() * caracteres.length));
    }
    this.nuevoUsuario.contrasena = pass;
    this.mensajeError = null;
  }

  copiarPassword() {
    if (this.nuevoUsuario.contrasena) {
      navigator.clipboard.writeText(this.nuevoUsuario.contrasena);
    }
  }

  onSubmit() {
    this.mensajeExito = null;
    this.mensajeError = null;

    this.usuarioService.registrar(this.nuevoUsuario).subscribe({
      next: (res) => {
        this.mensajeExito = '¡Usuario registrado correctamente!';
        setTimeout(() => this.router.navigate(['/cementerios']), 2500);
      },
      error: (err) => {
        this.mensajeError = err.error?.message || 'Error al conectar con el servidor';
      }
    });
  }

  cancelar() {
    this.router.navigate(['/cementerios']);
  }
}