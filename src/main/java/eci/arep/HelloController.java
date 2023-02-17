package eci.arep;

public class HelloController {
    @RequestMapping("/hello")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}
