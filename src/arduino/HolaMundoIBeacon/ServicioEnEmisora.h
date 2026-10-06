// -*- mode: c++ -*-


// --------------------------------------------------------------
// Fichero: ServicioEnEmisora.h
// Descripción: Clase de apoyo para definir y gestionar servicios BLE asociados a la
//              emisora.
// Copyright: material académico y modificaciones del autor.
// Fecha de adaptación: 05-10-2026
// Autora de la adaptación: Judith Civico Aporta
// Autor original: Jordi Bataller i Mascarell en fecha 2019-07-07
// Aportación: integración del código docente de servicios BLE en el proyecto.
// --------------------------------------------------------------


#ifndef SERVICIO_EMISORA_H_INCLUIDO
#define SERVICIO_EMISORA_H_INCLUIDO

// ------------------------------------------------------------
// Contenedor utilizado para almacenar las características del servicio.
// ------------------------------------------------------------
#include <vector>

// ----------------------------------------------------
// alReves() utilidad
// pone al revés el contenido de una array en el mismo array
// ----------------------------------------------------
template< typename T >
T *  alReves( T * p, int n ) {
  T aux;

  for( int i=0; i < n/2; i++ ) {
	aux = p[i];
	p[i] = p[n-i-1];
	p[n-i-1] = aux;
  }
  return p;
} // ()

// ------------------------------------------------------------
// stringAUint8AlReves()
//
// Copia una cadena a un array uint8_t invirtiendo el orden de sus bytes.
// ------------------------------------------------------------
uint8_t * stringAUint8AlReves( const char * pString, uint8_t * pUint, int tamMax ) {

	int longitudString =  strlen( pString );
	int longitudCopiar = ( longitudString > tamMax ? tamMax : longitudString );
	// copio nombreServicio -> uuidServicio pero al revés
	for( int i=0; i<=longitudCopiar-1; i++ ) {
	  pUint[ tamMax-i-1 ] = pString[ i ];
	} // for

	return pUint;
} // ()

// ------------------------------------------------------------
// Clase ServicioEnEmisora: gestiona servicios y características BLE.
// ------------------------------------------------------------
class ServicioEnEmisora {

public:


  // ------------------------------------------------------------
  // CallbackCaracteristicaEscrita
  //
  // Tipo de función llamada cuando una característica BLE recibe una escritura.
  // ------------------------------------------------------------
  using CallbackCaracteristicaEscrita = void ( uint16_t conn_handle,
											   BLECharacteristic * chr,
											   uint8_t * data, uint16_t len); 
  // ------------------------------------------------------------
  // Caracteristica
  //
  // Clase interna que encapsula una característica BLE y su configuración.
  // ------------------------------------------------------------
  class Caracteristica {
  private:
	uint8_t uuidCaracteristica[16] = { // el uuid se copia aquí (al revés) a partir de un string-c
	  // least signficant byte, el primero
	  '0', '1', '2', '3', 
	  '4', '5', '6', '7', 
	  '8', '9', 'A', 'B', 
	  'C', 'D', 'E', 'F'
	};

	// 
	// 
	// 
	BLECharacteristic laCaracteristica;

  public:

	// ------------------------------------------------------------
	// Caracteristica()
	//
	// Crea una característica BLE a partir del identificador indicado.
	// ------------------------------------------------------------
	Caracteristica( const char * nombreCaracteristica_ )
	  :
	  laCaracteristica( stringAUint8AlReves( nombreCaracteristica_, &uuidCaracteristica[0], 16 ) )
	{
	  
	} // ()

	// ------------------------------------------------------------
	// Caracteristica()
	//
	// Crea una característica y configura propiedades, permisos y tamaño de datos.
	// ------------------------------------------------------------
	Caracteristica( const char * nombreCaracteristica_ ,
					uint8_t props,
					SecureMode_t permisoRead,
					SecureMode_t permisoWrite, 
					uint8_t tam ) 
	  :
	  Caracteristica( nombreCaracteristica_ ) // llamada al otro constructor
	{
	  (*this).asignarPropiedadesPermisosYTamanyoDatos( props, permisoRead, permisoWrite, tam );
	} // ()

  private:
	// .........................................................
	// CHR_PROPS_WRITE , CHR_PROPS_READ ,  CHR_PROPS_NOTIFY 
	// .........................................................
	void asignarPropiedades ( uint8_t props ) {
	  // no puedo escribir AUN si el constructor llama a esto: Serial.println( " laCaracteristica.setProperties( props ); ");
	  (*this).laCaracteristica.setProperties( props );
	} // ()

	// .........................................................
	// BleSecurityMode::SECMODE_OPEN  , BleSecurityMode::SECMODE_NO_ACCESS
	// .........................................................
	void asignarPermisos( SecureMode_t permisoRead, SecureMode_t permisoWrite ) {
	  // no puedo escribir AUN si el constructor llama a esto: Serial.println( "laCaracteristica.setPermission( permisoRead, permisoWrite ); " );
	  (*this).laCaracteristica.setPermission( permisoRead, permisoWrite );
	} // ()

	// ------------------------------------------------------------
	// asignarTamanyoDatos()
	//
	// Establece el tamaño máximo de datos admitido por la característica.
	// ------------------------------------------------------------
	void asignarTamanyoDatos( uint8_t tam ) {
	  // no puedo escribir AUN si el constructor llama a esto: Serial.print( " (*this).laCaracteristica.setFixedLen( tam = " );
	  // no puedo escribir AUN si el constructor llama a esto: Serial.println( tam );
	  // (*this).laCaracteristica.setFixedLen( tam );
	  (*this).laCaracteristica.setMaxLen( tam );
	} // ()

  public:
	// ------------------------------------------------------------
	// asignarPropiedadesPermisosYTamanyoDatos()
	//
	// Aplica conjuntamente propiedades, permisos y tamaño de datos a la característica.
	// ------------------------------------------------------------
	void asignarPropiedadesPermisosYTamanyoDatos( uint8_t props,
												 SecureMode_t permisoRead,
												 SecureMode_t permisoWrite, 
												 uint8_t tam ) {
	  asignarPropiedades( props );
	  asignarPermisos( permisoRead, permisoWrite );
	  asignarTamanyoDatos( tam );
	} // ()
												 

	// ------------------------------------------------------------
	// escribirDatos()
	//
	// Escribe la cadena recibida en la característica y devuelve el número de bytes escritos.
	// ------------------------------------------------------------
	uint16_t escribirDatos( const char * str ) {
	  // Serial.print( " return (*this).laCaracteristica.write( str  = " );
	  // Serial.println( str );

	  uint16_t r = (*this).laCaracteristica.write( str );

	  // Serial.print( ">>>Escritos " ); Serial.print( r ); Serial.println( " bytes con write() " );

	  return r;
	} // ()

	// ------------------------------------------------------------
	// notificarDatos()
	//
	// Envía una notificación BLE con los datos recibidos.
	// ------------------------------------------------------------
	uint16_t notificarDatos( const char * str ) {
	  
	  uint16_t r = laCaracteristica.notify( &str[0] );

	  return r;
	} //  ()

	// ------------------------------------------------------------
	// instalarCallbackCaracteristicaEscrita()
	//
	// Registra el callback que se ejecutará cuando la característica sea escrita.
	// ------------------------------------------------------------
	void instalarCallbackCaracteristicaEscrita( CallbackCaracteristicaEscrita cb ) {
	  (*this).laCaracteristica.setWriteCallback( cb );
	} // ()

	// ------------------------------------------------------------
	// activar()
	//
	// Inicializa la característica BLE y registra el resultado por el puerto serie.
	// ------------------------------------------------------------
	void activar() {
	  err_t error = (*this).laCaracteristica.begin();
	  Globales::elPuerto.escribir(  " (*this).laCaracteristica.begin(); error = " );
	  Globales::elPuerto.escribir(  error );
	} // ()

  }; // class Caracteristica
  
  // ------------------------------------------------------------
  // Datos internos del servicio BLE.
  // ------------------------------------------------------------
private:
  
  uint8_t uuidServicio[16] = { // el uuid se copia aquí (al revés) a partir de un string-c
	// least signficant byte, el primero
	'0', '1', '2', '3', 
	'4', '5', '6', '7', 
	'8', '9', 'A', 'B', 
	'C', 'D', 'E', 'F'
  };

  //
  //
  //
  BLEService elServicio;

  //
  //
  //
  std::vector< Caracteristica * > lasCaracteristicas;

public:
  
  // ------------------------------------------------------------
  // ServicioEnEmisora()
  //
  // Crea un servicio BLE a partir del identificador recibido.
  // ------------------------------------------------------------
  ServicioEnEmisora( const char * nombreServicio_ )
	:
	elServicio( stringAUint8AlReves( nombreServicio_, &uuidServicio[0], 16 ) )
  {
	
  } // ()
  
  // ------------------------------------------------------------
  // escribeUUID()
  //
  // Muestra por el puerto serie el UUID asociado al servicio.
  // ------------------------------------------------------------
  void escribeUUID() {
	Serial.println ( "**********" );
	for (int i=0; i<= 15; i++) {
	  Serial.print( (char) uuidServicio[i] );
	}
	Serial.println ( "\n**********" );
  } // ()

  // ------------------------------------------------------------
  // anyadirCaracteristica()
  //
  // Añade una característica a la colección interna del servicio.
  // ------------------------------------------------------------
  void anyadirCaracteristica( Caracteristica & car ) {
	(*this).lasCaracteristicas.push_back( & car );
  } // ()

  // ------------------------------------------------------------
  // activarServicio()
  //
  // Inicializa el servicio BLE y activa todas sus características.
  // ------------------------------------------------------------
  void activarServicio( ) {
	// entiendo que al llegar aquí ya ha sido configurado
	// todo: características y servicio

	err_t error = (*this).elServicio.begin();
	Serial.print( " (*this).elServicio.begin(); error = " );
	Serial.println( error );

	for( auto pCar : (*this).lasCaracteristicas ) {
	  (*pCar).activar();
	} // for

  } // ()

  // ------------------------------------------------------------
  // operator BLEService&()
  //
  // Permite utilizar ServicioEnEmisora donde se espera un BLEService.
  // ------------------------------------------------------------
  operator BLEService&() {
	// "conversión de tipo": si pongo esta clase en un sitio donde necesitan un BLEService
	return elServicio;
  } // ()
	
}; // class

#endif
