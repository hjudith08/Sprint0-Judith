// -*- mode: c++ -*-


// --------------------------------------------------------------
// Fichero: Publicador.h
// Descripción: Clase encargada de codificar el tipo y contador en major y el valor de O3 en
//              minor para publicarlo como iBeacon.
// Copyright: material académico y modificaciones del autor.
// Fecha de adaptación: 05-10-2026
// Autora de la adaptación: Judith Civico Aporta
// Autor original: Jordi Bataller i Mascarell en fecha 2019-07-07
// Aportación: publicación del valor de O3 mediante iBeacon para el Sprint 0.
// --------------------------------------------------------------


#ifndef PUBLICADOR_H_INCLUIDO
#define PUBLICADOR_H_INCLUIDO

// ------------------------------------------------------------
// Clase Publicador: prepara y publica las mediciones mediante iBeacon.
// ------------------------------------------------------------
class Publicador {

  // ------------------------------------------------------------
  // Datos privados del publicador
  //
  // Contiene el UUID utilizado para construir los anuncios iBeacon.
  // ------------------------------------------------------------
private:

  uint8_t beaconUUID[16] = {
	'E', 'P', 'S', 'G', '-', 'G', 'T', 'I',
	'-', 'P', 'R', 'O', 'Y', '-', '3', 'A'
	};

  // ------------------------------------------------------------
  // Configuración pública de la emisora
  //
  // Define el nombre JU-GTI, el fabricante y la potencia de transmisión.
  // ------------------------------------------------------------
public:
  EmisoraBLE laEmisora {
	"JU-GTI", //  nombre emisora
	  0x004c, // fabricanteID (Apple)
	  4 // txPower
	  };

  const int RSSI = -53; // por poner algo, de momento no lo uso

public:

  // ------------------------------------------------------------
  // MedicionesID
  //
  // Identificadores numéricos de los tipos de medición codificados
  // en el byte alto del campo major.
  // ------------------------------------------------------------
  enum MedicionesID  {
	O3 = 11,
	TEMPERATURA = 12,
	RUIDO = 13
  };

  // ------------------------------------------------------------
  // Publicador()
// --> Publicador() --> publicador: Publicador
//
  // Crea el publicador sin encender todavía la emisora BLE.
  // ------------------------------------------------------------
  Publicador( ) {
	// ATENCION: no hacerlo aquí. (*this).laEmisora.encenderEmisora();
	// Pondremos un método para llamarlo desde el setup() más tarde
  } // ()

  // ------------------------------------------------------------
  // encenderEmisora()
// --> encenderEmisora() -->
//
  // Enciende la emisora BLE utilizada por el publicador.
  // ------------------------------------------------------------
  void encenderEmisora() {
	(*this).laEmisora.encenderEmisora();
  } // ()

  // ------------------------------------------------------------
  // publicarO3()
// valorO3: Z, contador: N, tiempoEspera: N --> publicarO3() -->
//
  // Codifica el tipo O3 y el contador en major, coloca valorO3 en minor y publica el iBeacon.
  // ------------------------------------------------------------
  void publicarO3( int16_t valorO3, uint8_t contador,
					long tiempoEspera ) {

	//
	// 1. empezamos anuncio
	//
	uint16_t major = (MedicionesID::O3 << 8) + contador;
	(*this).laEmisora.emitirAnuncioIBeacon( (*this).beaconUUID,
											major,
											valorO3, // minor
											(*this).RSSI // rssi
									);

	Globales::elPuerto.escribir( "   publicarO3(): valor=" );
	Globales::elPuerto.escribir( valorO3 );
	Globales::elPuerto.escribir( "   contador=" );
	Globales::elPuerto.escribir( contador );
	Globales::elPuerto.escribir( "   todo="  );
	Globales::elPuerto.escribir( major );
	Globales::elPuerto.escribir( "\n" );

	//
	// 2. esperamos el tiempo que nos digan
	//
	esperar( tiempoEspera );

//
// 3. paramos anuncio
//
// (*this).laEmisora.detenerAnuncio();

} // ()
  // ------------------------------------------------------------
  // publicarTemperatura()
// valorTemperatura: Z, contador: N, tiempoEspera: N --> publicarTemperatura() -->
//
  // Publica una medición de temperatura mediante iBeacon usando el contador recibido.
  // ------------------------------------------------------------
  void publicarTemperatura( int16_t valorTemperatura,
							uint8_t contador, long tiempoEspera ) {

	uint16_t major = (MedicionesID::TEMPERATURA << 8) + contador;
	(*this).laEmisora.emitirAnuncioIBeacon( (*this).beaconUUID,
											major,
											valorTemperatura, // minor
											(*this).RSSI // rssi
									);
	esperar( tiempoEspera );

	(*this).laEmisora.detenerAnuncio();
  } // ()

}; // class

#endif
