import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'; 
import { Observable } from 'rxjs';
// Asume que estas interfaces existen en 'models'
import { Cementerio } from '../models/cementerio.interface'; 
import { CementerioDetalle } from '../models/cementerio-detalle.interface'; 

// URL base del backend
const API_URL = 'http://localhost:8080/api/v1/cementerios'; 

@Injectable({
  providedIn: 'root'
})
export class CementerioService {

  constructor(private http: HttpClient) { }

  
  //  FIRMA SIMPLIFICADA: Sin parámetros de seguridad.
  obtenerCementeriosPorUsuario(): Observable<Cementerio[]> {
    return this.http.get<Cementerio[]>(API_URL);
  }
  
  /**
   * Método para obtener el detalle completo de un cementerio
   * @param id El ID del cementerio a buscar
   */
  obtenerDetallePorId(id: number): Observable<CementerioDetalle> {
    const url = `${API_URL}/${id}`;
    return this.http.get<CementerioDetalle>(url);
  }
}