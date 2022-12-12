package com.example.proyectovalorant.controller;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

//Todo 0. Revisa las consideraciones previas del proyecto!!

//Todo 1.Clase que gestiona la conexión con la base de datos externa, a través de una url base a la
// que debemos concatenarle los "endpoint" o final de url para recibir la información que necesitemos.
// Así mismo los parametros que se quieran enviar se forman en la URL a través del caracter '?'
public class HttpConnectValorant {

    //Todo 1.1 Declaramos la url base, que no cambia.
    private static final String URL_BASE = "https://valorant-api.com/v1";

    //Todo 1.2 Definimos el método para peticiones GET el cual se usará para la consulta de
    // información
    public static String getRequest(String endpoint) {

        HttpURLConnection http = null;
        String content = null;
        try {
            //Todo 1.3 Se forma la url más el endpoint. Así como la cabecera, que permitira decidir
            // la codificación de los datos que se están trasmitiendo.
            URL url = new URL( URL_BASE + endpoint );
            Log.d("U", "Url: " + url );
            http = (HttpURLConnection)url.openConnection();
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("Accept", "application/json");

            //Todo 1.4 Si el servidor devuelve un codigo 200 (HTTP_OK == 200)
            // quiere decir que ha devuelto correctamente la información solicitada.
            if(http.getResponseCode() == HttpURLConnection.HTTP_OK ) {
                Log.d("E", "Entra");
                //Todo 1.5 Se codifica el texto de la respuesta como String.
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader( http.getInputStream() ));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                content = sb.toString();
                reader.close();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {

            //1.5 Se desconecta la conexión.
            if( http != null ) http.disconnect();
        }
        Log.d("C", "Content: "+content);
        return content;
    }

    //Todo 1.6 Esta función define el tipo de petición POST que se usa para la modificación de
    // la información en base de datos externas. En este caso los parametros van encapsulados en
    // la petición. En este método los parametros se están pasando a través de 'params'
    public static int postRequest( String strUrl, String params )
    {
        HttpURLConnection http = null;
        int responseCode = -1;
        try {
            URL url = new URL( strUrl );
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("Accept", "application/json");
            http.setDoOutput(true);


            /*
              Otra forma de formar los datos sería la siguiente:

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("firstParam", paramValue1)
                    .appendQueryParameter("secondParam", paramValue2)
                    .appendQueryParameter("thirdParam", paramValue3);
            String query = builder.build().getEncodedQuery();
            */

            PrintWriter writer = new PrintWriter(http.getOutputStream());
            writer.print(params); //Aquí se le pasaría la variable creada query
            writer.flush();
            responseCode = http.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (http != null) http.disconnect();
        }
        return responseCode;
    }

}
