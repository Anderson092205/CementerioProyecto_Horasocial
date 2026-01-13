import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ParcelaService } from '../../services/parcela.service';

@Component({
  selector: 'app-lista-parcelas',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lista-parcela.component.html',
  styleUrls: ['./lista-parcela.component.css']
})
export class ListaParcelaComponent implements OnInit {

  idCementerio!: number;
  parcelas: any[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private parcelaService: ParcelaService
  ) {}

  ngOnInit(): void {
    this.idCementerio = Number(this.route.snapshot.paramMap.get('id'));
    this.cargarParcelas();
  }

  cargarParcelas() {
    this.parcelaService
      .getParcelasPorCementerio(this.idCementerio)
      .subscribe(data => {
        this.parcelas = data;
      });
  }

  abrirParcela(parcela: any) {
    this.router.navigate([
      'cementerio',
      this.idCementerio,
      'parcela',
      parcela.numero_parcela
    ]);
  }
}
