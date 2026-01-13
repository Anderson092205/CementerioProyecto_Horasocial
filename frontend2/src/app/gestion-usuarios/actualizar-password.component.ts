import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UsuarioService } from '../services/usuario.service';

@Component({
  selector: 'app-actualizar-password',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './actualizar-password.component.html',
  // CORRECCIÓN: Se cambió './' por '../' para encontrar el archivo fuera de la carpeta actual
  styleUrls: ['../login.component.css'] 
})
export class ActualizarPasswordComponent implements OnInit {
  correo: string = '';
  nuevaPass: string = '';
  confirmarPass: string = '';
  errorMessage: string = '';

  constructor(private router: Router, private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    const email = sessionStorage.getItem('correo_para_cambio');
    if (!email) {
      this.router.navigate(['/login']);
    } else {
      this.correo = email;
    }
  }

  onActualizar(): void {
    if (this.nuevaPass !== this.confirmarPass) {
      this.errorMessage = 'Las contraseñas no coinciden.';
      return;
    }

    this.usuarioService.actualizarPasswordTemporal(this.correo, this.nuevaPass).subscribe({
      next: () => {
        alert('¡Contraseña actualizada! Por favor, ingresa con tus nuevas credenciales.');
        sessionStorage.removeItem('correo_para_cambio');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.errorMessage = 'No se pudo actualizar la contraseña. Intente más tarde.';
      }
    });
  }
}