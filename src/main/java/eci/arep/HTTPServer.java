package eci.arep;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.Scanner;

import org.json.*;


/**
 * Crea servidor y sube HTML con datos de la pelicula
 */

public class HTTPServer {

    /**
     * Clase main que inicia el servidor y lo deja listo para conexion con navegador
     */
    private static final HTTPServer instance = new HTTPServer();

    public static HTTPServer getInstance() {
        return instance;
    }

    public static void run(String[] args) throws IOException, ClassNotFoundException{
        String className = args[0];
        // Cargar clase con forname
        try {
            Class<?> c = Class.forName(className);
            Method[] methods = c.getMethods();
            for (Method method: methods){
                if(method.isAnnotationPresent(RequestMapping.class)){
                    String path = method.getAnnotation(RequestMapping.class).value();
                    System.out.println(path);
                }
            }
        } catch (ClassNotFoundException x) {
            x.printStackTrace();
        }

        // extraer metodos con anotacion @RequestMapping
        // Extraer valor path
        // Extraer una instancia del metodo


        String movie = "";
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        Socket clientSocket = null;
        boolean running = true;
        while (running){
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }

//            Scanner keyboard = new Scanner(System.in);
//            System.out.println("Pelicula de la cual desea obtener informacion");
//            movie = keyboard.nextLine();

//            String json = Cache.checkCache("cars");
//            outputLine = jsonHTML(json);
            outputLine = htmlSimple();
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }


    /**
     * Elabora el HTML para ser mostrado en el navegador
     * @param json recibe el String en formato JSON
     * @return String en forma HTML con la informacion
     */

    public static String jsonHTML(String json){
        return "HTTP/1.1 200 OK\r\n"
                + "\r\n"
                + "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>Movies</title>\n"
                + "</head>" +
                "<style>\n" +
                "table {\n" +
                "  font-family: arial, sans-serif;\n" +
                "  border-collapse: collapse;\n" +
                "  width: 100%;\n" +
                "}\n" +
                "\n" +
                "td, th {\n" +
                "  border: 1px solid #dddddd;\n" +
                "  text-align: left;\n" +
                "  padding: 8px;\n" +
                "}\n" +
                "\n" +
                "tr:nth-child(even) {\n" +
                "  background-color: #dddddd;\n" +
                "}\n" +
                "</style>" +
                "<h2>"+HTTPCliente.getTitle().toUpperCase(Locale.ROOT)+"</h2>"+
                "<table> \n"+
                "<tr>\n" +
                "<th>Category</th>" +
                "<th>Data</th>"+
                "</tr>" +
                tabla(json) +
                "</table>";

    }
    /**
     * Crea una tabla en formato HTML
     * @param json recibe el String que esta en formato JSON
     * @return tabla con formato HTML con los datos recibidos del string JSON ya convertido
     */
    public static String tabla(String json){
        String p = "[" + json + "]";
        // System.out.println(p);
        JSONArray a = new JSONArray(p);
        // System.out.println(a);
        String tabla = "";
        for(int i = 0; i<a.length();i++){
            JSONObject tupla = a.getJSONObject(i);
            Set<String> setKeys= tupla.keySet();
            // System.out.println(setKeys);
            for(String value : setKeys) {
                // System.out.println(value + "@" + tupla.get(value));
                tabla += "<tr>\n" + "<td>\n" + value + "</td>\n";
                tabla += "<td>\n" + tupla.get(value) + "</td>\n" + "</tr>\n";
            }
        }
        return tabla.replace("[","").replace("{","").replace("}","").replace("]","");
    }


    public static String htmlSimple(){
        return "HTTP/1.1 200 OK\r\n"
                + "\r\n"
                + "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>Title of the document</title>\n"
                + "</head>"
                + "<body>"
                + "My Web Site"
                + "</body>"
                + "</html>";
    }


    public static String htmlWithForms(){
        return  "HTTP/1.1 200 OK\r\n"
                + "\r\n" +
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Form Example</title>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <h1>Form with GET</h1>\n" +
                "        <form action=\"/hello\">\n" +
                "            <label for=\"name\">Name:</label><br>\n" +
                "            <input type=\"text\" id=\"name\" name=\"name\" value=\"John\"><br><br>\n" +
                "            <input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\">\n" +
                "        </form> \n" +
                "        <div id=\"getrespmsg\"></div>\n" +
                "\n" +
                "        <script>\n" +
                "            function loadGetMsg() {\n" +
                "                let nameVar = document.getElementById(\"name\").value;\n" +
                "                const xhttp = new XMLHttpRequest();\n" +
                "                xhttp.onload = function() {\n" +
                "                    document.getElementById(\"getrespmsg\").innerHTML =\n" +
                "                    this.responseText;\n" +
                "                }\n" +
                "                xhttp.open(\"GET\", \"/hello?name=\"+nameVar);\n" +
                "                xhttp.send();\n" +
                "            }\n" +
                "        </script>\n" +
                "\n" +
                "        <h1>Form with POST</h1>\n" +
                "        <form action=\"/hellopost\">\n" +
                "            <label for=\"postname\">Name:</label><br>\n" +
                "            <input type=\"text\" id=\"postname\" name=\"name\" value=\"John\"><br><br>\n" +
                "            <input type=\"button\" value=\"Submit\" onclick=\"loadPostMsg(postname)\">\n" +
                "        </form>\n" +
                "        \n" +
                "        <div id=\"postrespmsg\"></div>\n" +
                "        \n" +
                "        <script>\n" +
                "            function loadPostMsg(name){\n" +
                "                let url = \"/hellopost?name=\" + name.value;\n" +
                "\n" +
                "                fetch (url, {method: 'POST'})\n" +
                "                    .then(x => x.text())\n" +
                "                    .then(y => document.getElementById(\"postrespmsg\").innerHTML = y);\n" +
                "            }\n" +
                "        </script>\n" +
                "    </body>\n" +
                "</html>";
    }
}

