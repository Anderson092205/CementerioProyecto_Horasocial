import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { LoteService } from '../../services/lote.service';

@Component({
  selector: 'app-mapa-parcela',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mapa-parcela.component.html',
  styleUrls: ['./mapa-parcela.component.css']
})
export class MapaParcelaComponent implements OnInit {

  idCementerio!: number;
  numParcela!: number;

  lotes: any[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private loteService: LoteService
  ) {}

  ngOnInit(): void {
    this.idCementerio = Number(this.route.snapshot.paramMap.get('id'));
    this.numParcela = Number(this.route.snapshot.paramMap.get('num'));

    this.cargarLotes();
  }

  cargarLotes() {
    this.loteService
      .getLotesPorParcela(this.idCementerio, this.numParcela)
      .subscribe(data => {
        this.lotes = data;
      });
  }

  abrirLote(lote: any) {
    if (lote.entierros >= 4) return; // 🛑 BLOQUEO

    this.router.navigate(['lote', lote.id_lote]);
  }
}

