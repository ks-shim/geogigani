package dwayne.shim.geogigani.allinone.data.service.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/index")
public class IndexController {

    @RequestMapping(value = {"/to-be-path"})
    public String getToBeIndexPath() {
        return "";
    }
}
