// --------------------------------------------------------------
// Fichero: TramaIBeacon.java
// Descripción: Clase proporcionada como base docente para interpretar los campos de una
//              trama iBeacon.
// Copyright: material académico y modificaciones del autor.
// Fecha de adaptación: 05-10-2026
// Autora de la adaptación: Judith Civico Aporta
// Autor original: Jordi Bataller i Mascarell
// Aportación: integración de la clase docente de parseo iBeacon en el Sprint 0.
// --------------------------------------------------------------
package com.example.jcivapo.sprint0_ju;

import java.util.Arrays;

public class TramaIBeacon {
    private byte[] prefijo = null; // 9 bytes
    private byte[] uuid = null; // 16 bytes
    private byte[] major = null; // 2 bytes
    private byte[] minor = null; // 2 bytes
    private byte txPower = 0; // 1 byte

    private byte[] losBytes;

    private byte[] advFlags = null; // 3 bytes
    private byte[] advHeader = null; // 2 bytes
    private byte[] companyID = new byte[2]; // 2 bytes
    private byte iBeaconType = 0 ; // 1 byte
    private byte iBeaconLength = 0 ; // 1 byte

    // ------------------------------------------------------------
    // getPrefijo()
    //
    // Devuelve el prefijo de la trama iBeacon.
    // ------------------------------------------------------------
    public byte[] getPrefijo() {
        return prefijo;
    }

    // ------------------------------------------------------------
    // getUUID()
    //
    // Devuelve los 16 bytes que forman el UUID del iBeacon.
    // ------------------------------------------------------------
    public byte[] getUUID() {
        return uuid;
    }

    // ------------------------------------------------------------
    // getMajor()
    //
    // Devuelve los dos bytes del campo major.
    // ------------------------------------------------------------
    public byte[] getMajor() {
        return major;
    }

    // ------------------------------------------------------------
    // getMinor()
    //
    // Devuelve los dos bytes del campo minor.
    // ------------------------------------------------------------
    public byte[] getMinor() {
        return minor;
    }

    // ------------------------------------------------------------
    // getTxPower()
    //
    // Devuelve el valor de potencia TxPower de la trama.
    // ------------------------------------------------------------
    public byte getTxPower() {
        return txPower;
    }

    // ------------------------------------------------------------
    // getLosBytes()
    //
    // Devuelve la trama BLE completa recibida.
    // ------------------------------------------------------------
    public byte[] getLosBytes() {
        return losBytes;
    }

    // ------------------------------------------------------------
    // getAdvFlags()
    //
    // Devuelve los bytes correspondientes a los flags de advertising.
    // ------------------------------------------------------------
    public byte[] getAdvFlags() {
        return advFlags;
    }

    // ------------------------------------------------------------
    // getAdvHeader()
    //
    // Devuelve la cabecera de advertising de la trama.
    // ------------------------------------------------------------
    public byte[] getAdvHeader() {
        return advHeader;
    }

    // ------------------------------------------------------------
    // getCompanyID()
    //
    // Devuelve el identificador de fabricante del iBeacon.
    // ------------------------------------------------------------
    public byte[] getCompanyID() {
        return companyID;
    }

    // ------------------------------------------------------------
    // getiBeaconType()
    //
    // Devuelve el tipo de trama iBeacon.
    // ------------------------------------------------------------
    public byte getiBeaconType() {
        return iBeaconType;
    }

    // ------------------------------------------------------------
    // getiBeaconLength()
    //
    // Devuelve la longitud declarada de los datos iBeacon.
    // ------------------------------------------------------------
    public byte getiBeaconLength() {
        return iBeaconLength;
    }

    // ------------------------------------------------------------
    // TramaIBeacon()
    //
    // Interpreta los bytes recibidos y separa los campos de una trama iBeacon.
    // ------------------------------------------------------------
    public TramaIBeacon(byte[] bytes ) {
        this.losBytes = bytes;

        prefijo = Arrays.copyOfRange(losBytes, 0, 8+1 ); // 9 bytes
        uuid = Arrays.copyOfRange(losBytes, 9, 24+1 ); // 16 bytes
        major = Arrays.copyOfRange(losBytes, 25, 26+1 ); // 2 bytes
        minor = Arrays.copyOfRange(losBytes, 27, 28+1 ); // 2 bytes
        txPower = losBytes[ 29 ]; // 1 byte

        advFlags = Arrays.copyOfRange( prefijo, 0, 2+1 ); // 3 bytes
        advHeader = Arrays.copyOfRange( prefijo, 3, 4+1 ); // 2 bytes
        companyID = Arrays.copyOfRange( prefijo, 5, 6+1 ); // 2 bytes
        iBeaconType = prefijo[ 7 ]; // 1 byte
        iBeaconLength = prefijo[ 8 ]; // 1 byte

    } // ()
} // class
