import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LanguageService {

  private languageSource = new BehaviorSubject<string>('es'); // Idioma por defecto
  currentLang$ = this.languageSource.asObservable();

  // Traducciones para todo el sitio
  private translations: any = {
    es: {
      soporte: "SOPORTE TÉCNICO ",
      idioma: "ESPAÑOL - INTERNACIONAL",
      bienvenida: "Bienvenidos a Cementerios La Libertad Este",
      ir: "Ir a...",
      logout: "Cerrar Sesión",
      cargando: "Cargando cementerios...",
      noResultados: "No se encontraron cementerios disponibles."
    },
    en: {
      soporte: "TECHNICAL SUPPORT",
      idioma: "ENGLISH - INTERNATIONAL (EN)",
      bienvenida: "Welcome to La Libertad Este Cemeteries",
      ir: "Go to...",
      logout: "Logout",
      cargando: "Loading cemeteries...",
      noResultados: "No cemeteries available."
    }
  };

  setLanguage(lang: 'es' | 'en') {
    this.languageSource.next(lang);
  }

  translate(key: string): string {
    const lang = this.languageSource.value;
    return this.translations[lang][key] || key;
  }
}

