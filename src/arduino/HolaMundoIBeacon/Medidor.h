// -*- mode: c++ -*-

#ifndef MEDIDOR_H_INCLUIDO
#define MEDIDOR_H_INCLUIDO

// ------------------------------------------------------
// ------------------------------------------------------
class Medidor {

  // .....................................................
  // .....................................................
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

  // Sensibilidad típica del ULPSM-O3
  // -30 mV/ppm = -0.030 V/ppm
  //
  // Más adelante se puede sustituir por el valor
  // específico de calibración del sensor.
  const float sensibilidadO3 = -56.44;


public:

  // .....................................................
  // constructor
  // .....................................................
  Medidor(  ) {
  } // ()

  // .....................................................
  // .....................................................
  void iniciarMedidor() {
	// las cosas que no se puedan hacer en el constructor, if any

    pinMode(pinVgas, INPUT);
    pinMode(pinVref, INPUT);
    pinMode(pinVtemp, INPUT);

    // Configuramos el ADC con resolución de 12 bits
    analogReadResolution(12);

  } // ()

  // .....................................................
  // .....................................................
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

  // .....................................................
  // .....................................................
  int medirTemperatura() {
	return -12; // qué frío !
  } // ()
	
}; // class

// ------------------------------------------------------
// ------------------------------------------------------
// ------------------------------------------------------
// ------------------------------------------------------
#endif