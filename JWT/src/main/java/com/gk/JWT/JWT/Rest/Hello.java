package com.gk.JWT.JWT.Rest;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {

    @GetMapping("/hello")
    public String helloWorld(){
        return "Hello World!!!";
    }
}
