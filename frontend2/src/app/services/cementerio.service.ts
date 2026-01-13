import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'; 
import { Observable } from 'rxjs';
import { Cementerio } from '../models/cementerio.interface'; 
import { CementerioDetalle } from '../models/cementerio-detalle.interface'; 

const API_URL = 'http://localhost:8080/api/v1/cementerios'; 

@Injectable({
  providedIn: 'root'
})
export class CementerioService {

  constructor(private http: HttpClient) { }

  // --- NUEVO MÉTODO PARA EL DESPLEGABLE ---
  getListaCompleta(): Observable<any[]> {
    return this.http.get<any[]>(`${API_URL}/lista-completa`);
  }

  // FIRMA SIMPLIFICADA (Dashboard)
  obtenerCementeriosPorUsuario(): Observable<Cementerio[]> {
    return this.http.get<Cementerio[]>(API_URL);
  }
  
  obtenerDetallePorId(id: number): Observable<CementerioDetalle> {
    const url = `${API_URL}/${id}`;
    return this.http.get<CementerioDetalle>(url);
  }
}