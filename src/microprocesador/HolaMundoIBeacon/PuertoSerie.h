// --------------------------------------------------------------
// Fichero: PuertoSerie.h
// Descripción: Clase de apoyo para la comunicación por puerto serie y la salida de mensajes
//              de depuración.
// Copyright: material académico y modificaciones del autor.
// Fecha de adaptación: 05-10-2026
// Autora de la adaptación: Judith Civico Aporta
// Autor original: Jordi Bataller i Mascarell en fecha 2019-07-07
// Aportación: integración del puerto serie en las pruebas y trazas del Sprint 0.
// --------------------------------------------------------------
// -*- mode: c++ -*-


#ifndef PUERTO_SERIE_H_INCLUIDO
#define PUERTO_SERIE_H_INCLUIDO

// ------------------------------------------------------------
// Clase PuertoSerie: simplifica la escritura de mensajes de depuración.
// ------------------------------------------------------------
class PuertoSerie  {

public:
  // ------------------------------------------------------------
  // PuertoSerie()
// baudios: N --> PuertoSerie() --> puerto: PuertoSerie
//
  // Inicializa el puerto serie con la velocidad en baudios indicada.
  // ------------------------------------------------------------
  PuertoSerie (long baudios) {
	Serial.begin( baudios );
	// mejor no poner esto aquí: while ( !Serial ) delay(10);   
  } // ()

  // ------------------------------------------------------------
  // esperarDisponible()
// --> esperarDisponible() -->
//
  // Espera hasta tres segundos a que el puerto serie esté disponible.
  // ------------------------------------------------------------
  void esperarDisponible() {

    unsigned long inicio = millis();

	while ( !Serial && (millis() - inicio < 3000) ) {
	  delay(10);   
	}

  } // ()

  // ------------------------------------------------------------
  // escribir()
// mensaje: T --> escribir() -->
//
  // Escribe por el puerto serie un mensaje de cualquier tipo compatible con Serial.print().
  // ------------------------------------------------------------
  template<typename T>
  void escribir (T mensaje) {
	Serial.print( mensaje );
  } // ()
  
}; // class PuertoSerie

#endif
