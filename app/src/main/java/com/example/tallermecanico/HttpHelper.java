package com.example.tallermecanico;

import java.net.HttpURLConnection;

/**
 * Clase auxiliar para configurar peticiones HTTP
 * Ayuda a evitar el error 403 del servidor
 */
public class HttpHelper {

    /**
     * Configura headers comunes para todas las peticiones
     * Esto hace que las peticiones parezcan venir de un navegador real
     */
    public static void configurarHeaders(HttpURLConnection conn) {
        // Headers que hacen que la petición parezca de un navegador
        conn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

        conn.setRequestProperty("Accept",
                "application/json, text/plain, */*");

        conn.setRequestProperty("Accept-Language",
                "es-ES,es;q=0.9,en;q=0.8");

        conn.setRequestProperty("Accept-Encoding",
                "gzip, deflate, br");

        conn.setRequestProperty("Connection",
                "keep-alive");

        conn.setRequestProperty("Cache-Control",
                "no-cache");

        conn.setRequestProperty("Pragma",
                "no-cache");

        // Referer (importante para algunos servidores)
        conn.setRequestProperty("Referer",
                "https://smayckel.xo.je/");

        // Origin (para peticiones POST/PUT/DELETE)
        conn.setRequestProperty("Origin",
                "https://smayckel.xo.je");
    }

    /**
     * Configura headers específicos para peticiones POST/PUT
     */
    public static void configurarHeadersContenido(HttpURLConnection conn) {
        configurarHeaders(conn);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    }
}