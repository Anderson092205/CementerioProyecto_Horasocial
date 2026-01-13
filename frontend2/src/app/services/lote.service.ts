import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoteService {

  private apiUrl = 'http://localhost:8080/api'; // ajusta si cambia

  constructor(private http: HttpClient) {}

  // 🔹 Lotes por parcela
  getLotesPorParcela(idCementerio: number, numParcela: number): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.apiUrl}/cementerios/${idCementerio}/parcelas/${numParcela}/lotes`
    );
  }

  // 🔹 Entierros por lote
  getEntierrosPorLote(idLote: number): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.apiUrl}/lotes/${idLote}/entierros`
    );
  }

  // 🔹 Registrar entierro
  registrarEntierro(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/entierros`, data);
  }
}

