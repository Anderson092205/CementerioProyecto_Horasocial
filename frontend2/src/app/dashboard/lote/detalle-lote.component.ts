import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LoteService } from '../../services/lote.service';

@Component({
  selector: 'app-detalle-lote',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './detalle-lote.component.html',
  styleUrls: ['./detalle-lote.component.css']
})
export class DetalleLoteComponent implements OnInit {

  idLote!: number;
  entierros: any[] = [];

  nuevo = {
    nombre: '',
    fecha_defuncion: ''
  };

  constructor(
    private route: ActivatedRoute,
    private loteService: LoteService
  ) {}

  ngOnInit(): void {
    this.idLote = Number(this.route.snapshot.paramMap.get('id'));
    this.cargarEntierros();
  }

  cargarEntierros() {
    this.loteService
      .getEntierrosPorLote(this.idLote)
      .subscribe(data => this.entierros = data);
  }

  guardar() {
    if (this.entierros.length >= 4) return;

    const payload = {
      ...this.nuevo,
      id_lote: this.idLote
    };

    this.loteService.registrarEntierro(payload).subscribe(() => {
      this.nuevo = { nombre: '', fecha_defuncion: '' };
      this.cargarEntierros();
    });
  }

  get loteLleno(): boolean {
    return this.entierros.length >= 4;
  }
}

