package dwayne.shim.geogigani.front.service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/main-page")
public class FrontController {

    @RequestMapping(value = {"/popular-locations"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public String showPopularLocations(Model model) {
        return "main-page";
    }
}
