// -*- mode: c++ -*-


// --------------------------------------------------------------
// Fichero: LED.h
// Descripción: Clase de apoyo para controlar el LED de la placa durante la ejecución del
//              programa.
// Copyright: material académico y modificaciones del autor.
// Fecha de adaptación: 05-10-2026
// Autora de la adaptación: Judith Civico Aporta
// Autor original: Jordi Bataller i Mascarell en fecha 2019-07-07
// Aportación: integración del indicador LED en el funcionamiento del Sprint 0.
// --------------------------------------------------------------
#ifndef LED_H_INCLUIDO
#define LED_H_INCLUIDO

// ------------------------------------------------------------
// esperar()
// tiempo: N --> esperar() -->
//
// Bloquea la ejecución durante el tiempo indicado en milisegundos.
// ------------------------------------------------------------
void esperar (long tiempo) {
  delay (tiempo);
}

// ------------------------------------------------------------
// Clase LED: controla el LED integrado de la placa.
// ------------------------------------------------------------
class LED {
private:
  int numeroLED;
  bool encendido;
public:

  // ------------------------------------------------------------
  // LED()
// numero: N --> LED() --> led: LED
//
  // Configura el pin indicado como salida y deja el LED inicialmente apagado.
  // ------------------------------------------------------------
  LED (int numero)
	: numeroLED (numero), encendido(false)
  {
	pinMode(numeroLED, OUTPUT);
	apagar ();
  }

  // ------------------------------------------------------------
  // encender()
// --> encender() -->
//
  // Enciende el LED y actualiza su estado interno.
  // ------------------------------------------------------------
  void encender () {
	digitalWrite(numeroLED, HIGH); 
	encendido = true;
  }

  // ------------------------------------------------------------
  // apagar()
// --> apagar() -->
//
  // Apaga el LED y actualiza su estado interno.
  // ------------------------------------------------------------
  void apagar () {
	  digitalWrite(numeroLED, LOW);
	  encendido = false;
  }

  // ------------------------------------------------------------
  // alternar()
// --> alternar() -->
//
  // Cambia el LED al estado contrario al actual.
  // ------------------------------------------------------------
  void alternar () {
	if (encendido) {
	  apagar();
	} else {
	  encender ();
	}
  } // ()

  // ------------------------------------------------------------
  // brillar()
// tiempo: N --> brillar() -->
//
  // Enciende el LED durante el tiempo indicado y después lo apaga.
  // ------------------------------------------------------------
  void brillar (long tiempo) {
	encender ();
	esperar(tiempo); 
	apagar ();
  }
}; // class

#endif
