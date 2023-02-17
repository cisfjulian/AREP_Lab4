package eci.arep;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Clase cache que administra y guarda la informacion de una pelicula
 * de manera temporal
 */

public class Cache {

    private static ConcurrentHashMap<String, String> movies = new ConcurrentHashMap<>();

    /**
     * Metodo que revisa su cache para uso de API o no. Si ya tiene la informacion en el cache
     * no consulta la API, si no la tiene, se remite a la API.
     * @param title recibe el String de la pelicula que se quiere consultar
     * @return String en formato JSON con la informacion de la pelicula
     */

    public static String checkCache(String title) throws IOException {
            if(movies.contains(title)){
                return movies.get(title);
            }
            else{
                String json = HTTPCliente.getAPI(title);
                movies.put(title, json);
                return json;

            }
    }
}
