package eci.arep;

import java.io.IOException;

public class FirstApp {
    public static void  main(String[] args) throws IOException, ClassNotFoundException {
        HTTPServer server = HTTPServer.getInstance();
        server.run(args);
    }
}
