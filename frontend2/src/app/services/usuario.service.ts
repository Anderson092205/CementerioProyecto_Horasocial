import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private apiUrl = 'http://localhost:8080/api/usuarios';

  constructor(private http: HttpClient) {}

  registrar(usuario: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/crear`, usuario);
  }

  actualizarPasswordTemporal(correo: string, nuevaPassword: string): Observable<any> {
  return this.http.patch(`${this.apiUrl}/actualizar-password-temporal`, {
    correo,
    nuevaPassword
  });
}
}