package eci.arep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPCliente{

    private static final String USER_AGENT = "Mozilla/5.0";
    private static String URLbase = "http://www.omdbapi.com/?t=";
    private static final String key = "&apikey=89f929b9";
    private static String urlConsulta = "";
    private static String json = "";
    private static String movie = "";

    /**
     * Obtiene la informacion del API
     * @param title recibe el titulo de la pelicula
     * @return String en formato json con la informacion de la pelicula
     */

    public static String getAPI(String title) throws IOException {

        movie = title;

        urlConsulta += URLbase + title + key;

        // System.out.println(urlConsulta);

        URL obj = new URL(urlConsulta);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);

        //The following invocation perform the connection implicitly before getting the code
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // convert result to string and save it
            // System.out.println(response.toString());
            json = response.toString();
        } else {
            System.out.println("GET request not worked");
        }
        System.out.println("GET DONE");
        urlConsulta = "";
        return json;
    }

    public static String getTitle(){
        return movie;
    }

}
