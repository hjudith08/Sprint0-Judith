// --------------------------------------------------------------
// Fichero: MainActivity.java
// Descripción: Actividad principal. Escanea el iBeacon JU-GTI, interpreta major/minor y
//              envía la medición de O3 recibida al servidor REST.
// Copyright: material académico y modificaciones del autor.
// Fecha de adaptación: 05-10-2026
// Autora de la adaptación: Judith Civico Aporta
// Aportación: adaptación BLE para Android actual e integración de la cadena BLE -> REST.
// --------------------------------------------------------------
package com.example.jcivapo.sprint0_ju;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    // ----------------------------------------------------
    // Constantes
    // ----------------------------------------------------

    private static final String ETIQUETA_LOG = ">>>>";

    private static final String ETIQUETA_SPRINT0 = "SPRINT0";

    private static final int CODIGO_PETICION_PERMISOS = 100;

    private static final String NOMBRE_BEACON = "JU-GTI";

    private static final int TIPO_O3 = 11;

    // Evita subir varias veces el mismo anuncio mientras se repite.
    private int ultimoMajorEnviado = -1;


    // ----------------------------------------------------
    // Bluetooth
    // ----------------------------------------------------

    private BluetoothAdapter bluetoothAdapter;

    private BluetoothLeScanner elEscanner;

    private ScanCallback callbackDelEscaneo;


    // ------------------------------------------------------------
    // onCreate()
    //
    // Inicializa la actividad, prepara Bluetooth y solicita los permisos necesarios.
    // ------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        Log.d(
                ETIQUETA_LOG,
                "onCreate(): empieza"
        );


        // ----------------------------------------------------
        // Inicializar Bluetooth
        // ----------------------------------------------------

        inicializarBluetooth();


        // ----------------------------------------------------
        // Solicitar permisos Bluetooth si hacen falta
        // ----------------------------------------------------

        if (!tenemosPermisosBluetooth()) {

            solicitarPermisosBluetooth();
        }


        Log.d(
                ETIQUETA_LOG,
                "onCreate(): termina"
        );
    }

    // ------------------------------------------------------------
    // inicializarBluetooth()
    //
    // Obtiene el adaptador Bluetooth del dispositivo y comprueba que esté disponible.
    // ------------------------------------------------------------

    private void inicializarBluetooth() {

        Log.d(
                ETIQUETA_LOG,
                "inicializarBluetooth()"
        );


        BluetoothManager bluetoothManager =
                (BluetoothManager)
                        getSystemService(
                                Context.BLUETOOTH_SERVICE
                        );


        if (bluetoothManager == null) {

            Log.e(
                    ETIQUETA_LOG,
                    "No se ha podido obtener BluetoothManager"
            );

            return;
        }


        bluetoothAdapter =
                bluetoothManager.getAdapter();


        if (bluetoothAdapter == null) {

            Log.e(
                    ETIQUETA_LOG,
                    "Este dispositivo no dispone de Bluetooth"
            );


            Toast.makeText(
                    this,
                    "Este dispositivo no dispone de Bluetooth",
                    Toast.LENGTH_LONG
            ).show();


            return;
        }


        Log.d(
                ETIQUETA_LOG,
                "Adaptador Bluetooth obtenido"
        );
    }


    // ------------------------------------------------------------
    // tenemosPermisosBluetooth()
    //
    // Comprueba si la aplicación dispone de los permisos Bluetooth necesarios.
    // ------------------------------------------------------------

    private boolean tenemosPermisosBluetooth() {

        // Android 12 o superior
        if (
                Build.VERSION.SDK_INT
                        >= Build.VERSION_CODES.S
        ) {

            boolean permisoScan =
                    ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.BLUETOOTH_SCAN
                    )
                            == PackageManager.PERMISSION_GRANTED;


            boolean permisoConnect =
                    ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.BLUETOOTH_CONNECT
                    )
                            == PackageManager.PERMISSION_GRANTED;


            return permisoScan && permisoConnect;
        }


        // Android 11 o inferior
        return ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        )
                == PackageManager.PERMISSION_GRANTED;
    }


    // ------------------------------------------------------------
    // solicitarPermisosBluetooth()
    //
    // Solicita al usuario los permisos Bluetooth necesarios según la versión de Android.
    // ------------------------------------------------------------

    private void solicitarPermisosBluetooth() {

        Log.d(
                ETIQUETA_LOG,
                "solicitarPermisosBluetooth()"
        );


        if (
                Build.VERSION.SDK_INT
                        >= Build.VERSION_CODES.S
        ) {

            ActivityCompat.requestPermissions(
                    this,

                    new String[]{
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_CONNECT
                    },

                    CODIGO_PETICION_PERMISOS
            );

        } else {

            ActivityCompat.requestPermissions(
                    this,

                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },

                    CODIGO_PETICION_PERMISOS
            );
        }
    }


    // ------------------------------------------------------------
    // prepararEscaner()
    //
    // Comprueba permisos, Bluetooth y disponibilidad del escáner antes de iniciar una búsqueda.
    // ------------------------------------------------------------

    private boolean prepararEscaner() {

        if (!tenemosPermisosBluetooth()) {

            solicitarPermisosBluetooth();


            Toast.makeText(
                    this,
                    "Debes conceder los permisos Bluetooth",
                    Toast.LENGTH_SHORT
            ).show();


            return false;
        }


        if (bluetoothAdapter == null) {

            inicializarBluetooth();
        }


        if (bluetoothAdapter == null) {

            return false;
        }


        // Android 12+
        if (
                Build.VERSION.SDK_INT
                        >= Build.VERSION_CODES.S
        ) {

            if (
                    ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.BLUETOOTH_CONNECT
                    )
                            != PackageManager.PERMISSION_GRANTED
            ) {

                return false;
            }
        }


        // Bluetooth apagado
        if (!bluetoothAdapter.isEnabled()) {

            Log.d(
                    ETIQUETA_LOG,
                    "Bluetooth desactivado"
            );


            Intent enableBtIntent =
                    new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE
                    );


            startActivity(enableBtIntent);


            Toast.makeText(
                    this,
                    "Activa Bluetooth y vuelve a pulsar buscar",
                    Toast.LENGTH_SHORT
            ).show();


            return false;
        }


        elEscanner =
                bluetoothAdapter.getBluetoothLeScanner();


        if (elEscanner == null) {

            Log.e(
                    ETIQUETA_LOG,
                    "No se ha podido obtener BluetoothLeScanner"
            );


            return false;
        }


        return true;
    }


    // ------------------------------------------------------------
    // buscarTodosLosDispositivosBTLE()
    //
    // Inicia un escaneo BLE sin filtrar por nombre de dispositivo.
    // ------------------------------------------------------------

    private void buscarTodosLosDispositivosBTLE() {

        Log.d(
                ETIQUETA_LOG,
                "buscarTodosLosDispositivosBTLE(): empieza"
        );


        if (!prepararEscaner()) {

            return;
        }


        crearCallbackEscaneo();


        if (
                Build.VERSION.SDK_INT
                        >= Build.VERSION_CODES.S
                        &&
                        ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.BLUETOOTH_SCAN
                        )
                                != PackageManager.PERMISSION_GRANTED
        ) {

            return;
        }


        Log.d(
                ETIQUETA_LOG,
                "Empezando escaneo BLE de todos los dispositivos"
        );


        elEscanner.startScan(
                callbackDelEscaneo
        );
    }


    // ------------------------------------------------------------
    // buscarEsteDispositivoBTLE()
    //
    // Inicia un escaneo BLE filtrado por el nombre del dispositivo indicado.
    // ------------------------------------------------------------

    private void buscarEsteDispositivoBTLE(
            final String dispositivoBuscado
    ) {

        Log.d(
                ETIQUETA_LOG,
                "buscarEsteDispositivoBTLE(): "
                        + dispositivoBuscado
        );


        if (!prepararEscaner()) {

            return;
        }


        crearCallbackEscaneo();


        ScanFilter filtro =
                new ScanFilter.Builder()
                        .setDeviceName(
                                dispositivoBuscado
                        )
                        .build();


        List<ScanFilter> filtros =
                Collections.singletonList(
                        filtro
                );


        ScanSettings settings =
                new ScanSettings.Builder()
                        .setScanMode(
                                ScanSettings.SCAN_MODE_LOW_LATENCY
                        )
                        .build();


        if (
                Build.VERSION.SDK_INT
                        >= Build.VERSION_CODES.S
                        &&
                        ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.BLUETOOTH_SCAN
                        )
                                != PackageManager.PERMISSION_GRANTED
        ) {

            return;
        }


        Log.d(
                ETIQUETA_LOG,
                "Buscando: "
                        + dispositivoBuscado
        );


        elEscanner.startScan(
                filtros,
                settings,
                callbackDelEscaneo
        );
    }


    // ------------------------------------------------------------
    // crearCallbackEscaneo()
    //
    // Crea el callback que procesa los resultados y errores del escaneo BLE.
    // ------------------------------------------------------------

    private void crearCallbackEscaneo() {

        callbackDelEscaneo =
                new ScanCallback() {

                    @Override
                    public void onScanResult(
                            int callbackType,
                            ScanResult resultado
                    ) {

                        super.onScanResult(
                                callbackType,
                                resultado
                        );


                        mostrarInformacionDispositivoBTLE(
                                resultado
                        );
                    }


                    @Override
                    public void onBatchScanResults(
                            List<ScanResult> results
                    ) {

                        super.onBatchScanResults(
                                results
                        );


                        for (
                                ScanResult resultado :
                                results
                        ) {

                            mostrarInformacionDispositivoBTLE(
                                    resultado
                            );
                        }
                    }


                    @Override
                    public void onScanFailed(
                            int errorCode
                    ) {

                        super.onScanFailed(
                                errorCode
                        );


                        Log.e(
                                ETIQUETA_LOG,
                                "Error de escaneo BLE: "
                                        + errorCode
                        );
                    }
                };
    }


    // ------------------------------------------------------------
    // mostrarInformacionDispositivoBTLE()
    //
    // Interpreta una trama BLE detectada, muestra sus campos y procesa el iBeacon recibido.
    // ------------------------------------------------------------

    private void mostrarInformacionDispositivoBTLE(
            ScanResult resultado
    ) {

        if (resultado == null) {

            return;
        }


        if (resultado.getScanRecord() == null) {

            return;
        }


        BluetoothDevice bluetoothDevice =
                resultado.getDevice();


        byte[] bytes =
                resultado
                        .getScanRecord()
                        .getBytes();


        int rssi =
                resultado.getRssi();


        if (
                Build.VERSION.SDK_INT
                        >= Build.VERSION_CODES.S
                        &&
                        ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.BLUETOOTH_CONNECT
                        )
                                != PackageManager.PERMISSION_GRANTED
        ) {

            return;
        }


        String nombre =
                bluetoothDevice.getName();


        String direccion =
                bluetoothDevice.getAddress();


        Log.d(
                ETIQUETA_LOG,
                "****************************************************"
        );

        Log.d(
                ETIQUETA_LOG,
                "****** DISPOSITIVO DETECTADO BTLE ******************"
        );

        Log.d(
                ETIQUETA_LOG,
                "****************************************************"
        );


        Log.d(
                ETIQUETA_LOG,
                "nombre = "
                        + nombre
        );


        Log.d(
                ETIQUETA_LOG,
                "dirección = "
                        + direccion
        );


        Log.d(
                ETIQUETA_LOG,
                "rssi = "
                        + rssi
        );


        Log.d(
                ETIQUETA_LOG,
                "bytes ("
                        + bytes.length
                        + ") = "
                        + Utilidades.bytesToHexString(
                        bytes
                )
        );


        /*
         * La clase TramaIBeacon proporcionada por
         * el profesor espera al menos los primeros
         * 30 bytes de la trama.
         */
        if (bytes.length < 30) {

            Log.d(
                    ETIQUETA_LOG,
                    "No parece una trama iBeacon válida"
            );

            return;
        }


        try {

            TramaIBeacon tib =
                    new TramaIBeacon(
                            bytes
                    );


            Log.d(
                    ETIQUETA_LOG,
                    "----------------------------------------------------"
            );


            Log.d(
                    ETIQUETA_LOG,
                    "prefijo = "
                            + Utilidades.bytesToHexString(
                            tib.getPrefijo()
                    )
            );


            Log.d(
                    ETIQUETA_LOG,
                    "advFlags = "
                            + Utilidades.bytesToHexString(
                            tib.getAdvFlags()
                    )
            );


            Log.d(
                    ETIQUETA_LOG,
                    "advHeader = "
                            + Utilidades.bytesToHexString(
                            tib.getAdvHeader()
                    )
            );


            Log.d(
                    ETIQUETA_LOG,
                    "companyID = "
                            + Utilidades.bytesToHexString(
                            tib.getCompanyID()
                    )
            );


            Log.d(
                    ETIQUETA_LOG,
                    "iBeacon type = "
                            + Integer.toHexString(
                            tib.getiBeaconType()
                    )
            );


            Log.d(
                    ETIQUETA_LOG,
                    "iBeacon length = "
                            + tib.getiBeaconLength()
            );


            Log.d(
                    ETIQUETA_LOG,
                    "uuid HEX = "
                            + Utilidades.bytesToHexString(
                            tib.getUUID()
                    )
            );


            Log.d(
                    ETIQUETA_LOG,
                    "uuid texto = "
                            + Utilidades.bytesToString(
                            tib.getUUID()
                    )
            );


            Log.d(
                    ETIQUETA_LOG,
                    "major = "
                            + Utilidades.bytesToHexString(
                            tib.getMajor()
                    )
                            + " ("
                            + Utilidades.bytesToInt(
                            tib.getMajor()
                    )
                            + ")"
            );


            Log.d(
                    ETIQUETA_LOG,
                    "minor = "
                            + Utilidades.bytesToHexString(
                            tib.getMinor()
                    )
                            + " ("
                            + Utilidades.bytesToInt(
                            tib.getMinor()
                    )
                            + ")"
            );


            Log.d(
                    ETIQUETA_LOG,
                    "txPower = "
                            + tib.getTxPower()
            );


            enviarMedicionIBeaconAlREST(
                    nombre,
                    tib
            );


            Log.d(
                    ETIQUETA_LOG,
                    "****************************************************"
            );


        } catch (Exception e) {

            Log.e(
                    ETIQUETA_LOG,
                    "Error procesando trama iBeacon",
                    e
            );
        }
    }


    /*
    ----------------------------------------------------
    nombre: Text, trama: TramaIBeacon
        -->
    enviarMedicionIBeaconAlREST() -->

    Si el anuncio pertenece a JU-GTI y su major identifica O3,
    crea una MedicionEntrada con el minor y la envía al REST.
    Un mismo major solo se envía una vez.
    ----------------------------------------------------
    */
    private void enviarMedicionIBeaconAlREST(
            String nombre,
            TramaIBeacon trama
    ) {

        if (!NOMBRE_BEACON.equals(nombre)) {
            return;
        }

        int major =
                Utilidades.bytesToInt(
                        trama.getMajor()
                );

        int tipoMedicion =
                (major >> 8) & 0xFF;

        if (tipoMedicion != TIPO_O3) {
            return;
        }

        if (major == ultimoMajorEnviado) {
            return;
        }

        int valorO3 =
                Utilidades.bytesToInt(
                        trama.getMinor()
                );

        MedicionEntrada medicion =
                new MedicionEntrada(
                        "O3",
                        valorO3
                );

        ultimoMajorEnviado = major;

        PeticionarioREST peticionarioREST =
                new PeticionarioREST();

        peticionarioREST.enviarMedicion(
                medicion,
                (exito, mensaje) -> {

                    Log.d(
                            ETIQUETA_SPRINT0,
                            "BLE -> REST: major="
                                    + major
                                    + " valor="
                                    + valorO3
                                    + " exito="
                                    + exito
                                    + " mensaje="
                                    + mensaje
                    );

                    if (!exito) {
                        // Permite reintentar el mismo anuncio si el POST falló.
                        ultimoMajorEnviado = -1;
                    }
                }
        );
    }


    // ------------------------------------------------------------
    // detenerBusquedaDispositivosBTLE()
    //
    // Detiene el escaneo BLE activo y libera el callback asociado.
    // ------------------------------------------------------------

    private void detenerBusquedaDispositivosBTLE() {

        if (
                callbackDelEscaneo == null
                        ||
                        elEscanner == null
        ) {

            return;
        }


        if (
                Build.VERSION.SDK_INT
                        >= Build.VERSION_CODES.S
                        &&
                        ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.BLUETOOTH_SCAN
                        )
                                != PackageManager.PERMISSION_GRANTED
        ) {

            return;
        }


        elEscanner.stopScan(
                callbackDelEscaneo
        );


        callbackDelEscaneo = null;


        Log.d(
                ETIQUETA_LOG,
                "Escaneo detenido"
        );
    }


    // ------------------------------------------------------------
    // botonBuscarDispositivosBTLEPulsado()
    //
    // Gestiona la pulsación del botón que inicia la búsqueda de todos los dispositivos BLE.
    // ------------------------------------------------------------

    public void botonBuscarDispositivosBTLEPulsado(
            View v
    ) {

        Log.d(
                ETIQUETA_LOG,
                "Botón buscar dispositivos BTLE pulsado"
        );


        buscarTodosLosDispositivosBTLE();
    }


    // ------------------------------------------------------------
    // botonBuscarNuestroDispositivoBTLEPulsado()
    //
    // Gestiona la pulsación del botón que busca únicamente el beacon JU-GTI.
    // ------------------------------------------------------------

    public void botonBuscarNuestroDispositivoBTLEPulsado(
            View v
    ) {

        Log.d(
                ETIQUETA_LOG,
                "Botón buscar nuestro dispositivo pulsado"
        );


        buscarEsteDispositivoBTLE(
                NOMBRE_BEACON
        );
    }


    // ------------------------------------------------------------
    // botonDetenerBusquedaDispositivosBTLEPulsado()
    //
    // Gestiona la pulsación del botón que detiene el escaneo BLE.
    // ------------------------------------------------------------

    public void botonDetenerBusquedaDispositivosBTLEPulsado(
            View v
    ) {

        Log.d(
                ETIQUETA_LOG,
                "Botón detener búsqueda pulsado"
        );


        detenerBusquedaDispositivosBTLE();
    }


    // ------------------------------------------------------------
    // onRequestPermissionsResult()
    //
    // Procesa la respuesta del usuario a la solicitud de permisos Bluetooth.
    // ------------------------------------------------------------

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults
    ) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );


        if (
                requestCode
                        == CODIGO_PETICION_PERMISOS
        ) {

            if (tenemosPermisosBluetooth()) {

                Log.d(
                        ETIQUETA_LOG,
                        "Permisos Bluetooth concedidos"
                );


                Toast.makeText(
                        this,
                        "Permisos Bluetooth concedidos",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                Log.e(
                        ETIQUETA_LOG,
                        "Permisos Bluetooth NO concedidos"
                );


                Toast.makeText(
                        this,
                        "Se necesitan permisos Bluetooth",
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }


    // ------------------------------------------------------------
    // onDestroy()
    //
    // Detiene cualquier escaneo BLE antes de destruir la actividad.
    // ------------------------------------------------------------

    @Override
    protected void onDestroy() {

        detenerBusquedaDispositivosBTLE();

        super.onDestroy();
    }
}