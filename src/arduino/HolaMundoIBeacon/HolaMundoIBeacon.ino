// -*-c++-*-


// --------------------------------------------------------------
// Fichero: HolaMundoIBeacon.ino
// Descripción: Programa principal del Sprint 0. Emite por iBeacon una medición ficticia de
//              O3 para validar la cadena Arduino -> Android -> REST -> BD -> Web.
// Copyright: material académico y modificaciones del autor.
// Fecha de adaptación: 05-10-2026
// Autora de la adaptación: Judith Civico Aporta
// Autor original: Jordi Bataller i Mascarell en fecha 2019-07-07
// Aportación: integración de O3 ficticio, contador y publicación BLE.
// --------------------------------------------------------------

// https://learn.sparkfun.com/tutorials/nrf52840-development-with-arduino-and-circuitpython

// https://stackoverflow.com/questions/29246805/can-an-ibeacon-have-a-data-payload

// --------------------------------------------------------------
// Librería principal de Bluetooth Low Energy.
// --------------------------------------------------------------
#include <bluefruit.h>

#undef min // vaya tela, están definidos en bluefruit.h y  !
#undef max // colisionan con los de la biblioteca estándar

// --------------------------------------------------------------
// Clases auxiliares utilizadas por el programa principal.
// --------------------------------------------------------------
#include "LED.h"
#include "PuertoSerie.h"

// --------------------------------------------------------------
// Objetos globales compartidos por setup() y loop().
// --------------------------------------------------------------
namespace Globales {
  
  LED elLED ( /* NUMERO DEL PIN LED = */ 7 );

  PuertoSerie elPuerto ( /* velocidad = */ 115200 ); // 115200 o 9600 o ...

  // Serial1 en el ejemplo de Curro creo que es la conexión placa-sensor 
};

// --------------------------------------------------------------
// Clases responsables de la emisión BLE y publicación iBeacon.
// --------------------------------------------------------------
#include "EmisoraBLE.h"
#include "Publicador.h"
#include "Medidor.h"


// --------------------------------------------------------------
// Objetos globales compartidos por setup() y loop().
// --------------------------------------------------------------
namespace Globales {

  Publicador elPublicador;

  Medidor elMedidor;

}; // namespace

// ------------------------------------------------------------
// inicializarPlaquita()
//
// Inicializa los elementos básicos utilizados por la placa.
// ------------------------------------------------------------
void inicializarPlaquita () {

  // de momento nada

} // ()

// ------------------------------------------------------------
// setup()
//
// Inicializa el puerto serie, la emisora BLE y el medidor antes de comenzar el programa.
// ------------------------------------------------------------
void setup() {

  Globales::elPuerto.esperarDisponible();

  // Inicializar los componentes básicos de la placa.
  inicializarPlaquita();

  // Suspend Loop() to save power
  // suspendLoop();

  // Encender la emisora BLE.
  Globales::elPublicador.encenderEmisora();

  // Globales::elPublicador.laEmisora.pruebaEmision();
  
  // Preparar el ADC y las entradas del medidor.
  Globales::elMedidor.iniciarMedidor();

  // Dar un pequeño margen antes de comenzar el bucle principal.
  esperar( 1000 );

  Globales::elPuerto.escribir( "---- setup(): fin ---- \n " );

} // setup ()

// ------------------------------------------------------------
// lucecitas()
//
// Hace parpadear el LED para indicar visualmente que la placa sigue ejecutándose.
// ------------------------------------------------------------
inline void lucecitas() {
  using namespace Globales;

  elLED.brillar( 100 ); // 100 encendido
  esperar ( 400 ); //  100 apagado
  elLED.brillar( 100 ); // 100 encendido
  esperar ( 400 ); //  100 apagado
  Globales::elLED.brillar( 100 ); // 100 encendido
  esperar ( 400 ); //  100 apagado
  Globales::elLED.brillar( 1000 ); // 1000 encendido
  esperar ( 1000 ); //  100 apagado
} // ()

// ------------------------------------------------------------
// Estado utilizado por loop().
//
// Mantiene el contador que identifica cada nueva medición publicada.
// ------------------------------------------------------------
namespace Loop {
  uint8_t cont = 0;
};

// ------------------------------------------------------------
// loop()
//
// Genera la medición ficticia de O3, incrementa el contador y publica el iBeacon.
// ------------------------------------------------------------
void loop () {

  using namespace Loop;
  using namespace Globales;

  cont++;

  elPuerto.escribir( "\n---- loop(): empieza " );
  elPuerto.escribir( cont );
  elPuerto.escribir( "\n" );


  // lucecitas();

  // ------------------------------------------------------------
  // valorO3: Z --> publicar medición ficticia de Sprint 0
  //
  // La rúbrica exige que el valor ficticio nazca en el código
  // de la placa. El minor del iBeacon transporta este 1234.
  // ------------------------------------------------------------
  int valorO3 = 1234;

  elPuerto.escribir( "O3 ficticio = " );
  elPuerto.escribir( valorO3 );
  elPuerto.escribir( "\n" );
  
  elPublicador.publicarO3( valorO3,
							cont,
							1000 // intervalo de emisión
							);
  
  // 
  // mido y publico
  // 

  /*
  int valorTemperatura = elMedidor.medirTemperatura();
  
  elPublicador.publicarTemperatura( valorTemperatura, 
									cont,
									1000 // intervalo de emisión
									);
  */

  // 
  // prueba para emitir un iBeacon y poner
  // en la carga (21 bytes = uuid 16 major 2 minor 2 txPower 1 )
  // lo que queramos (sin seguir dicho formato)
  // 
  // Al terminar la prueba hay que hacer Publicador::laEmisora privado
  // 

  /*
  char datos[21] = {
	'H', 'o', 'l', 'a',
	'H', 'o', 'l', 'a',
	'H', 'o', 'l', 'a',
	'H', 'o', 'l', 'a',
	'H', 'o', 'l', 'a',
	'H'
  };

  // elPublicador.laEmisora.emitirAnuncioIBeaconLibre ( &datos[0], 21 );
  elPublicador.laEmisora.emitirAnuncioIBeaconLibre ( "MolaMolaMolaMolaMolaM", 21 );
  */

  esperar( 1000 );

  elPublicador.laEmisora.detenerAnuncio();
  
  // 
  // 
  // 
  elPuerto.escribir( "---- loop(): acaba **** " );
  elPuerto.escribir( cont );
  elPuerto.escribir( "\n" );
  
} // loop ()
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------