package com.zenquery;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class StartController {
    @RequestMapping(method = RequestMethod.GET)
    public String welcome() {
        System.out.println("Circle CI test");
        return "index";
    }
}