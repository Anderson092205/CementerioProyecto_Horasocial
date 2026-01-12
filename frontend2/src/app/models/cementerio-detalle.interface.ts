import { Cementerio } from './cementerio.interface';

// Define la estructura de Difunto que viene del backend
export interface Difunto {
  idDifunto: number;
  nombre: string;
  fechaDefuncion: string; // Usar string para fechas es más seguro en Angular
  ubicacion: string; // Lote/Sección/Fila/Columna
}

// Define la estructura de Propietario
export interface Propietario {
  idPropietario: number;
  nombre: string;
  telefono: string;
  correo: string;
  totalLotes: number;
}

// Estructura consolidada del detalle del cementerio (Extiende de la base Cementerio)
export interface CementerioDetalle extends Cementerio {
  
  // Resumen de Espacios
  totalEspacios: number;
  espaciosOcupados: number;
  espaciosDisponibles: number;

  // Listas de detalle
  difuntos: Difunto[];
  propietarios: Propietario[];
}