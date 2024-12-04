package artesanas.artesanas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectToSwagger() {
        return "redirect:/doc/swagger-ui/index.html"; // Redirige a Swagger
    }
}