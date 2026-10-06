// -*- mode: c++ -*-


// --------------------------------------------------------------
// Fichero: Medidor.h
// Descripción: Clase de soporte para la lectura y conversión de la medida de O3; en Sprint
//              0 se conserva para futuras mediciones reales.
// Copyright: material académico y modificaciones del autor.
// Fecha de adaptación: 05-10-2026
// Autora de la adaptación: Judith Civico Aporta
// Autor original: Jordi Bataller i Mascarell en fecha 2019-07-07
// Aportación: configuración del soporte del sensor O3 y mantenimiento de la arquitectura
//             prevista.
// --------------------------------------------------------------

#ifndef MEDIDOR_H_INCLUIDO
#define MEDIDOR_H_INCLUIDO

// ------------------------------------------------------------
// Clase Medidor: agrupa la lectura y conversión de las entradas del sensor.
// ------------------------------------------------------------
class Medidor {

  // ------------------------------------------------------------
  // Configuración interna del medidor
  //
  // Define los pines, resolución ADC, tensión y sensibilidad utilizada.
  // ------------------------------------------------------------
private:

  // Pines conectados al sensor ULPSM-O3
  const int pinVgas  = 5;
  const int pinVref  = 28;
  const int pinVtemp = 29;

  // Resolución ADC configurada a 12 bits
  const int maximoADC = 4095;

  // Tensión de referencia utilizada para convertir
  // las lecturas ADC a voltios
  const float tensionADC = 3.3;

  // Sensibilidad provisional procedente de las pruebas de calibración.
  // No se utiliza en el test funcional del Sprint 0, que emite 1234.
  // Debe sustituirse por el valor calibrado definitivo antes de usar
  // mediciones físicas como datos de producción.
  const float sensibilidadO3 = -56.44;


public:

  // .....................................................
  // constructor
  // .....................................................
  Medidor(  ) {
  } // ()

  // ------------------------------------------------------------
  // iniciarMedidor()
  //
  // Configura las entradas analógicas y establece el ADC a 12 bits.
  // ------------------------------------------------------------
  void iniciarMedidor() {
	// las cosas que no se puedan hacer en el constructor, if any

    pinMode(pinVgas, INPUT);
    pinMode(pinVref, INPUT);
    pinMode(pinVtemp, INPUT);

    // Configuramos el ADC con resolución de 12 bits
    analogReadResolution(12);

  } // ()

  // ------------------------------------------------------------
  // medirO3()
  //
  // Lee Vgas y Vref, calcula una estimación de O3 y devuelve el resultado como entero.
  // ------------------------------------------------------------
  int medirO3() {

    // 
    // 1. Leemos las señales analógicas del sensor
    // 
    int lecturaVgas = analogRead(pinVgas);
    int lecturaVref = analogRead(pinVref);

    // 
    // 2. Convertimos las lecturas ADC a voltios
    // 
    float Vgas =
      ((float) lecturaVgas / maximoADC) * tensionADC;

    float Vref =
      ((float) lecturaVref / maximoADC) * tensionADC;

    // 
    // 3. Calculamos la concentración de O3
    //
    // Según el fabricante:
    //
    // Cx = (Vgas - Vgas0) / M
    //
    // Como primera aproximación:
    //
    // Vgas0 = Vref
    //
    // y para O3:
    //
    // M = -0.030 V/ppm
    // 
    float ppm =
      (Vgas - Vref) / sensibilidadO3;

    // 
    // 4. Evitamos valores negativos debidos
    //    a pequeñas variaciones o ruido
    // 
    if (ppm < 0) {
      ppm = 0;
    }

    // 
    // 5. Información de depuración
    // 

    Globales::elPuerto.escribir( "   medirO3(): Vgas ADC=" );
    Globales::elPuerto.escribir( lecturaVgas );

    Globales::elPuerto.escribir( "   Vref ADC=" );
    Globales::elPuerto.escribir( lecturaVref );

    // Para evitar posibles problemas si PuertoSerie
    // no admite directamente valores float,
    // mostramos la concentración final como entero.
    Globales::elPuerto.escribir( "   O3 ppm=" );
    Globales::elPuerto.escribir( (int) ppm );

    Globales::elPuerto.escribir( "\n" );

    return (int) ppm;

  } // ()

  // ------------------------------------------------------------
  // medirTemperatura()
  //
  // Devuelve el valor de temperatura utilizado actualmente como prueba.
  // ------------------------------------------------------------
  int medirTemperatura() {
	return -12; // qué frío !
  } // ()
	
}; // class

#endif