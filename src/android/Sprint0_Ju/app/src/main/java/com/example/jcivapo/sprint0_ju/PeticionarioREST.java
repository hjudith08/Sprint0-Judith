// --------------------------------------------------------------
// Fichero: PeticionarioREST.java
// Descripción: Cliente HTTP encargado de enviar mediciones desde Android al endpoint POST
//              /mediciones.
// Copyright: material académico y modificaciones del autor.
// Fecha de adaptación: 05-10-2026
// Autora de la adaptación: Judith Civico Aporta
// Autor original: Jordi Bataller i Mascarell
// Aportación: comunicación Android -> REST mediante HttpURLConnection.
// --------------------------------------------------------------
package com.example.jcivapo.sprint0_ju;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * PeticionarioREST
 *
 * Se encarga de realizar las peticiones HTTP al servidor REST.
 *
 * En este Sprint 0 únicamente se utiliza para enviar una medición
 * mediante POST /mediciones.
 */
public class PeticionarioREST {

    private static final String URL_MEDICIONES =
            "https://jcivapo.upv.edu.es/rest/index.php/mediciones";
    // ------------------------------------------------------------
    // RespuestaCallback
    //
    // Define la operación de retorno utilizada para comunicar el resultado
    // de una petición REST al código llamador.
    // ------------------------------------------------------------
    public interface RespuestaCallback {

        // ------------------------------------------------------------
        // exito: B, mensaje: Text --> onRespuesta() -->
        // Entrega al llamador el resultado de la petición REST.
        // ------------------------------------------------------------
void onRespuesta(boolean exito, String mensaje);
    }

    // ------------------------------------------------------------
    // medicion: MedicionEntrada, callback: RespuestaCallback
    //      --> enviarMedicion() -->
    //
    // Envía una medición al servidor REST mediante HTTP POST y entrega
    // el resultado de forma asíncrona mediante el callback.
    // ------------------------------------------------------------
    public void enviarMedicion(
            MedicionEntrada medicion,
            RespuestaCallback callback
    ) {

        // La petición HTTP se realiza fuera del hilo principal.
        new Thread(() -> {

            HttpURLConnection conexion = null;

            try {

                // ---------------------------------------------------------
                // Crear JSON que espera nuestro servidor REST
                // ---------------------------------------------------------

                JSONObject jsonMedicion = new JSONObject();

                jsonMedicion.put("tipo", medicion.getTipo());
                jsonMedicion.put("valor", medicion.getValor());


                // ---------------------------------------------------------
                // Crear conexión HTTP
                // ---------------------------------------------------------

                URL url = new URL(URL_MEDICIONES);

                conexion = (HttpURLConnection) url.openConnection();

                conexion.setRequestMethod("POST");

                conexion.setRequestProperty(
                        "Content-Type",
                        "application/json; charset=UTF-8"
                );

                conexion.setRequestProperty(
                        "Accept",
                        "application/json"
                );

                conexion.setConnectTimeout(10000);
                conexion.setReadTimeout(10000);

                conexion.setDoOutput(true);


                // ---------------------------------------------------------
                // Enviar JSON
                // ---------------------------------------------------------

                try (OutputStream os = conexion.getOutputStream()) {

                    byte[] datos =
                            jsonMedicion.toString()
                                    .getBytes(StandardCharsets.UTF_8);

                    os.write(datos);
                }


                // ---------------------------------------------------------
                // Obtener código HTTP
                // ---------------------------------------------------------

                int codigoHTTP = conexion.getResponseCode();


                // Si la respuesta es correcta leemos inputStream.
                // Si hay error leemos errorStream.
                InputStream flujo;

                if (codigoHTTP >= 200 && codigoHTTP < 300) {

                    flujo = conexion.getInputStream();

                } else {

                    flujo = conexion.getErrorStream();
                }


                // ---------------------------------------------------------
                // Leer respuesta
                // ---------------------------------------------------------

                BufferedReader lector =
                        new BufferedReader(
                                new InputStreamReader(
                                        flujo,
                                        StandardCharsets.UTF_8
                                )
                        );

                StringBuilder respuestaTexto =
                        new StringBuilder();

                String linea;

                while ((linea = lector.readLine()) != null) {

                    respuestaTexto.append(linea);
                }

                lector.close();


                // ---------------------------------------------------------
                // Convertir respuesta JSON
                // ---------------------------------------------------------

                JSONObject respuesta =
                        new JSONObject(respuestaTexto.toString());

                boolean exito =
                        respuesta.optBoolean(
                                "exito",
                                codigoHTTP >= 200 && codigoHTTP < 300
                        );

                String mensaje =
                        respuesta.optString(
                                "mensaje",
                                "Sin mensaje"
                        );


                devolverResultado(
                        callback,
                        exito,
                        mensaje
                );


            } catch (Exception e) {

                devolverResultado(
                        callback,
                        false,
                        "Error REST: " + e.getMessage()
                );

            } finally {

                if (conexion != null) {

                    conexion.disconnect();
                }
            }

        }).start();
    }

    // ------------------------------------------------------------
    // callback: RespuestaCallback, exito: B, mensaje: Text
    //      --> devolverResultado() -->
    //
    // Publica el resultado en el hilo principal de Android.
    // ------------------------------------------------------------
    private void devolverResultado(
            RespuestaCallback callback,
            boolean exito,
            String mensaje
    ) {

        new Handler(
                Looper.getMainLooper()
        ).post(() -> {

            if (callback != null) {

                callback.onRespuesta(
                        exito,
                        mensaje
                );
            }
        });
    }
}
