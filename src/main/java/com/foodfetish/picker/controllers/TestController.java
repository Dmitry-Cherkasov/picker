package com.foodfetish.picker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class TestController {

    @GetMapping("/test")
    public @ResponseBody String hello(){
        return "<h1><b>Test</b> <br>  <i>Test</i> <br> <u>Test</u></h1>";
    }
}
