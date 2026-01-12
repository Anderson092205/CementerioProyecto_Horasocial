import { Component, OnInit, Injectable } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { catchError, of, Observable } from 'rxjs';

export interface CementerioDetalle {
    id: number;
    nombre: string;
    ubicacion: string;
    tipo: string;
    datosTabla: string[];
}

@Injectable({ providedIn: 'root' })
export class CementerioService {
    constructor(private http: HttpClient) {}

    // ⭐ ESTE MÉTODO SOLO ES MOCK, NO LO CAMBIÉ
    obtenerDetallePorId(id: number): Observable<CementerioDetalle> {
        const mockData: CementerioDetalle = {
            id: id,
            nombre: `Cementerio Histórico Nº ${id}`,
            ubicacion: 'Avenida Siempre Viva 742',
            tipo: (id % 2 === 0) ? 'General' : 'Jardín',
            datosTabla: Array(10).fill(0).map((_, i) => `Metadato ${i + 1}`)
        };

        return of(mockData);
    }
}

@Component({
  selector: 'app-detalle-cementerio',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './detalle-cementerio.component.html',
  styleUrls: ['./detalle-cementerio.component.css']
})
export class DetalleCementerioComponent implements OnInit {

  columnas: string[] = [];   // 🔥 Inicializa vacío (lo llenamos dinámicamente)

  cementerioId: number | null = null;
  cementerioDetalle: CementerioDetalle | null = null;

  cargando = true;
  errorCarga: string | null = null;

  /** Vista dinámica */
  vista: 'lista' | 'parcela' | 'documentos' | 'pagos' = 'lista';
  parcelaSeleccionada: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private cementerioService: CementerioService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (idParam) {
      this.cementerioId = +idParam;
      this.cargarDetalleCementerio(this.cementerioId);
    } else {
      this.errorCarga = "ID de cementerio no encontrado.";
      this.cargando = false;
    }
  }

  cargarDetalleCementerio(id: number): void {
    this.cementerioService.obtenerDetallePorId(id)
      .pipe(
        catchError(err => {
          this.errorCarga = `No se pudo cargar el detalle del cementerio ID ${id}.`;
          this.cargando = false;
          return of(null);
        })
      )
      .subscribe(data => {

        if (!data) return;

        this.cementerioDetalle = data;

        // ⭐ NOMBRE EXACTO DEL CEMENTERIO PRIVADO
        const nombrePrivado = "Cementerio Privado La Libertad";

        // ⭐ Si el nombre del cementerio es EXACTO → solo 2 filas
        if (data.nombre.trim().toLowerCase() === nombrePrivado.toLowerCase()) {
          this.columnas = ["Parcela 1", "Parcela 2"];
        } 
        // ⭐ Caso contrario → 10 filas
        else {
          this.columnas = Array.from({ length: 10 }, (_, i) => `Parcela ${i + 1}`);
        }

        this.cargando = false;
      });
  }

  abrirParcela(num: number) {
    this.parcelaSeleccionada = num;
    this.vista = 'parcela';
  }

  abrirDocumentos(num: number) {
    this.parcelaSeleccionada = num;
    this.vista = 'documentos';
  }

  abrirPagos(num: number) {
    this.parcelaSeleccionada = num;
    this.vista = 'pagos';
  }

  regresarLista() {
    this.vista = 'lista';
    this.parcelaSeleccionada = null;
  }

  goBack() {
    this.router.navigate(['/cementerios']);
  }
}









